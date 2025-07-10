package com.orange.qos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *  Utilisateur.
 */
@Entity
@Table(name = "utilisateur")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_utilisateur_id") // précisé explicitement
    private TypeUtilisateur typeUtilisateur;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_utilisateur__roles",
        joinColumns = @JoinColumn(name = "utilisateur_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id") // correction ici : "role_id" au singulier
    )
    @JsonIgnoreProperties(value = { "utilisateurs" }, allowSetters = true)
    private Set<Role> roles = new HashSet<>();

    // Getters, setters et méthodes de chainage fluide

    public Long getId() {
        return this.id;
    }

    public Utilisateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Utilisateur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Utilisateur prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return this.email;
    }

    public Utilisateur email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return this.motDePasse;
    }

    public Utilisateur motDePasse(String motDePasse) {
        this.setMotDePasse(motDePasse);
        return this;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public TypeUtilisateur getTypeUtilisateur() {
        return this.typeUtilisateur;
    }

    public void setTypeUtilisateur(TypeUtilisateur typeUtilisateur) {
        this.typeUtilisateur = typeUtilisateur;
    }

    public Utilisateur typeUtilisateur(TypeUtilisateur typeUtilisateur) {
        this.setTypeUtilisateur(typeUtilisateur);
        return this;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Utilisateur roles(Set<Role> roles) {
        this.setRoles(roles);
        return this;
    }

    public Utilisateur addRoles(Role role) {
        this.roles.add(role);
        return this;
    }

    public Utilisateur removeRoles(Role role) {
        this.roles.remove(role);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilisateur)) return false;
        return id != null && id.equals(((Utilisateur) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Utilisateur{" +
            "id=" +
            getId() +
            ", nom='" +
            getNom() +
            '\'' +
            ", prenom='" +
            getPrenom() +
            '\'' +
            ", email='" +
            getEmail() +
            '\'' +
            ", motDePasse='" +
            getMotDePasse() +
            '\'' +
            '}'
        );
    }
}
