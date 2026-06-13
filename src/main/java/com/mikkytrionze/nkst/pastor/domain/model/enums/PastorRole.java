package com.mikkytrionze.nkst.pastor.domain.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PastorRole {
    LEAD("Lead Pastor"),
    ASSISTANT("Assistant Pastor"),
    ASSOCIATE("Associate Pastor"),
    YOUTH("Youth Pastor");

    private final String description;

    @JsonCreator
    public static PastorRole fromString(String value) {
        return Arrays.stream(values())
                .filter(role ->
                        role.name().equalsIgnoreCase(value) || role.getDescription().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown Pastor Role"));
    }

    PastorRole(String description) {
        this.description = description;
    }
}
