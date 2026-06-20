package com.mikkytrionze.nkst.member.domain.model;

import com.mikkytrionze.nkst.member.domain.enums.Gender;
import com.mikkytrionze.nkst.shared.domain.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "members")
@SuperBuilder(toBuilder = true)
@SQLRestriction("deleted = false")
public class Member extends Auditable {

    @Column(unique = true, nullable = false)
    private String tel;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Gender gender = Gender.MALE;

    private String middleName;

    @Column(unique = true)
    private String emailAddress;

    @Builder.Default
    private boolean isBaptised = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "baptism_record_id", referencedColumnName = "id")
    private BaptismRecord baptismRecord;
}
