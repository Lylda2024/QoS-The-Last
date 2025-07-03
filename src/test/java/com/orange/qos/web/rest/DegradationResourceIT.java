package com.orange.qos.web.rest;

import static com.orange.qos.domain.DegradationAsserts.*;
import static com.orange.qos.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.qos.IntegrationTest;
import com.orange.qos.domain.Degradation;
import com.orange.qos.domain.Site;
import com.orange.qos.domain.Utilisateur;
import com.orange.qos.repository.DegradationRepository;
import com.orange.qos.service.dto.DegradationDTO;
import com.orange.qos.service.mapper.DegradationMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link DegradationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DegradationResourceIT {

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_LOCALITE = "AAAAAAAAAA";
    private static final String UPDATED_LOCALITE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_TEMOIN = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_TEMOIN = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_ANOMALIE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_ANOMALIE = "BBBBBBBBBB";

    private static final String DEFAULT_PRIORITE = "AAAAAAAAAA";
    private static final String UPDATED_PRIORITE = "BBBBBBBBBB";

    private static final String DEFAULT_PROBLEM = "AAAAAAAAAA";
    private static final String UPDATED_PROBLEM = "BBBBBBBBBB";

    private static final String DEFAULT_PORTEUR = "AAAAAAAAAA";
    private static final String UPDATED_PORTEUR = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIONS_EFFECTUEES = "AAAAAAAAAA";
    private static final String UPDATED_ACTIONS_EFFECTUEES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/degradations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DegradationRepository degradationRepository;

    @Autowired
    private DegradationMapper degradationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDegradationMockMvc;

    private Degradation degradation;

    private Degradation insertedDegradation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Degradation createEntity() {
        return new Degradation()
            .numero(DEFAULT_NUMERO)
            .localite(DEFAULT_LOCALITE)
            .contactTemoin(DEFAULT_CONTACT_TEMOIN)
            .typeAnomalie(DEFAULT_TYPE_ANOMALIE)
            .priorite(DEFAULT_PRIORITE)
            .problem(DEFAULT_PROBLEM)
            .porteur(DEFAULT_PORTEUR)
            .actionsEffectuees(DEFAULT_ACTIONS_EFFECTUEES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Degradation createUpdatedEntity() {
        return new Degradation()
            .numero(UPDATED_NUMERO)
            .localite(UPDATED_LOCALITE)
            .contactTemoin(UPDATED_CONTACT_TEMOIN)
            .typeAnomalie(UPDATED_TYPE_ANOMALIE)
            .priorite(UPDATED_PRIORITE)
            .problem(UPDATED_PROBLEM)
            .porteur(UPDATED_PORTEUR)
            .actionsEffectuees(UPDATED_ACTIONS_EFFECTUEES);
    }

    @BeforeEach
    void initTest() {
        degradation = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDegradation != null) {
            degradationRepository.delete(insertedDegradation);
            insertedDegradation = null;
        }
    }

    @Test
    @Transactional
    void createDegradation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Degradation
        DegradationDTO degradationDTO = degradationMapper.toDto(degradation);
        var returnedDegradationDTO = om.readValue(
            restDegradationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(degradationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DegradationDTO.class
        );

        // Validate the Degradation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDegradation = degradationMapper.toEntity(returnedDegradationDTO);
        assertDegradationUpdatableFieldsEquals(returnedDegradation, getPersistedDegradation(returnedDegradation));

        insertedDegradation = returnedDegradation;
    }

    @Test
    @Transactional
    void createDegradationWithExistingId() throws Exception {
        // Create the Degradation with an existing ID
        degradation.setId(1L);
        DegradationDTO degradationDTO = degradationMapper.toDto(degradation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDegradationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(degradationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Degradation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        degradation.setNumero(null);

        // Create the Degradation, which fails.
        DegradationDTO degradationDTO = degradationMapper.toDto(degradation);

        restDegradationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(degradationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocaliteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        degradation.setLocalite(null);

        // Create the Degradation, which fails.
        DegradationDTO degradationDTO = degradationMapper.toDto(degradation);

        restDegradationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(degradationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContactTemoinIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        degradation.setContactTemoin(null);

        // Create the Degradation, which fails.
        DegradationDTO degradationDTO = degradationMapper.toDto(degradation);

        restDegradationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(degradationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeAnomalieIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        degradation.setTypeAnomalie(null);

        // Create the Degradation, which fails.
        DegradationDTO degradationDTO = degradationMapper.toDto(degradation);

        restDegradationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(degradationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrioriteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        degradation.setPriorite(null);

        // Create the Degradation, which fails.
        DegradationDTO degradationDTO = degradationMapper.toDto(degradation);

        restDegradationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(degradationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProblemIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        degradation.setProblem(null);

        // Create the Degradation, which fails.
        DegradationDTO degradationDTO = degradationMapper.toDto(degradation);

        restDegradationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(degradationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPorteurIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        degradation.setPorteur(null);

        // Create the Degradation, which fails.
        DegradationDTO degradationDTO = degradationMapper.toDto(degradation);

        restDegradationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(degradationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDegradations() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList
        restDegradationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(degradation.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].localite").value(hasItem(DEFAULT_LOCALITE)))
            .andExpect(jsonPath("$.[*].contactTemoin").value(hasItem(DEFAULT_CONTACT_TEMOIN)))
            .andExpect(jsonPath("$.[*].typeAnomalie").value(hasItem(DEFAULT_TYPE_ANOMALIE)))
            .andExpect(jsonPath("$.[*].priorite").value(hasItem(DEFAULT_PRIORITE)))
            .andExpect(jsonPath("$.[*].problem").value(hasItem(DEFAULT_PROBLEM)))
            .andExpect(jsonPath("$.[*].porteur").value(hasItem(DEFAULT_PORTEUR)))
            .andExpect(jsonPath("$.[*].actionsEffectuees").value(hasItem(DEFAULT_ACTIONS_EFFECTUEES)));
    }

    @Test
    @Transactional
    void getDegradation() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get the degradation
        restDegradationMockMvc
            .perform(get(ENTITY_API_URL_ID, degradation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(degradation.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.localite").value(DEFAULT_LOCALITE))
            .andExpect(jsonPath("$.contactTemoin").value(DEFAULT_CONTACT_TEMOIN))
            .andExpect(jsonPath("$.typeAnomalie").value(DEFAULT_TYPE_ANOMALIE))
            .andExpect(jsonPath("$.priorite").value(DEFAULT_PRIORITE))
            .andExpect(jsonPath("$.problem").value(DEFAULT_PROBLEM))
            .andExpect(jsonPath("$.porteur").value(DEFAULT_PORTEUR))
            .andExpect(jsonPath("$.actionsEffectuees").value(DEFAULT_ACTIONS_EFFECTUEES));
    }

    @Test
    @Transactional
    void getDegradationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        Long id = degradation.getId();

        defaultDegradationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDegradationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDegradationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDegradationsByNumeroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where numero equals to
        defaultDegradationFiltering("numero.equals=" + DEFAULT_NUMERO, "numero.equals=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllDegradationsByNumeroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where numero in
        defaultDegradationFiltering("numero.in=" + DEFAULT_NUMERO + "," + UPDATED_NUMERO, "numero.in=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllDegradationsByNumeroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where numero is not null
        defaultDegradationFiltering("numero.specified=true", "numero.specified=false");
    }

    @Test
    @Transactional
    void getAllDegradationsByNumeroContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where numero contains
        defaultDegradationFiltering("numero.contains=" + DEFAULT_NUMERO, "numero.contains=" + UPDATED_NUMERO);
    }

    @Test
    @Transactional
    void getAllDegradationsByNumeroNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where numero does not contain
        defaultDegradationFiltering("numero.doesNotContain=" + UPDATED_NUMERO, "numero.doesNotContain=" + DEFAULT_NUMERO);
    }

    @Test
    @Transactional
    void getAllDegradationsByLocaliteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where localite equals to
        defaultDegradationFiltering("localite.equals=" + DEFAULT_LOCALITE, "localite.equals=" + UPDATED_LOCALITE);
    }

    @Test
    @Transactional
    void getAllDegradationsByLocaliteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where localite in
        defaultDegradationFiltering("localite.in=" + DEFAULT_LOCALITE + "," + UPDATED_LOCALITE, "localite.in=" + UPDATED_LOCALITE);
    }

    @Test
    @Transactional
    void getAllDegradationsByLocaliteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where localite is not null
        defaultDegradationFiltering("localite.specified=true", "localite.specified=false");
    }

    @Test
    @Transactional
    void getAllDegradationsByLocaliteContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where localite contains
        defaultDegradationFiltering("localite.contains=" + DEFAULT_LOCALITE, "localite.contains=" + UPDATED_LOCALITE);
    }

    @Test
    @Transactional
    void getAllDegradationsByLocaliteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where localite does not contain
        defaultDegradationFiltering("localite.doesNotContain=" + UPDATED_LOCALITE, "localite.doesNotContain=" + DEFAULT_LOCALITE);
    }

    @Test
    @Transactional
    void getAllDegradationsByContactTemoinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where contactTemoin equals to
        defaultDegradationFiltering("contactTemoin.equals=" + DEFAULT_CONTACT_TEMOIN, "contactTemoin.equals=" + UPDATED_CONTACT_TEMOIN);
    }

    @Test
    @Transactional
    void getAllDegradationsByContactTemoinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where contactTemoin in
        defaultDegradationFiltering(
            "contactTemoin.in=" + DEFAULT_CONTACT_TEMOIN + "," + UPDATED_CONTACT_TEMOIN,
            "contactTemoin.in=" + UPDATED_CONTACT_TEMOIN
        );
    }

    @Test
    @Transactional
    void getAllDegradationsByContactTemoinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where contactTemoin is not null
        defaultDegradationFiltering("contactTemoin.specified=true", "contactTemoin.specified=false");
    }

    @Test
    @Transactional
    void getAllDegradationsByContactTemoinContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where contactTemoin contains
        defaultDegradationFiltering("contactTemoin.contains=" + DEFAULT_CONTACT_TEMOIN, "contactTemoin.contains=" + UPDATED_CONTACT_TEMOIN);
    }

    @Test
    @Transactional
    void getAllDegradationsByContactTemoinNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where contactTemoin does not contain
        defaultDegradationFiltering(
            "contactTemoin.doesNotContain=" + UPDATED_CONTACT_TEMOIN,
            "contactTemoin.doesNotContain=" + DEFAULT_CONTACT_TEMOIN
        );
    }

    @Test
    @Transactional
    void getAllDegradationsByTypeAnomalieIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where typeAnomalie equals to
        defaultDegradationFiltering("typeAnomalie.equals=" + DEFAULT_TYPE_ANOMALIE, "typeAnomalie.equals=" + UPDATED_TYPE_ANOMALIE);
    }

    @Test
    @Transactional
    void getAllDegradationsByTypeAnomalieIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where typeAnomalie in
        defaultDegradationFiltering(
            "typeAnomalie.in=" + DEFAULT_TYPE_ANOMALIE + "," + UPDATED_TYPE_ANOMALIE,
            "typeAnomalie.in=" + UPDATED_TYPE_ANOMALIE
        );
    }

    @Test
    @Transactional
    void getAllDegradationsByTypeAnomalieIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where typeAnomalie is not null
        defaultDegradationFiltering("typeAnomalie.specified=true", "typeAnomalie.specified=false");
    }

    @Test
    @Transactional
    void getAllDegradationsByTypeAnomalieContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where typeAnomalie contains
        defaultDegradationFiltering("typeAnomalie.contains=" + DEFAULT_TYPE_ANOMALIE, "typeAnomalie.contains=" + UPDATED_TYPE_ANOMALIE);
    }

    @Test
    @Transactional
    void getAllDegradationsByTypeAnomalieNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where typeAnomalie does not contain
        defaultDegradationFiltering(
            "typeAnomalie.doesNotContain=" + UPDATED_TYPE_ANOMALIE,
            "typeAnomalie.doesNotContain=" + DEFAULT_TYPE_ANOMALIE
        );
    }

    @Test
    @Transactional
    void getAllDegradationsByPrioriteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where priorite equals to
        defaultDegradationFiltering("priorite.equals=" + DEFAULT_PRIORITE, "priorite.equals=" + UPDATED_PRIORITE);
    }

    @Test
    @Transactional
    void getAllDegradationsByPrioriteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where priorite in
        defaultDegradationFiltering("priorite.in=" + DEFAULT_PRIORITE + "," + UPDATED_PRIORITE, "priorite.in=" + UPDATED_PRIORITE);
    }

    @Test
    @Transactional
    void getAllDegradationsByPrioriteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where priorite is not null
        defaultDegradationFiltering("priorite.specified=true", "priorite.specified=false");
    }

    @Test
    @Transactional
    void getAllDegradationsByPrioriteContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where priorite contains
        defaultDegradationFiltering("priorite.contains=" + DEFAULT_PRIORITE, "priorite.contains=" + UPDATED_PRIORITE);
    }

    @Test
    @Transactional
    void getAllDegradationsByPrioriteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where priorite does not contain
        defaultDegradationFiltering("priorite.doesNotContain=" + UPDATED_PRIORITE, "priorite.doesNotContain=" + DEFAULT_PRIORITE);
    }

    @Test
    @Transactional
    void getAllDegradationsByProblemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where problem equals to
        defaultDegradationFiltering("problem.equals=" + DEFAULT_PROBLEM, "problem.equals=" + UPDATED_PROBLEM);
    }

    @Test
    @Transactional
    void getAllDegradationsByProblemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where problem in
        defaultDegradationFiltering("problem.in=" + DEFAULT_PROBLEM + "," + UPDATED_PROBLEM, "problem.in=" + UPDATED_PROBLEM);
    }

    @Test
    @Transactional
    void getAllDegradationsByProblemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where problem is not null
        defaultDegradationFiltering("problem.specified=true", "problem.specified=false");
    }

    @Test
    @Transactional
    void getAllDegradationsByProblemContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where problem contains
        defaultDegradationFiltering("problem.contains=" + DEFAULT_PROBLEM, "problem.contains=" + UPDATED_PROBLEM);
    }

    @Test
    @Transactional
    void getAllDegradationsByProblemNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where problem does not contain
        defaultDegradationFiltering("problem.doesNotContain=" + UPDATED_PROBLEM, "problem.doesNotContain=" + DEFAULT_PROBLEM);
    }

    @Test
    @Transactional
    void getAllDegradationsByPorteurIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where porteur equals to
        defaultDegradationFiltering("porteur.equals=" + DEFAULT_PORTEUR, "porteur.equals=" + UPDATED_PORTEUR);
    }

    @Test
    @Transactional
    void getAllDegradationsByPorteurIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where porteur in
        defaultDegradationFiltering("porteur.in=" + DEFAULT_PORTEUR + "," + UPDATED_PORTEUR, "porteur.in=" + UPDATED_PORTEUR);
    }

    @Test
    @Transactional
    void getAllDegradationsByPorteurIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where porteur is not null
        defaultDegradationFiltering("porteur.specified=true", "porteur.specified=false");
    }

    @Test
    @Transactional
    void getAllDegradationsByPorteurContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where porteur contains
        defaultDegradationFiltering("porteur.contains=" + DEFAULT_PORTEUR, "porteur.contains=" + UPDATED_PORTEUR);
    }

    @Test
    @Transactional
    void getAllDegradationsByPorteurNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where porteur does not contain
        defaultDegradationFiltering("porteur.doesNotContain=" + UPDATED_PORTEUR, "porteur.doesNotContain=" + DEFAULT_PORTEUR);
    }

    @Test
    @Transactional
    void getAllDegradationsByActionsEffectueesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where actionsEffectuees equals to
        defaultDegradationFiltering(
            "actionsEffectuees.equals=" + DEFAULT_ACTIONS_EFFECTUEES,
            "actionsEffectuees.equals=" + UPDATED_ACTIONS_EFFECTUEES
        );
    }

    @Test
    @Transactional
    void getAllDegradationsByActionsEffectueesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where actionsEffectuees in
        defaultDegradationFiltering(
            "actionsEffectuees.in=" + DEFAULT_ACTIONS_EFFECTUEES + "," + UPDATED_ACTIONS_EFFECTUEES,
            "actionsEffectuees.in=" + UPDATED_ACTIONS_EFFECTUEES
        );
    }

    @Test
    @Transactional
    void getAllDegradationsByActionsEffectueesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where actionsEffectuees is not null
        defaultDegradationFiltering("actionsEffectuees.specified=true", "actionsEffectuees.specified=false");
    }

    @Test
    @Transactional
    void getAllDegradationsByActionsEffectueesContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where actionsEffectuees contains
        defaultDegradationFiltering(
            "actionsEffectuees.contains=" + DEFAULT_ACTIONS_EFFECTUEES,
            "actionsEffectuees.contains=" + UPDATED_ACTIONS_EFFECTUEES
        );
    }

    @Test
    @Transactional
    void getAllDegradationsByActionsEffectueesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where actionsEffectuees does not contain
        defaultDegradationFiltering(
            "actionsEffectuees.doesNotContain=" + UPDATED_ACTIONS_EFFECTUEES,
            "actionsEffectuees.doesNotContain=" + DEFAULT_ACTIONS_EFFECTUEES
        );
    }

    @Test
    @Transactional
    void getAllDegradationsByUtilisateurIsEqualToSomething() throws Exception {
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            degradationRepository.saveAndFlush(degradation);
            utilisateur = UtilisateurResourceIT.createEntity();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(utilisateur);
        em.flush();
        degradation.setUtilisateur(utilisateur);
        degradationRepository.saveAndFlush(degradation);
        Long utilisateurId = utilisateur.getId();
        // Get all the degradationList where utilisateur equals to utilisateurId
        defaultDegradationShouldBeFound("utilisateurId.equals=" + utilisateurId);

        // Get all the degradationList where utilisateur equals to (utilisateurId + 1)
        defaultDegradationShouldNotBeFound("utilisateurId.equals=" + (utilisateurId + 1));
    }

    @Test
    @Transactional
    void getAllDegradationsBySiteIsEqualToSomething() throws Exception {
        Site site;
        if (TestUtil.findAll(em, Site.class).isEmpty()) {
            degradationRepository.saveAndFlush(degradation);
            site = SiteResourceIT.createEntity();
        } else {
            site = TestUtil.findAll(em, Site.class).get(0);
        }
        em.persist(site);
        em.flush();
        degradation.setSite(site);
        degradationRepository.saveAndFlush(degradation);
        Long siteId = site.getId();
        // Get all the degradationList where site equals to siteId
        defaultDegradationShouldBeFound("siteId.equals=" + siteId);

        // Get all the degradationList where site equals to (siteId + 1)
        defaultDegradationShouldNotBeFound("siteId.equals=" + (siteId + 1));
    }

    private void defaultDegradationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDegradationShouldBeFound(shouldBeFound);
        defaultDegradationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDegradationShouldBeFound(String filter) throws Exception {
        restDegradationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(degradation.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].localite").value(hasItem(DEFAULT_LOCALITE)))
            .andExpect(jsonPath("$.[*].contactTemoin").value(hasItem(DEFAULT_CONTACT_TEMOIN)))
            .andExpect(jsonPath("$.[*].typeAnomalie").value(hasItem(DEFAULT_TYPE_ANOMALIE)))
            .andExpect(jsonPath("$.[*].priorite").value(hasItem(DEFAULT_PRIORITE)))
            .andExpect(jsonPath("$.[*].problem").value(hasItem(DEFAULT_PROBLEM)))
            .andExpect(jsonPath("$.[*].porteur").value(hasItem(DEFAULT_PORTEUR)))
            .andExpect(jsonPath("$.[*].actionsEffectuees").value(hasItem(DEFAULT_ACTIONS_EFFECTUEES)));

        // Check, that the count call also returns 1
        restDegradationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDegradationShouldNotBeFound(String filter) throws Exception {
        restDegradationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDegradationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDegradation() throws Exception {
        // Get the degradation
        restDegradationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDegradation() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the degradation
        Degradation updatedDegradation = degradationRepository.findById(degradation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDegradation are not directly saved in db
        em.detach(updatedDegradation);
        updatedDegradation
            .numero(UPDATED_NUMERO)
            .localite(UPDATED_LOCALITE)
            .contactTemoin(UPDATED_CONTACT_TEMOIN)
            .typeAnomalie(UPDATED_TYPE_ANOMALIE)
            .priorite(UPDATED_PRIORITE)
            .problem(UPDATED_PROBLEM)
            .porteur(UPDATED_PORTEUR)
            .actionsEffectuees(UPDATED_ACTIONS_EFFECTUEES);
        DegradationDTO degradationDTO = degradationMapper.toDto(updatedDegradation);

        restDegradationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, degradationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(degradationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Degradation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDegradationToMatchAllProperties(updatedDegradation);
    }

    @Test
    @Transactional
    void putNonExistingDegradation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        degradation.setId(longCount.incrementAndGet());

        // Create the Degradation
        DegradationDTO degradationDTO = degradationMapper.toDto(degradation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDegradationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, degradationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(degradationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Degradation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDegradation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        degradation.setId(longCount.incrementAndGet());

        // Create the Degradation
        DegradationDTO degradationDTO = degradationMapper.toDto(degradation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDegradationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(degradationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Degradation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDegradation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        degradation.setId(longCount.incrementAndGet());

        // Create the Degradation
        DegradationDTO degradationDTO = degradationMapper.toDto(degradation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDegradationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(degradationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Degradation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDegradationWithPatch() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the degradation using partial update
        Degradation partialUpdatedDegradation = new Degradation();
        partialUpdatedDegradation.setId(degradation.getId());

        partialUpdatedDegradation
            .numero(UPDATED_NUMERO)
            .localite(UPDATED_LOCALITE)
            .contactTemoin(UPDATED_CONTACT_TEMOIN)
            .priorite(UPDATED_PRIORITE)
            .problem(UPDATED_PROBLEM)
            .porteur(UPDATED_PORTEUR)
            .actionsEffectuees(UPDATED_ACTIONS_EFFECTUEES);

        restDegradationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDegradation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDegradation))
            )
            .andExpect(status().isOk());

        // Validate the Degradation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDegradationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDegradation, degradation),
            getPersistedDegradation(degradation)
        );
    }

    @Test
    @Transactional
    void fullUpdateDegradationWithPatch() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the degradation using partial update
        Degradation partialUpdatedDegradation = new Degradation();
        partialUpdatedDegradation.setId(degradation.getId());

        partialUpdatedDegradation
            .numero(UPDATED_NUMERO)
            .localite(UPDATED_LOCALITE)
            .contactTemoin(UPDATED_CONTACT_TEMOIN)
            .typeAnomalie(UPDATED_TYPE_ANOMALIE)
            .priorite(UPDATED_PRIORITE)
            .problem(UPDATED_PROBLEM)
            .porteur(UPDATED_PORTEUR)
            .actionsEffectuees(UPDATED_ACTIONS_EFFECTUEES);

        restDegradationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDegradation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDegradation))
            )
            .andExpect(status().isOk());

        // Validate the Degradation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDegradationUpdatableFieldsEquals(partialUpdatedDegradation, getPersistedDegradation(partialUpdatedDegradation));
    }

    @Test
    @Transactional
    void patchNonExistingDegradation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        degradation.setId(longCount.incrementAndGet());

        // Create the Degradation
        DegradationDTO degradationDTO = degradationMapper.toDto(degradation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDegradationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, degradationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(degradationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Degradation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDegradation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        degradation.setId(longCount.incrementAndGet());

        // Create the Degradation
        DegradationDTO degradationDTO = degradationMapper.toDto(degradation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDegradationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(degradationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Degradation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDegradation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        degradation.setId(longCount.incrementAndGet());

        // Create the Degradation
        DegradationDTO degradationDTO = degradationMapper.toDto(degradation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDegradationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(degradationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Degradation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDegradation() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the degradation
        restDegradationMockMvc
            .perform(delete(ENTITY_API_URL_ID, degradation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return degradationRepository.count();
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

    protected Degradation getPersistedDegradation(Degradation degradation) {
        return degradationRepository.findById(degradation.getId()).orElseThrow();
    }

    protected void assertPersistedDegradationToMatchAllProperties(Degradation expectedDegradation) {
        assertDegradationAllPropertiesEquals(expectedDegradation, getPersistedDegradation(expectedDegradation));
    }

    protected void assertPersistedDegradationToMatchUpdatableProperties(Degradation expectedDegradation) {
        assertDegradationAllUpdatablePropertiesEquals(expectedDegradation, getPersistedDegradation(expectedDegradation));
    }
}
