package com.mikkytrionze.nkst.member.domain.service;

import com.mikkytrionze.nkst.member.domain.model.BaptismRecord;

public interface BaptismRecordService {
    void delete(Long id);
    BaptismRecord getById(Long id);
    BaptismRecord save(BaptismRecord baptismRecord);
    BaptismRecord update(Long id, BaptismRecord baptismRecord);
}
