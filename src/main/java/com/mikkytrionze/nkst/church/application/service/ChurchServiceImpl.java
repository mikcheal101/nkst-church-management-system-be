package com.mikkytrionze.nkst.church.application.service;

import com.mikkytrionze.nkst.church.application.mapper.ChurchMapper;
import com.mikkytrionze.nkst.church.api.request.ChurchRequest;
import com.mikkytrionze.nkst.shared.exception.ResourceNotFoundException;
import com.mikkytrionze.nkst.church.api.response.ChurchResponse;
import com.mikkytrionze.nkst.church.domain.model.Church;
import com.mikkytrionze.nkst.church.domain.repository.ChurchRepository;
import com.mikkytrionze.nkst.church.domain.service.ChurchService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ChurchServiceImpl implements ChurchService {

    private final ChurchMapper churchMapper;
    private final ChurchRepository churchRepository;

    @Override
    public void deleteChurch(Long id) throws ResourceNotFoundException {
        log.info("Deleting church with id: {}", id);

        Church church = getChurchById(id);

        church.setDeleted(true);

        churchRepository.save(church);
    }

    @Override
    public ChurchResponse getChurch(Long id) {
        log.info("Get Church with id: {}", id);
        Church church = getChurchById(id);
        return churchMapper.toResponse(church);
    }

    @Override
    public ChurchResponse createChurch(ChurchRequest churchRequest) {
        log.info("Creating a church entity with name: {}", churchRequest.getName());

        Church parentChurch = churchRequest.getParentChurchId() == null ? null : churchRepository.findById(churchRequest.getParentChurchId()).orElse(null);

        Church church = Church.builder()
                .name(churchRequest.getName().toUpperCase())
                .address(churchRequest.getAddress())
                .parentChurch(parentChurch)
                .build();
        Church savedChurch = churchRepository.save(church);
        return churchMapper.toResponse(savedChurch);
    }

    @Override
    public ChurchResponse updateChurch(Long id, ChurchRequest churchRequest) throws DataIntegrityViolationException {
        log.info("Updating a church with id: {}", id);

        Church existingChurch = getChurchById(id);

        boolean nameExists = existingChurch.getName().equalsIgnoreCase(churchRequest.getName());
        boolean nameExistsInDb = churchRepository.existsByName(churchRequest.getName());

        if(!nameExists && nameExistsInDb) {
            throw new DataIntegrityViolationException(
                    String.format("A church with the name: '%s' already exists", churchRequest.getName()));
        }

        // get the new parent church
        boolean hasParentChurch = churchRequest.getParentChurchId() != null;
        boolean requestingParentChurchChange = churchRequest.getParentChurchId() != existingChurch.getParentChurch().getId();
        if (hasParentChurch && requestingParentChurchChange) {
            Church parentChurch = churchRepository.findById(churchRequest.getParentChurchId()).orElseGet(null);
            existingChurch.setParentChurch(parentChurch);
        }

        existingChurch.setName(churchRequest.getName().toUpperCase());
        existingChurch.setAddress(churchRequest.getAddress());

        Church updatedChurch = churchRepository.save(existingChurch);
        return churchMapper.toResponse(updatedChurch);
    }

    @Override
    public Page<ChurchResponse> getChurches(Pageable pageable) {
        log.info("Fetching all the churches with pagination: {}", pageable);

        Page<Church> churchPage = churchRepository.findAll(pageable);

        return churchPage.map(churchMapper::toResponse);
    }

    @Override
    public Church findChurchById(Long id) {
        return churchRepository.findById(id).orElse(null);
    }

    private Church getChurchById(Long id) {
        return churchRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(Church.class.getName(), id));
    }
}
