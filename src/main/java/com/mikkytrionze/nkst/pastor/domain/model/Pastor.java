package com.mikkytrionze.nkst.pastor.domain.model;

import com.mikkytrionze.nkst.shared.domain.Auditable;
import com.mikkytrionze.nkst.church.domain.model.Church;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pastors")
@SuperBuilder(toBuilder = true)
@SQLRestriction("deleted = false")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Pastor extends Auditable {

    @EqualsAndHashCode.Include
    @Column(unique = true, nullable = false)
    private String tel;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @Builder.Default
    private String gender = "M";

    private String middleName;

    @Column(unique = true)
    private String emailAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_id")
    private Church church;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pastor_role_id", nullable = false)
    private PastorRole pastorRole;
}
