package com.orange.qos.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class HistoriqueAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHistoriqueAllPropertiesEquals(Historique expected, Historique actual) {
        assertHistoriqueAutoGeneratedPropertiesEquals(expected, actual);
        assertHistoriqueAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHistoriqueAllUpdatablePropertiesEquals(Historique expected, Historique actual) {
        assertHistoriqueUpdatableFieldsEquals(expected, actual);
        assertHistoriqueUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHistoriqueAutoGeneratedPropertiesEquals(Historique expected, Historique actual) {
        assertThat(actual)
            .as("Verify Historique auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHistoriqueUpdatableFieldsEquals(Historique expected, Historique actual) {
        assertThat(actual)
            .as("Verify Historique relevant properties")
            .satisfies(a -> assertThat(a.getUtilisateur()).as("check utilisateur").isEqualTo(expected.getUtilisateur()))
            .satisfies(a -> assertThat(a.getSection()).as("check section").isEqualTo(expected.getSection()))
            .satisfies(a -> assertThat(a.getHorodatage()).as("check horodatage").isEqualTo(expected.getHorodatage()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHistoriqueUpdatableRelationshipsEquals(Historique expected, Historique actual) {
        assertThat(actual)
            .as("Verify Historique relationships")
            .satisfies(a -> assertThat(a.getDegradation()).as("check degradation").isEqualTo(expected.getDegradation()));
    }
}
