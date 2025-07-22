package com.orange.qos.repository;

import com.orange.qos.domain.Utilisateur;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Implémentation pour charger les relations de type "bag" (collections)
 * afin d'éviter LazyInitializationException et MultipleBagFetchException.
 *
 * Basé sur la stratégie recommandée par Vlad Mihalcea :
 * https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class UtilisateurRepositoryWithBagRelationshipsImpl implements UtilisateurRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String UTILISATEURS_PARAMETER = "utilisateurs";

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Charge les rôles (ManyToMany) pour un seul utilisateur.
     */
    @Override
    public Optional<Utilisateur> fetchBagRelationships(Optional<Utilisateur> utilisateur) {
        return utilisateur.map(this::fetchRoles);
    }

    /**
     * Charge les rôles pour une page d'utilisateurs.
     */
    @Override
    public Page<Utilisateur> fetchBagRelationships(Page<Utilisateur> utilisateurs) {
        return new PageImpl<>(
            fetchBagRelationships(utilisateurs.getContent()),
            utilisateurs.getPageable(),
            utilisateurs.getTotalElements()
        );
    }

    /**
     * Charge les rôles pour une liste d'utilisateurs.
     */
    @Override
    public List<Utilisateur> fetchBagRelationships(List<Utilisateur> utilisateurs) {
        return Optional.of(utilisateurs).map(this::fetchRoles).orElse(Collections.emptyList());
    }

    /**
     * 🔹 Fetch pour un seul utilisateur
     */
    private Utilisateur fetchRoles(Utilisateur utilisateur) {
        return entityManager
            .createQuery(
                "SELECT u FROM Utilisateur u " +
                "LEFT JOIN FETCH u.roles " +
                "LEFT JOIN FETCH u.typeUtilisateur " + // On peut aussi fetch ManyToOne
                "WHERE u.id = :id",
                Utilisateur.class
            )
            .setParameter(ID_PARAMETER, utilisateur.getId())
            .getSingleResult();
    }

    /**
     * 🔹 Fetch pour plusieurs utilisateurs (batch)
     */
    private List<Utilisateur> fetchRoles(List<Utilisateur> utilisateurs) {
        if (utilisateurs.isEmpty()) {
            return Collections.emptyList();
        }

        // Conserver l'ordre initial
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, utilisateurs.size()).forEach(i -> order.put(utilisateurs.get(i).getId(), i));

        List<Utilisateur> result = entityManager
            .createQuery(
                "SELECT DISTINCT u FROM Utilisateur u " +
                "LEFT JOIN FETCH u.roles " +
                "LEFT JOIN FETCH u.typeUtilisateur " +
                "WHERE u IN :utilisateurs",
                Utilisateur.class
            )
            .setParameter(UTILISATEURS_PARAMETER, utilisateurs)
            .getResultList();

        // Réordonner comme la liste d'entrée
        result.sort((u1, u2) -> Integer.compare(order.get(u1.getId()), order.get(u2.getId())));
        return result;
    }
}
