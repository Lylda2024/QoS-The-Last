package com.orange.qos.web.rest;

import static com.orange.qos.domain.TypeUtilisateurAsserts.*;
import static com.orange.qos.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.qos.IntegrationTest;
import com.orange.qos.domain.TypeUtilisateur;
import com.orange.qos.repository.TypeUtilisateurRepository;
import com.orange.qos.service.dto.TypeUtilisateurDTO;
import com.orange.qos.service.mapper.TypeUtilisateurMapper;
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
 * Integration tests for the {@link TypeUtilisateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeUtilisateurResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_NIVEAU = 1;
    private static final Integer UPDATED_NIVEAU = 2;
    private static final Integer SMALLER_NIVEAU = 1 - 1;

    private static final String DEFAULT_PERMISSIONS = "AAAAAAAAAA";
    private static final String UPDATED_PERMISSIONS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-utilisateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TypeUtilisateurRepository typeUtilisateurRepository;

    @Autowired
    private TypeUtilisateurMapper typeUtilisateurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeUtilisateurMockMvc;

    private TypeUtilisateur typeUtilisateur;

    private TypeUtilisateur insertedTypeUtilisateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeUtilisateur createEntity() {
        return new TypeUtilisateur()
            .nom(DEFAULT_NOM)
            .description(DEFAULT_DESCRIPTION)
            .niveau(DEFAULT_NIVEAU)
            .permissions(DEFAULT_PERMISSIONS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeUtilisateur createUpdatedEntity() {
        return new TypeUtilisateur()
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .niveau(UPDATED_NIVEAU)
            .permissions(UPDATED_PERMISSIONS);
    }

    @BeforeEach
    void initTest() {
        typeUtilisateur = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTypeUtilisateur != null) {
            typeUtilisateurRepository.delete(insertedTypeUtilisateur);
            insertedTypeUtilisateur = null;
        }
    }

    @Test
    @Transactional
    void createTypeUtilisateur() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TypeUtilisateur
        TypeUtilisateurDTO typeUtilisateurDTO = typeUtilisateurMapper.toDto(typeUtilisateur);
        var returnedTypeUtilisateurDTO = om.readValue(
            restTypeUtilisateurMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeUtilisateurDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TypeUtilisateurDTO.class
        );

        // Validate the TypeUtilisateur in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTypeUtilisateur = typeUtilisateurMapper.toEntity(returnedTypeUtilisateurDTO);
        assertTypeUtilisateurUpdatableFieldsEquals(returnedTypeUtilisateur, getPersistedTypeUtilisateur(returnedTypeUtilisateur));

        insertedTypeUtilisateur = returnedTypeUtilisateur;
    }

    @Test
    @Transactional
    void createTypeUtilisateurWithExistingId() throws Exception {
        // Create the TypeUtilisateur with an existing ID
        typeUtilisateur.setId(1L);
        TypeUtilisateurDTO typeUtilisateurDTO = typeUtilisateurMapper.toDto(typeUtilisateur);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeUtilisateurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TypeUtilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        typeUtilisateur.setNom(null);

        // Create the TypeUtilisateur, which fails.
        TypeUtilisateurDTO typeUtilisateurDTO = typeUtilisateurMapper.toDto(typeUtilisateur);

        restTypeUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeUtilisateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTypeUtilisateurs() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList
        restTypeUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeUtilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU)))
            .andExpect(jsonPath("$.[*].permissions").value(hasItem(DEFAULT_PERMISSIONS)));
    }

    @Test
    @Transactional
    void getTypeUtilisateur() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get the typeUtilisateur
        restTypeUtilisateurMockMvc
            .perform(get(ENTITY_API_URL_ID, typeUtilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeUtilisateur.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.niveau").value(DEFAULT_NIVEAU))
            .andExpect(jsonPath("$.permissions").value(DEFAULT_PERMISSIONS));
    }

    @Test
    @Transactional
    void getTypeUtilisateursByIdFiltering() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        Long id = typeUtilisateur.getId();

        defaultTypeUtilisateurFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTypeUtilisateurFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTypeUtilisateurFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByNomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where nom equals to
        defaultTypeUtilisateurFiltering("nom.equals=" + DEFAULT_NOM, "nom.equals=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByNomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where nom in
        defaultTypeUtilisateurFiltering("nom.in=" + DEFAULT_NOM + "," + UPDATED_NOM, "nom.in=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where nom is not null
        defaultTypeUtilisateurFiltering("nom.specified=true", "nom.specified=false");
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByNomContainsSomething() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where nom contains
        defaultTypeUtilisateurFiltering("nom.contains=" + DEFAULT_NOM, "nom.contains=" + UPDATED_NOM);
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByNomNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where nom does not contain
        defaultTypeUtilisateurFiltering("nom.doesNotContain=" + UPDATED_NOM, "nom.doesNotContain=" + DEFAULT_NOM);
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where description equals to
        defaultTypeUtilisateurFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where description in
        defaultTypeUtilisateurFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where description is not null
        defaultTypeUtilisateurFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where description contains
        defaultTypeUtilisateurFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where description does not contain
        defaultTypeUtilisateurFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByNiveauIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where niveau equals to
        defaultTypeUtilisateurFiltering("niveau.equals=" + DEFAULT_NIVEAU, "niveau.equals=" + UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByNiveauIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where niveau in
        defaultTypeUtilisateurFiltering("niveau.in=" + DEFAULT_NIVEAU + "," + UPDATED_NIVEAU, "niveau.in=" + UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByNiveauIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where niveau is not null
        defaultTypeUtilisateurFiltering("niveau.specified=true", "niveau.specified=false");
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByNiveauIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where niveau is greater than or equal to
        defaultTypeUtilisateurFiltering("niveau.greaterThanOrEqual=" + DEFAULT_NIVEAU, "niveau.greaterThanOrEqual=" + UPDATED_NIVEAU);
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByNiveauIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where niveau is less than or equal to
        defaultTypeUtilisateurFiltering("niveau.lessThanOrEqual=" + DEFAULT_NIVEAU, "niveau.lessThanOrEqual=" + SMALLER_NIVEAU);
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByNiveauIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where niveau is less than
        defaultTypeUtilisateurFiltering("niveau.lessThan=" + UPDATED_NIVEAU, "niveau.lessThan=" + DEFAULT_NIVEAU);
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByNiveauIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where niveau is greater than
        defaultTypeUtilisateurFiltering("niveau.greaterThan=" + SMALLER_NIVEAU, "niveau.greaterThan=" + DEFAULT_NIVEAU);
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByPermissionsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where permissions equals to
        defaultTypeUtilisateurFiltering("permissions.equals=" + DEFAULT_PERMISSIONS, "permissions.equals=" + UPDATED_PERMISSIONS);
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByPermissionsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where permissions in
        defaultTypeUtilisateurFiltering(
            "permissions.in=" + DEFAULT_PERMISSIONS + "," + UPDATED_PERMISSIONS,
            "permissions.in=" + UPDATED_PERMISSIONS
        );
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByPermissionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where permissions is not null
        defaultTypeUtilisateurFiltering("permissions.specified=true", "permissions.specified=false");
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByPermissionsContainsSomething() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where permissions contains
        defaultTypeUtilisateurFiltering("permissions.contains=" + DEFAULT_PERMISSIONS, "permissions.contains=" + UPDATED_PERMISSIONS);
    }

    @Test
    @Transactional
    void getAllTypeUtilisateursByPermissionsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        // Get all the typeUtilisateurList where permissions does not contain
        defaultTypeUtilisateurFiltering(
            "permissions.doesNotContain=" + UPDATED_PERMISSIONS,
            "permissions.doesNotContain=" + DEFAULT_PERMISSIONS
        );
    }

    private void defaultTypeUtilisateurFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTypeUtilisateurShouldBeFound(shouldBeFound);
        defaultTypeUtilisateurShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTypeUtilisateurShouldBeFound(String filter) throws Exception {
        restTypeUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeUtilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].niveau").value(hasItem(DEFAULT_NIVEAU)))
            .andExpect(jsonPath("$.[*].permissions").value(hasItem(DEFAULT_PERMISSIONS)));

        // Check, that the count call also returns 1
        restTypeUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTypeUtilisateurShouldNotBeFound(String filter) throws Exception {
        restTypeUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTypeUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTypeUtilisateur() throws Exception {
        // Get the typeUtilisateur
        restTypeUtilisateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTypeUtilisateur() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeUtilisateur
        TypeUtilisateur updatedTypeUtilisateur = typeUtilisateurRepository.findById(typeUtilisateur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTypeUtilisateur are not directly saved in db
        em.detach(updatedTypeUtilisateur);
        updatedTypeUtilisateur.nom(UPDATED_NOM).description(UPDATED_DESCRIPTION).niveau(UPDATED_NIVEAU).permissions(UPDATED_PERMISSIONS);
        TypeUtilisateurDTO typeUtilisateurDTO = typeUtilisateurMapper.toDto(updatedTypeUtilisateur);

        restTypeUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeUtilisateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeUtilisateurDTO))
            )
            .andExpect(status().isOk());

        // Validate the TypeUtilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTypeUtilisateurToMatchAllProperties(updatedTypeUtilisateur);
    }

    @Test
    @Transactional
    void putNonExistingTypeUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeUtilisateur.setId(longCount.incrementAndGet());

        // Create the TypeUtilisateur
        TypeUtilisateurDTO typeUtilisateurDTO = typeUtilisateurMapper.toDto(typeUtilisateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeUtilisateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeUtilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeUtilisateur.setId(longCount.incrementAndGet());

        // Create the TypeUtilisateur
        TypeUtilisateurDTO typeUtilisateurDTO = typeUtilisateurMapper.toDto(typeUtilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(typeUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeUtilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeUtilisateur.setId(longCount.incrementAndGet());

        // Create the TypeUtilisateur
        TypeUtilisateurDTO typeUtilisateurDTO = typeUtilisateurMapper.toDto(typeUtilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeUtilisateurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(typeUtilisateurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeUtilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeUtilisateurWithPatch() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeUtilisateur using partial update
        TypeUtilisateur partialUpdatedTypeUtilisateur = new TypeUtilisateur();
        partialUpdatedTypeUtilisateur.setId(typeUtilisateur.getId());

        partialUpdatedTypeUtilisateur
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .niveau(UPDATED_NIVEAU)
            .permissions(UPDATED_PERMISSIONS);

        restTypeUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeUtilisateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the TypeUtilisateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeUtilisateurUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTypeUtilisateur, typeUtilisateur),
            getPersistedTypeUtilisateur(typeUtilisateur)
        );
    }

    @Test
    @Transactional
    void fullUpdateTypeUtilisateurWithPatch() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the typeUtilisateur using partial update
        TypeUtilisateur partialUpdatedTypeUtilisateur = new TypeUtilisateur();
        partialUpdatedTypeUtilisateur.setId(typeUtilisateur.getId());

        partialUpdatedTypeUtilisateur
            .nom(UPDATED_NOM)
            .description(UPDATED_DESCRIPTION)
            .niveau(UPDATED_NIVEAU)
            .permissions(UPDATED_PERMISSIONS);

        restTypeUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeUtilisateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTypeUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the TypeUtilisateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTypeUtilisateurUpdatableFieldsEquals(
            partialUpdatedTypeUtilisateur,
            getPersistedTypeUtilisateur(partialUpdatedTypeUtilisateur)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTypeUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeUtilisateur.setId(longCount.incrementAndGet());

        // Create the TypeUtilisateur
        TypeUtilisateurDTO typeUtilisateurDTO = typeUtilisateurMapper.toDto(typeUtilisateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeUtilisateurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeUtilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeUtilisateur.setId(longCount.incrementAndGet());

        // Create the TypeUtilisateur
        TypeUtilisateurDTO typeUtilisateurDTO = typeUtilisateurMapper.toDto(typeUtilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(typeUtilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeUtilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        typeUtilisateur.setId(longCount.incrementAndGet());

        // Create the TypeUtilisateur
        TypeUtilisateurDTO typeUtilisateurDTO = typeUtilisateurMapper.toDto(typeUtilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeUtilisateurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(typeUtilisateurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeUtilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeUtilisateur() throws Exception {
        // Initialize the database
        insertedTypeUtilisateur = typeUtilisateurRepository.saveAndFlush(typeUtilisateur);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the typeUtilisateur
        restTypeUtilisateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeUtilisateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return typeUtilisateurRepository.count();
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

    protected TypeUtilisateur getPersistedTypeUtilisateur(TypeUtilisateur typeUtilisateur) {
        return typeUtilisateurRepository.findById(typeUtilisateur.getId()).orElseThrow();
    }

    protected void assertPersistedTypeUtilisateurToMatchAllProperties(TypeUtilisateur expectedTypeUtilisateur) {
        assertTypeUtilisateurAllPropertiesEquals(expectedTypeUtilisateur, getPersistedTypeUtilisateur(expectedTypeUtilisateur));
    }

    protected void assertPersistedTypeUtilisateurToMatchUpdatableProperties(TypeUtilisateur expectedTypeUtilisateur) {
        assertTypeUtilisateurAllUpdatablePropertiesEquals(expectedTypeUtilisateur, getPersistedTypeUtilisateur(expectedTypeUtilisateur));
    }
}
