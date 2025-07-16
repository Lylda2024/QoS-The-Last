package com.orange.qos.web.rest;

import static com.orange.qos.domain.DelaiInterventionAsserts.*;
import static com.orange.qos.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.qos.IntegrationTest;
import com.orange.qos.domain.Degradation;
import com.orange.qos.domain.DelaiIntervention;
import com.orange.qos.domain.Utilisateur;
import com.orange.qos.domain.enumeration.StatutDelai;
import com.orange.qos.repository.DelaiInterventionRepository;
import com.orange.qos.service.dto.DelaiInterventionDTO;
import com.orange.qos.service.mapper.DelaiInterventionMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DelaiInterventionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DelaiInterventionResourceIT {

    private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_LIMITE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_LIMITE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBB";

    private static final StatutDelai DEFAULT_STATUT = StatutDelai.EN_ATTENTE;
    private static final StatutDelai UPDATED_STATUT = StatutDelai.EN_COURS;

    private static final String ENTITY_API_URL = "/api/delai-interventions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DelaiInterventionRepository delaiInterventionRepository;

    @Autowired
    private DelaiInterventionMapper delaiInterventionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDelaiInterventionMockMvc;

    private DelaiIntervention delaiIntervention;

    private DelaiIntervention insertedDelaiIntervention;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DelaiIntervention createEntity() {
        return new DelaiIntervention()
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateLimite(DEFAULT_DATE_LIMITE)
            .commentaire(DEFAULT_COMMENTAIRE)
            .statut(DEFAULT_STATUT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DelaiIntervention createUpdatedEntity() {
        return new DelaiIntervention()
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateLimite(UPDATED_DATE_LIMITE)
            .commentaire(UPDATED_COMMENTAIRE)
            .statut(UPDATED_STATUT);
    }

    @BeforeEach
    void initTest() {
        delaiIntervention = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDelaiIntervention != null) {
            delaiInterventionRepository.delete(insertedDelaiIntervention);
            insertedDelaiIntervention = null;
        }
    }

    @Test
    @Transactional
    void createDelaiIntervention() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DelaiIntervention
        DelaiInterventionDTO delaiInterventionDTO = delaiInterventionMapper.toDto(delaiIntervention);
        var returnedDelaiInterventionDTO = om.readValue(
            restDelaiInterventionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delaiInterventionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DelaiInterventionDTO.class
        );

        // Validate the DelaiIntervention in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDelaiIntervention = delaiInterventionMapper.toEntity(returnedDelaiInterventionDTO);
        assertDelaiInterventionUpdatableFieldsEquals(returnedDelaiIntervention, getPersistedDelaiIntervention(returnedDelaiIntervention));

        insertedDelaiIntervention = returnedDelaiIntervention;
    }

    @Test
    @Transactional
    void createDelaiInterventionWithExistingId() throws Exception {
        // Create the DelaiIntervention with an existing ID
        delaiIntervention.setId(1L);
        DelaiInterventionDTO delaiInterventionDTO = delaiInterventionMapper.toDto(delaiIntervention);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDelaiInterventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delaiInterventionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DelaiIntervention in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateDebutIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        delaiIntervention.setDateDebut(null);

        // Create the DelaiIntervention, which fails.
        DelaiInterventionDTO delaiInterventionDTO = delaiInterventionMapper.toDto(delaiIntervention);

        restDelaiInterventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delaiInterventionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateLimiteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        delaiIntervention.setDateLimite(null);

        // Create the DelaiIntervention, which fails.
        DelaiInterventionDTO delaiInterventionDTO = delaiInterventionMapper.toDto(delaiIntervention);

        restDelaiInterventionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delaiInterventionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDelaiInterventions() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        // Get all the delaiInterventionList
        restDelaiInterventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(delaiIntervention.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateLimite").value(hasItem(DEFAULT_DATE_LIMITE.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));
    }

    @Test
    @Transactional
    void getDelaiIntervention() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        // Get the delaiIntervention
        restDelaiInterventionMockMvc
            .perform(get(ENTITY_API_URL_ID, delaiIntervention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(delaiIntervention.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateLimite").value(DEFAULT_DATE_LIMITE.toString()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()));
    }

    @Test
    @Transactional
    void getDelaiInterventionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        Long id = delaiIntervention.getId();

        defaultDelaiInterventionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDelaiInterventionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDelaiInterventionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDelaiInterventionsByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        // Get all the delaiInterventionList where dateDebut equals to
        defaultDelaiInterventionFiltering("dateDebut.equals=" + DEFAULT_DATE_DEBUT, "dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    void getAllDelaiInterventionsByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        // Get all the delaiInterventionList where dateDebut in
        defaultDelaiInterventionFiltering(
            "dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT,
            "dateDebut.in=" + UPDATED_DATE_DEBUT
        );
    }

    @Test
    @Transactional
    void getAllDelaiInterventionsByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        // Get all the delaiInterventionList where dateDebut is not null
        defaultDelaiInterventionFiltering("dateDebut.specified=true", "dateDebut.specified=false");
    }

    @Test
    @Transactional
    void getAllDelaiInterventionsByDateLimiteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        // Get all the delaiInterventionList where dateLimite equals to
        defaultDelaiInterventionFiltering("dateLimite.equals=" + DEFAULT_DATE_LIMITE, "dateLimite.equals=" + UPDATED_DATE_LIMITE);
    }

    @Test
    @Transactional
    void getAllDelaiInterventionsByDateLimiteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        // Get all the delaiInterventionList where dateLimite in
        defaultDelaiInterventionFiltering(
            "dateLimite.in=" + DEFAULT_DATE_LIMITE + "," + UPDATED_DATE_LIMITE,
            "dateLimite.in=" + UPDATED_DATE_LIMITE
        );
    }

    @Test
    @Transactional
    void getAllDelaiInterventionsByDateLimiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        // Get all the delaiInterventionList where dateLimite is not null
        defaultDelaiInterventionFiltering("dateLimite.specified=true", "dateLimite.specified=false");
    }

    @Test
    @Transactional
    void getAllDelaiInterventionsByCommentaireIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        // Get all the delaiInterventionList where commentaire equals to
        defaultDelaiInterventionFiltering("commentaire.equals=" + DEFAULT_COMMENTAIRE, "commentaire.equals=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllDelaiInterventionsByCommentaireIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        // Get all the delaiInterventionList where commentaire in
        defaultDelaiInterventionFiltering(
            "commentaire.in=" + DEFAULT_COMMENTAIRE + "," + UPDATED_COMMENTAIRE,
            "commentaire.in=" + UPDATED_COMMENTAIRE
        );
    }

    @Test
    @Transactional
    void getAllDelaiInterventionsByCommentaireIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        // Get all the delaiInterventionList where commentaire is not null
        defaultDelaiInterventionFiltering("commentaire.specified=true", "commentaire.specified=false");
    }

    @Test
    @Transactional
    void getAllDelaiInterventionsByCommentaireContainsSomething() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        // Get all the delaiInterventionList where commentaire contains
        defaultDelaiInterventionFiltering("commentaire.contains=" + DEFAULT_COMMENTAIRE, "commentaire.contains=" + UPDATED_COMMENTAIRE);
    }

    @Test
    @Transactional
    void getAllDelaiInterventionsByCommentaireNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        // Get all the delaiInterventionList where commentaire does not contain
        defaultDelaiInterventionFiltering(
            "commentaire.doesNotContain=" + UPDATED_COMMENTAIRE,
            "commentaire.doesNotContain=" + DEFAULT_COMMENTAIRE
        );
    }

    @Test
    @Transactional
    void getAllDelaiInterventionsByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        // Get all the delaiInterventionList where statut equals to
        defaultDelaiInterventionFiltering("statut.equals=" + DEFAULT_STATUT, "statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllDelaiInterventionsByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        // Get all the delaiInterventionList where statut in
        defaultDelaiInterventionFiltering("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT, "statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllDelaiInterventionsByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        // Get all the delaiInterventionList where statut is not null
        defaultDelaiInterventionFiltering("statut.specified=true", "statut.specified=false");
    }

    @Test
    @Transactional
    void getAllDelaiInterventionsByDegradationIsEqualToSomething() throws Exception {
        Degradation degradation;
        if (TestUtil.findAll(em, Degradation.class).isEmpty()) {
            delaiInterventionRepository.saveAndFlush(delaiIntervention);
            degradation = DegradationResourceIT.createEntity();
        } else {
            degradation = TestUtil.findAll(em, Degradation.class).get(0);
        }
        em.persist(degradation);
        em.flush();
        delaiIntervention.setDegradation(degradation);
        delaiInterventionRepository.saveAndFlush(delaiIntervention);
        Long degradationId = degradation.getId();
        // Get all the delaiInterventionList where degradation equals to degradationId
        defaultDelaiInterventionShouldBeFound("degradationId.equals=" + degradationId);

        // Get all the delaiInterventionList where degradation equals to (degradationId + 1)
        defaultDelaiInterventionShouldNotBeFound("degradationId.equals=" + (degradationId + 1));
    }

    @Test
    @Transactional
    void getAllDelaiInterventionsByUtilisateurIsEqualToSomething() throws Exception {
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            delaiInterventionRepository.saveAndFlush(delaiIntervention);
            utilisateur = UtilisateurResourceIT.createEntity();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(utilisateur);
        em.flush();
        delaiIntervention.setUtilisateur(utilisateur);
        delaiInterventionRepository.saveAndFlush(delaiIntervention);
        Long utilisateurId = utilisateur.getId();
        // Get all the delaiInterventionList where utilisateur equals to utilisateurId
        defaultDelaiInterventionShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the delaiInterventionList where utilisateur equals to (utilisateurId + 1)
        defaultDelaiInterventionShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    private void defaultDelaiInterventionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDelaiInterventionShouldBeFound(shouldBeFound);
        defaultDelaiInterventionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDelaiInterventionShouldBeFound(String filter) throws Exception {
        restDelaiInterventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(delaiIntervention.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateLimite").value(hasItem(DEFAULT_DATE_LIMITE.toString())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())));

        // Check, that the count call also returns 1
        restDelaiInterventionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDelaiInterventionShouldNotBeFound(String filter) throws Exception {
        restDelaiInterventionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDelaiInterventionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDelaiIntervention() throws Exception {
        // Get the delaiIntervention
        restDelaiInterventionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDelaiIntervention() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the delaiIntervention
        DelaiIntervention updatedDelaiIntervention = delaiInterventionRepository.findById(delaiIntervention.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDelaiIntervention are not directly saved in db
        em.detach(updatedDelaiIntervention);
        updatedDelaiIntervention
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateLimite(UPDATED_DATE_LIMITE)
            .commentaire(UPDATED_COMMENTAIRE)
            .statut(UPDATED_STATUT);
        DelaiInterventionDTO delaiInterventionDTO = delaiInterventionMapper.toDto(updatedDelaiIntervention);

        restDelaiInterventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, delaiInterventionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(delaiInterventionDTO))
            )
            .andExpect(status().isOk());

        // Validate the DelaiIntervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDelaiInterventionToMatchAllProperties(updatedDelaiIntervention);
    }

    @Test
    @Transactional
    void putNonExistingDelaiIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        delaiIntervention.setId(longCount.incrementAndGet());

        // Create the DelaiIntervention
        DelaiInterventionDTO delaiInterventionDTO = delaiInterventionMapper.toDto(delaiIntervention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDelaiInterventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, delaiInterventionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(delaiInterventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DelaiIntervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDelaiIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        delaiIntervention.setId(longCount.incrementAndGet());

        // Create the DelaiIntervention
        DelaiInterventionDTO delaiInterventionDTO = delaiInterventionMapper.toDto(delaiIntervention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDelaiInterventionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(delaiInterventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DelaiIntervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDelaiIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        delaiIntervention.setId(longCount.incrementAndGet());

        // Create the DelaiIntervention
        DelaiInterventionDTO delaiInterventionDTO = delaiInterventionMapper.toDto(delaiIntervention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDelaiInterventionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(delaiInterventionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DelaiIntervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDelaiInterventionWithPatch() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the delaiIntervention using partial update
        DelaiIntervention partialUpdatedDelaiIntervention = new DelaiIntervention();
        partialUpdatedDelaiIntervention.setId(delaiIntervention.getId());

        partialUpdatedDelaiIntervention.dateLimite(UPDATED_DATE_LIMITE).commentaire(UPDATED_COMMENTAIRE).statut(UPDATED_STATUT);

        restDelaiInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDelaiIntervention.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDelaiIntervention))
            )
            .andExpect(status().isOk());

        // Validate the DelaiIntervention in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDelaiInterventionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDelaiIntervention, delaiIntervention),
            getPersistedDelaiIntervention(delaiIntervention)
        );
    }

    @Test
    @Transactional
    void fullUpdateDelaiInterventionWithPatch() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the delaiIntervention using partial update
        DelaiIntervention partialUpdatedDelaiIntervention = new DelaiIntervention();
        partialUpdatedDelaiIntervention.setId(delaiIntervention.getId());

        partialUpdatedDelaiIntervention
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateLimite(UPDATED_DATE_LIMITE)
            .commentaire(UPDATED_COMMENTAIRE)
            .statut(UPDATED_STATUT);

        restDelaiInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDelaiIntervention.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDelaiIntervention))
            )
            .andExpect(status().isOk());

        // Validate the DelaiIntervention in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDelaiInterventionUpdatableFieldsEquals(
            partialUpdatedDelaiIntervention,
            getPersistedDelaiIntervention(partialUpdatedDelaiIntervention)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDelaiIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        delaiIntervention.setId(longCount.incrementAndGet());

        // Create the DelaiIntervention
        DelaiInterventionDTO delaiInterventionDTO = delaiInterventionMapper.toDto(delaiIntervention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDelaiInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, delaiInterventionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(delaiInterventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DelaiIntervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDelaiIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        delaiIntervention.setId(longCount.incrementAndGet());

        // Create the DelaiIntervention
        DelaiInterventionDTO delaiInterventionDTO = delaiInterventionMapper.toDto(delaiIntervention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDelaiInterventionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(delaiInterventionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DelaiIntervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDelaiIntervention() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        delaiIntervention.setId(longCount.incrementAndGet());

        // Create the DelaiIntervention
        DelaiInterventionDTO delaiInterventionDTO = delaiInterventionMapper.toDto(delaiIntervention);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDelaiInterventionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(delaiInterventionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DelaiIntervention in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDelaiIntervention() throws Exception {
        // Initialize the database
        insertedDelaiIntervention = delaiInterventionRepository.saveAndFlush(delaiIntervention);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the delaiIntervention
        restDelaiInterventionMockMvc
            .perform(delete(ENTITY_API_URL_ID, delaiIntervention.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return delaiInterventionRepository.count();
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

    protected DelaiIntervention getPersistedDelaiIntervention(DelaiIntervention delaiIntervention) {
        return delaiInterventionRepository.findById(delaiIntervention.getId()).orElseThrow();
    }

    protected void assertPersistedDelaiInterventionToMatchAllProperties(DelaiIntervention expectedDelaiIntervention) {
        assertDelaiInterventionAllPropertiesEquals(expectedDelaiIntervention, getPersistedDelaiIntervention(expectedDelaiIntervention));
    }

    protected void assertPersistedDelaiInterventionToMatchUpdatableProperties(DelaiIntervention expectedDelaiIntervention) {
        assertDelaiInterventionAllUpdatablePropertiesEquals(
            expectedDelaiIntervention,
            getPersistedDelaiIntervention(expectedDelaiIntervention)
        );
    }
}
