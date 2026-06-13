package com.mikkytrionze.nkst.church.domain.repository;

import com.mikkytrionze.nkst.church.domain.model.Church;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChurchRepository extends JpaRepository<Church, Long> {
    Boolean existsByName(String name);
}
