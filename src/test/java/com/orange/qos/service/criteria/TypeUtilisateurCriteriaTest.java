package com.orange.qos.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TypeUtilisateurCriteriaTest {

    @Test
    void newTypeUtilisateurCriteriaHasAllFiltersNullTest() {
        var typeUtilisateurCriteria = new TypeUtilisateurCriteria();
        assertThat(typeUtilisateurCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void typeUtilisateurCriteriaFluentMethodsCreatesFiltersTest() {
        var typeUtilisateurCriteria = new TypeUtilisateurCriteria();

        setAllFilters(typeUtilisateurCriteria);

        assertThat(typeUtilisateurCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void typeUtilisateurCriteriaCopyCreatesNullFilterTest() {
        var typeUtilisateurCriteria = new TypeUtilisateurCriteria();
        var copy = typeUtilisateurCriteria.copy();

        assertThat(typeUtilisateurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(typeUtilisateurCriteria)
        );
    }

    @Test
    void typeUtilisateurCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var typeUtilisateurCriteria = new TypeUtilisateurCriteria();
        setAllFilters(typeUtilisateurCriteria);

        var copy = typeUtilisateurCriteria.copy();

        assertThat(typeUtilisateurCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(typeUtilisateurCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var typeUtilisateurCriteria = new TypeUtilisateurCriteria();

        assertThat(typeUtilisateurCriteria).hasToString("TypeUtilisateurCriteria{}");
    }

    private static void setAllFilters(TypeUtilisateurCriteria typeUtilisateurCriteria) {
        typeUtilisateurCriteria.id();
        typeUtilisateurCriteria.nom();
        typeUtilisateurCriteria.description();
        typeUtilisateurCriteria.niveau();
        typeUtilisateurCriteria.permissions();
        typeUtilisateurCriteria.distinct();
    }

    private static Condition<TypeUtilisateurCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNom()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getNiveau()) &&
                condition.apply(criteria.getPermissions()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TypeUtilisateurCriteria> copyFiltersAre(
        TypeUtilisateurCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNom(), copy.getNom()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getNiveau(), copy.getNiveau()) &&
                condition.apply(criteria.getPermissions(), copy.getPermissions()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
