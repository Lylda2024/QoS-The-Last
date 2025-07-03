package com.orange.qos.web.rest;

import static com.orange.qos.domain.UtilisateurAsserts.*;
import static com.orange.qos.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.qos.IntegrationTest;
import com.orange.qos.domain.Role;
import com.orange.qos.domain.TypeUtilisateur;
import com.orange.qos.domain.Utilisateur;
import com.orange.qos.repository.UtilisateurRepository;
import com.orange.qos.service.UtilisateurService;
import com.orange.qos.service.dto.UtilisateurDTO;
import com.orange.qos.service.mapper.UtilisateurMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UtilisateurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UtilisateurResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_MOT_DE_PASSE = "AAAAAAAAAA";
    private static final String UPDATED_MOT_DE_PASSE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/utilisateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private UtilisateurRepository utilisateurRepositoryMock;

    @Autowired
    private UtilisateurMapper utilisateurMapper;

    @Mock
    private UtilisateurService utilisateurServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUtilisateurMockMvc;

    private Utilisateur utilisateur;

    private Utilisateur insertedUtilisateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilisateur createEntity() {
        return new Utilisateur().nom(DEFAULT_NOM).prenom(DEFAULT_PRENOM).email(DEFAULT_EMAIL).motDePasse(DEFAULT_MOT_DE_PASSE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilisateur createUpdatedEntity() {
        return new Utilisateur().nom(UPDATED_NOM).prenom(UPDATED_PRENOM).email(UPDATED_EMAIL).motDePasse(UPDATED_MOT_DE_PASSE);
    }

    @BeforeEach
    void initTest() {
        utilisateur = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUtilisateur != null) {
            utilisateurRepository.delete(insertedUtilisateur);
            insertedUtilisateur = null;
        }
    }

    @Test
    @Transactional
    void createUtilisateur() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);
        var returnedUtilisateurDTO = om.readValue(
            restUtilisateurMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UtilisateurDTO.class
        );

        // Validate the Utilisateur in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUtilisateur = utilisateurMapper.toEntity(returnedUtilisateurDTO);
        assertUtilisateurUpdatableFieldsEquals(returnedUtilisateur, getPersistedUtilisateur(returnedUtilisateur));

        insertedUtilisateur = returnedUtilisateur;
    }

    @Test
    @Transactional
    void createUtilisateurWithExistingId() throws Exception {
        // Create the Utilisateur with an existing ID
        utilisateur.setId(1L);
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisateur.setNom(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisateur.setPrenom(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisateur.setEmail(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMotDePasseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisateur.setMotDePasse(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUtilisateurs() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].motDePasse").value(hasItem(DEFAULT_MOT_DE_PASSE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUtilisateursWithEagerRelationshipsIsEnabled() throws Exception {
        when(utilisateurServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUtilisateurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(utilisateurServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUtilisateursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(utilisateurServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUtilisateurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(utilisateurRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUtilisateur() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get the utilisateur
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL_ID, utilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(utilisateur.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.motDePasse").value(DEFAULT_MOT_DE_PASSE));
    }

    @Test
    @Transactional
    void getUtilisateursByIdFiltering() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        Long id = utilisateur.getId();

        defaultUtilisateurFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUtilisateurFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUtilisateurFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUtilisateursByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where nom equals to
        defaultUtilisateurFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where nom in
        defaultUtilisateurFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where nom is not null
        defaultUtilisateurFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where nom contains
        defaultUtilisateurFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where nom does not contain
        defaultUtilisateurFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByPrenomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where prenom equals to
        defaultUtilisateurFiltering("prenom.equals=" + DEFAULT_PRENOM, "prenom.equals=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByPrenomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where prenom in
        defaultUtilisateurFiltering("prenom.in=" + DEFAULT_PRENOM + "," + UPDATED_PRENOM, "prenom.in=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByPrenomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where prenom is not null
        defaultUtilisateurFiltering("prenom.specified=true", "prenom.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByPrenomContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where prenom contains
        defaultUtilisateurFiltering("prenom.contains=" + DEFAULT_PRENOM, "prenom.contains=" + UPDATED_PRENOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByPrenomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where prenom does not contain
        defaultUtilisateurFiltering("prenom.doesNotContain=" + UPDATED_PRENOM, "prenom.doesNotContain=" + DEFAULT_PRENOM);
    }

    @Test
    @Transactional
    void getAllUtilisateursByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where email equals to
        defaultUtilisateurFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUtilisateursByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where email in
        defaultUtilisateurFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUtilisateursByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where email is not null
        defaultUtilisateurFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where email contains
        defaultUtilisateurFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllUtilisateursByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where email does not contain
        defaultUtilisateurFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllUtilisateursByMotDePasseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where motDePasse equals to
        defaultUtilisateurFiltering("motDePasse.equals=" + DEFAULT_MOT_DE_PASSE, "motDePasse.equals=" + UPDATED_MOT_DE_PASSE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByMotDePasseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where motDePasse in
        defaultUtilisateurFiltering(
            "motDePasse.in=" + DEFAULT_MOT_DE_PASSE + "," + UPDATED_MOT_DE_PASSE,
            "motDePasse.in=" + UPDATED_MOT_DE_PASSE
        );
    }

    @Test
    @Transactional
    void getAllUtilisateursByMotDePasseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where motDePasse is not null
        defaultUtilisateurFiltering("motDePasse.specified=true", "motDePasse.specified=false");
    }

    @Test
    @Transactional
    void getAllUtilisateursByMotDePasseContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where motDePasse contains
        defaultUtilisateurFiltering("motDePasse.contains=" + DEFAULT_MOT_DE_PASSE, "motDePasse.contains=" + UPDATED_MOT_DE_PASSE);
    }

    @Test
    @Transactional
    void getAllUtilisateursByMotDePasseNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList where motDePasse does not contain
        defaultUtilisateurFiltering(
            "motDePasse.doesNotContain=" + UPDATED_MOT_DE_PASSE,
            "motDePasse.doesNotContain=" + DEFAULT_MOT_DE_PASSE
        );
    }

    @Test
    @Transactional
    void getAllUtilisateursByTypeUtilisateurIsEqualToSomething() throws Exception {
        TypeUtilisateur typeUtilisateur;
        if (TestUtil.findAll(em, TypeUtilisateur.class).isEmpty()) {
            utilisateurRepository.saveAndFlush(utilisateur);
            typeUtilisateur = TypeUtilisateurResourceIT.createEntity();
        } else {
            typeUtilisateur = TestUtil.findAll(em, TypeUtilisateur.class).get(0);
        }
        em.persist(typeUtilisateur);
        em.flush();
        utilisateur.setTypeUtilisateur(typeUtilisateur);
        utilisateurRepository.saveAndFlush(utilisateur);
        Long typeUtilisateurId = typeUtilisateur.getId();
        // Get all the utilisateurList where typeUtilisateur equals to typeUtilisateurId
        defaultUtilisateurShouldBeFound("typeUtilisateurId.equals=" + typeUtilisateurId);

        // Get all the utilisateurList where typeUtilisateur equals to (typeUtilisateurId + 1)
        defaultUtilisateurShouldNotBeFound("typeUtilisateurId.equals=" + (typeUtilisateurId + 1));
    }

    @Test
    @Transactional
    void getAllUtilisateursByRolesIsEqualToSomething() throws Exception {
        Role roles;
        if (TestUtil.findAll(em, Role.class).isEmpty()) {
            utilisateurRepository.saveAndFlush(utilisateur);
            roles = RoleResourceIT.createEntity();
        } else {
            roles = TestUtil.findAll(em, Role.class).get(0);
        }
        em.persist(roles);
        em.flush();
        utilisateur.addRoles(roles);
        utilisateurRepository.saveAndFlush(utilisateur);
        Long rolesId = roles.getId();
        // Get all the utilisateurList where roles equals to rolesId
        defaultUtilisateurShouldBeFound("rolesId.equals=" + rolesId);

        // Get all the utilisateurList where roles equals to (rolesId + 1)
        defaultUtilisateurShouldNotBeFound("rolesId.equals=" + (rolesId + 1));
    }

    private void defaultUtilisateurFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUtilisateurShouldBeFound(shouldBeFound);
        defaultUtilisateurShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUtilisateurShouldBeFound(String filter) throws Exception {
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].motDePasse").value(hasItem(DEFAULT_MOT_DE_PASSE)));

        // Check, that the count call also returns 1
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUtilisateurShouldNotBeFound(String filter) throws Exception {
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUtilisateur() throws Exception {
        // Get the utilisateur
        restUtilisateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUtilisateur() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisateur
        Utilisateur updatedUtilisateur = utilisateurRepository.findById(utilisateur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUtilisateur are not directly saved in db
        em.detach(updatedUtilisateur);
        updatedUtilisateur.nom(UPDATED_NOM).prenom(UPDATED_PRENOM).email(UPDATED_EMAIL).motDePasse(UPDATED_MOT_DE_PASSE);
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(updatedUtilisateur);

        restUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilisateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utilisateurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUtilisateurToMatchAllProperties(updatedUtilisateur);
    }

    @Test
    @Transactional
    void putNonExistingUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilisateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUtilisateurWithPatch() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisateur using partial update
        Utilisateur partialUpdatedUtilisateur = new Utilisateur();
        partialUpdatedUtilisateur.setId(utilisateur.getId());

        partialUpdatedUtilisateur.motDePasse(UPDATED_MOT_DE_PASSE);

        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the Utilisateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUtilisateurUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUtilisateur, utilisateur),
            getPersistedUtilisateur(utilisateur)
        );
    }

    @Test
    @Transactional
    void fullUpdateUtilisateurWithPatch() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisateur using partial update
        Utilisateur partialUpdatedUtilisateur = new Utilisateur();
        partialUpdatedUtilisateur.setId(utilisateur.getId());

        partialUpdatedUtilisateur.nom(UPDATED_NOM).prenom(UPDATED_PRENOM).email(UPDATED_EMAIL).motDePasse(UPDATED_MOT_DE_PASSE);

        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the Utilisateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUtilisateurUpdatableFieldsEquals(partialUpdatedUtilisateur, getPersistedUtilisateur(partialUpdatedUtilisateur));
    }

    @Test
    @Transactional
    void patchNonExistingUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, utilisateurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(utilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(utilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUtilisateur() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the utilisateur
        restUtilisateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, utilisateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return utilisateurRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Utilisateur getPersistedUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.findById(utilisateur.getId()).orElseThrow();
    }

    protected void assertPersistedUtilisateurToMatchAllProperties(Utilisateur expectedUtilisateur) {
        assertUtilisateurAllPropertiesEquals(expectedUtilisateur, getPersistedUtilisateur(expectedUtilisateur));
    }

    protected void assertPersistedUtilisateurToMatchUpdatableProperties(Utilisateur expectedUtilisateur) {
        assertUtilisateurAllUpdatablePropertiesEquals(expectedUtilisateur, getPersistedUtilisateur(expectedUtilisateur));
    }
}
