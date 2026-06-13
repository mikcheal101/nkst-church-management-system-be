package com.mikkytrionze.nkst.pastor.application.service;

import com.mikkytrionze.nkst.pastor.api.response.PastorResponse;
import com.mikkytrionze.nkst.pastor.application.mapper.PastorMapper;
import com.mikkytrionze.nkst.pastor.api.request.PastorRequest;
import com.mikkytrionze.nkst.church.domain.model.Church;
import com.mikkytrionze.nkst.pastor.domain.model.Pastor;
import com.mikkytrionze.nkst.pastor.domain.model.enums.PastorRole;
import com.mikkytrionze.nkst.pastor.domain.repository.PastorRepository;
import com.mikkytrionze.nkst.church.domain.service.ChurchService;
import com.mikkytrionze.nkst.pastor.domain.service.PastorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PastorServiceImpl implements PastorService {

    private final PastorRepository pastorRepository;
    private final PastorMapper pastorMapper;
    private final ChurchService churchService;

    @Override
    public Page<PastorResponse> getPastors(Pageable pageable) {
        log.info("Loading pastors with pageable: {}", pageable);

        Page<Pastor> pastorsPage = pastorRepository.findAll(pageable);

        return pastorsPage.map(pastorMapper::toResponse);
    }

    @Override
    public PastorResponse getPastorById(Long id) throws IllegalArgumentException {
        log.info("Getting pastor by id: {}", id);

        Pastor pastor = findPastorById(id);

        return pastorMapper.toResponse(pastor);
    }

    @Override
    public PastorResponse createPastor(PastorRequest pastorRequest) {
        log.info("Creating a pastor with name: {} {}", pastorRequest.getFirstName(), pastorRequest.getLastName());

        // get the church
        Church church = churchService.findChurchById(pastorRequest.getChurchId());

        Pastor pastor = Pastor.builder()
                .firstName(pastorRequest.getFirstName())
                .lastName(pastorRequest.getLastName())
                .middleName(pastorRequest.getMiddleName())
                .emailAddress(pastorRequest.getEmailAddress())
                .pastorRole(PastorRole.fromString(pastorRequest.getPastorRole()))
                .church(church)
                .build();

        pastorRepository.save(pastor);
        return pastorMapper.toResponse(pastor);
    }

    @Override
    public PastorResponse updatePastor(Long id, PastorRequest pastorRequest) throws IllegalArgumentException {
        log.info("Updating a pastor entity with id: {}", id);

        // fetch the pastor
        Pastor pastor = findPastorById(id);

        pastor.setTel(pastorRequest.getTel());
        pastor.setEmailAddress(pastorRequest.getEmailAddress());
        pastor.setFirstName(pastorRequest.getFirstName());
        pastor.setLastName(pastorRequest.getLastName());
        pastor.setMiddleName(pastorRequest.getMiddleName());
        pastor.setPastorRole(PastorRole.fromString(pastorRequest.getPastorRole()));

        Church church = churchService.findChurchById(pastorRequest.getChurchId());
        pastor.setChurch(church);

        pastorRepository.save(pastor);

        return pastorMapper.toResponse(pastor);
    }

    @Override
    public void deletePastorById(Long id) throws IllegalArgumentException {
        log.info("Deleting a pastor entity with id: {}", id);

        Pastor pastor = findPastorById(id);

        pastor.setDeleted(true);

        pastorRepository.save(pastor);
    }

    private Pastor findPastorById(Long id) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("Id is required!");
        }

        return pastorRepository.findById(id).orElse(null);
    }
}
