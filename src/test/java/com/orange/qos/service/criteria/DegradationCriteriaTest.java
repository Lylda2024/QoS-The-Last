package com.orange.qos.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DegradationCriteriaTest {

    @Test
    void newDegradationCriteriaHasAllFiltersNullTest() {
        var degradationCriteria = new DegradationCriteria();
        assertThat(degradationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void degradationCriteriaFluentMethodsCreatesFiltersTest() {
        var degradationCriteria = new DegradationCriteria();

        setAllFilters(degradationCriteria);

        assertThat(degradationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void degradationCriteriaCopyCreatesNullFilterTest() {
        var degradationCriteria = new DegradationCriteria();
        var copy = degradationCriteria.copy();

        assertThat(degradationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(degradationCriteria)
        );
    }

    @Test
    void degradationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var degradationCriteria = new DegradationCriteria();
        setAllFilters(degradationCriteria);

        var copy = degradationCriteria.copy();

        assertThat(degradationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(degradationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var degradationCriteria = new DegradationCriteria();

        assertThat(degradationCriteria).hasToString("DegradationCriteria{}");
    }

    private static void setAllFilters(DegradationCriteria degradationCriteria) {
        degradationCriteria.id();
        degradationCriteria.numero();
        degradationCriteria.localite();
        degradationCriteria.contactTemoin();
        degradationCriteria.typeAnomalie();
        degradationCriteria.priorite();
        degradationCriteria.problem();
        degradationCriteria.porteur();
        degradationCriteria.actionsEffectuees();
        degradationCriteria.utilisateurId();
        degradationCriteria.siteId();
        degradationCriteria.distinct();
    }

    private static Condition<DegradationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNumero()) &&
                condition.apply(criteria.getLocalite()) &&
                condition.apply(criteria.getContactTemoin()) &&
                condition.apply(criteria.getTypeAnomalie()) &&
                condition.apply(criteria.getPriorite()) &&
                condition.apply(criteria.getProblem()) &&
                condition.apply(criteria.getPorteur()) &&
                condition.apply(criteria.getActionsEffectuees()) &&
                condition.apply(criteria.getUtilisateurId()) &&
                condition.apply(criteria.getSiteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DegradationCriteria> copyFiltersAre(DegradationCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNumero(), copy.getNumero()) &&
                condition.apply(criteria.getLocalite(), copy.getLocalite()) &&
                condition.apply(criteria.getContactTemoin(), copy.getContactTemoin()) &&
                condition.apply(criteria.getTypeAnomalie(), copy.getTypeAnomalie()) &&
                condition.apply(criteria.getPriorite(), copy.getPriorite()) &&
                condition.apply(criteria.getProblem(), copy.getProblem()) &&
                condition.apply(criteria.getPorteur(), copy.getPorteur()) &&
                condition.apply(criteria.getActionsEffectuees(), copy.getActionsEffectuees()) &&
                condition.apply(criteria.getUtilisateurId(), copy.getUtilisateurId()) &&
                condition.apply(criteria.getSiteId(), copy.getSiteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
