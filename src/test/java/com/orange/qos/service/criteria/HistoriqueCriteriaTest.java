package com.orange.qos.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HistoriqueCriteriaTest {

    @Test
    void newHistoriqueCriteriaHasAllFiltersNullTest() {
        var historiqueCriteria = new HistoriqueCriteria();
        assertThat(historiqueCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void historiqueCriteriaFluentMethodsCreatesFiltersTest() {
        var historiqueCriteria = new HistoriqueCriteria();

        setAllFilters(historiqueCriteria);

        assertThat(historiqueCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void historiqueCriteriaCopyCreatesNullFilterTest() {
        var historiqueCriteria = new HistoriqueCriteria();
        var copy = historiqueCriteria.copy();

        assertThat(historiqueCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(historiqueCriteria)
        );
    }

    @Test
    void historiqueCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var historiqueCriteria = new HistoriqueCriteria();
        setAllFilters(historiqueCriteria);

        var copy = historiqueCriteria.copy();

        assertThat(historiqueCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(historiqueCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var historiqueCriteria = new HistoriqueCriteria();

        assertThat(historiqueCriteria).hasToString("HistoriqueCriteria{}");
    }

    private static void setAllFilters(HistoriqueCriteria historiqueCriteria) {
        historiqueCriteria.id();
        historiqueCriteria.utilisateur();
        historiqueCriteria.section();
        historiqueCriteria.horodatage();
        historiqueCriteria.degradationId();
        historiqueCriteria.distinct();
    }

    private static Condition<HistoriqueCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUtilisateur()) &&
                condition.apply(criteria.getSection()) &&
                condition.apply(criteria.getHorodatage()) &&
                condition.apply(criteria.getDegradationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HistoriqueCriteria> copyFiltersAre(HistoriqueCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUtilisateur(), copy.getUtilisateur()) &&
                condition.apply(criteria.getSection(), copy.getSection()) &&
                condition.apply(criteria.getHorodatage(), copy.getHorodatage()) &&
                condition.apply(criteria.getDegradationId(), copy.getDegradationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
