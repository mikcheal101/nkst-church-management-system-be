package com.mikkytrionze.nkst.pastor.domain.model.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PastorRoleTest {

    @Test
    void shouldReturnEnumFromValidName() {
        assertEquals(PastorRole.LEAD, PastorRole.fromString("LEAD"));
        assertEquals(PastorRole.ASSISTANT, PastorRole.fromString("ASSISTANT"));
        assertEquals(PastorRole.ASSOCIATE, PastorRole.fromString("ASSOCIATE"));
        assertEquals(PastorRole.YOUTH, PastorRole.fromString("YOUTH"));
    }

    @Test
    void shouldReturnEnumFromCaseInsensitiveName() {
        assertEquals(PastorRole.LEAD, PastorRole.fromString("lead"));
        assertEquals(PastorRole.ASSISTANT, PastorRole.fromString("assistant"));
    }

    @Test
    void shouldReturnEnumFromDescription() {
        assertEquals(PastorRole.LEAD, PastorRole.fromString("Lead Pastor"));
        assertEquals(PastorRole.ASSISTANT, PastorRole.fromString("Assistant Pastor"));
        assertEquals(PastorRole.ASSOCIATE, PastorRole.fromString("Associate Pastor"));
        assertEquals(PastorRole.YOUTH, PastorRole.fromString("Youth Pastor"));
    }

    @Test
    void shouldThrowExceptionForInvalidRole() {
        assertThrows(IllegalArgumentException.class, () -> PastorRole.fromString("InvalidRole"));
    }

    @Test
    void shouldHaveCorrectDescriptions() {
        assertEquals("Lead Pastor", PastorRole.LEAD.getDescription());
        assertEquals("Assistant Pastor", PastorRole.ASSISTANT.getDescription());
        assertEquals("Associate Pastor", PastorRole.ASSOCIATE.getDescription());
        assertEquals("Youth Pastor", PastorRole.YOUTH.getDescription());
    }
}
