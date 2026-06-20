package com.mikkytrionze.nkst.member.domain.service;

import com.mikkytrionze.nkst.member.domain.model.Member;

/**
 * Service interface for Member domain operations.
 * Handles lifecycle, retrieval, and business logic for Member entities.
 */
public interface MemberService {

    /**
     * Performs a soft-delete of the member identified by ID.
     *
     * @param id The unique identifier of the member to be removed.
     */
    void deleteMember(Long id);

    /**
     * Retrieves the details of a specific member.
     *
     * @param id The unique identifier of the member.
     * @return The {@link Member} matching the requested ID.
     */
    Member getById(Long id);

    /**
     * Persists a new member record in the system.
     *
     * @param member The Member entity to be saved.
     * @return The saved {@link Member} including the generated ID.
     */
    Member saveMember(Member member);

    /**
     * Updates information for an existing member.
     *
     * @param id The unique identifier of the member to update.
     * @param member The Member entity containing the updated details.
     * @return The updated {@link Member}.
     */
    Member updateMember(Long id, Member member);
}
