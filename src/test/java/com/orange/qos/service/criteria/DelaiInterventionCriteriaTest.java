package com.orange.qos.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DelaiInterventionCriteriaTest {

    @Test
    void newDelaiInterventionCriteriaHasAllFiltersNullTest() {
        var delaiInterventionCriteria = new DelaiInterventionCriteria();
        assertThat(delaiInterventionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void delaiInterventionCriteriaFluentMethodsCreatesFiltersTest() {
        var delaiInterventionCriteria = new DelaiInterventionCriteria();

        setAllFilters(delaiInterventionCriteria);

        assertThat(delaiInterventionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void delaiInterventionCriteriaCopyCreatesNullFilterTest() {
        var delaiInterventionCriteria = new DelaiInterventionCriteria();
        var copy = delaiInterventionCriteria.copy();

        assertThat(delaiInterventionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(delaiInterventionCriteria)
        );
    }

    @Test
    void delaiInterventionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var delaiInterventionCriteria = new DelaiInterventionCriteria();
        setAllFilters(delaiInterventionCriteria);

        var copy = delaiInterventionCriteria.copy();

        assertThat(delaiInterventionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(delaiInterventionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var delaiInterventionCriteria = new DelaiInterventionCriteria();

        assertThat(delaiInterventionCriteria).hasToString("DelaiInterventionCriteria{}");
    }

    private static void setAllFilters(DelaiInterventionCriteria delaiInterventionCriteria) {
        delaiInterventionCriteria.id();
        delaiInterventionCriteria.dateDebut();
        delaiInterventionCriteria.dateLimite();
        delaiInterventionCriteria.commentaire();
        delaiInterventionCriteria.statut();
        delaiInterventionCriteria.degradationId();
        delaiInterventionCriteria.utilisateurId();
        delaiInterventionCriteria.distinct();
    }

    private static Condition<DelaiInterventionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDateDebut()) &&
                condition.apply(criteria.getDateLimite()) &&
                condition.apply(criteria.getCommentaire()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getDegradationId()) &&
                condition.apply(criteria.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DelaiInterventionCriteria> copyFiltersAre(
        DelaiInterventionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDateDebut(), copy.getDateDebut()) &&
                condition.apply(criteria.getDateLimite(), copy.getDateLimite()) &&
                condition.apply(criteria.getCommentaire(), copy.getCommentaire()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getDegradationId(), copy.getDegradationId()) &&
                condition.apply(criteria.getUtilisateurId(), copy.getUtilisateurId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
