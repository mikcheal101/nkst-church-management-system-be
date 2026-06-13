package com.mikkytrionze.nkst.church.domain.model;

import com.mikkytrionze.nkst.shared.domain.Auditable;
import com.mikkytrionze.nkst.pastor.domain.model.Pastor;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "churches")
@SuperBuilder(toBuilder = true)
@SQLRestriction("deleted = false")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Church extends Auditable {

    @EqualsAndHashCode.Include
    @Column(unique = true, nullable = false)
    private String name;

    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "church_id")
    private Church parentChurch;

    @Builder.Default
    @OneToMany(mappedBy = "parentChurch", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Church> subChurches = new LinkedHashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "church", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Pastor> pastors = new LinkedHashSet<>();

    public void addPastor(Pastor pastor) {
        pastors.add(pastor);
        pastor.setChurch(this);
    }

    public void removePastor(Pastor pastor) {
        pastors.remove(pastor);
        pastor.setChurch(null);
    }

    public void addSubChurch(Church church) {
        subChurches.add(church);
        church.setParentChurch(this);
    }

    public void removeSubChurch(Church church) {
        subChurches.remove(church);
        church.setParentChurch(null);
    }
}
