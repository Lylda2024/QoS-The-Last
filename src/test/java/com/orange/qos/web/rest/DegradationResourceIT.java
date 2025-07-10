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
import com.orange.qos.repository.DegradationRepository;
import com.orange.qos.service.dto.DegradationDTO;
import com.orange.qos.service.mapper.DegradationMapper;
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
 * Integration tests for the {@link DegradationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DegradationResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_SIGNALEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_SIGNALEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_STATUT = "AAAAAAAAAA";
    private static final String UPDATED_STATUT = "BBBBBBBBBB";

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
        return new Degradation().description(DEFAULT_DESCRIPTION).dateSignalement(DEFAULT_DATE_SIGNALEMENT).statut(DEFAULT_STATUT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Degradation createUpdatedEntity() {
        return new Degradation().description(UPDATED_DESCRIPTION).dateSignalement(UPDATED_DATE_SIGNALEMENT).statut(UPDATED_STATUT);
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
    void getAllDegradations() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList
        restDegradationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(degradation.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateSignalement").value(hasItem(DEFAULT_DATE_SIGNALEMENT.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)));
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
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateSignalement").value(DEFAULT_DATE_SIGNALEMENT.toString()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT));
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
    void getAllDegradationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where description equals to
        defaultDegradationFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDegradationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where description in
        defaultDegradationFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllDegradationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where description is not null
        defaultDegradationFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllDegradationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where description contains
        defaultDegradationFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDegradationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where description does not contain
        defaultDegradationFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllDegradationsByDateSignalementIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where dateSignalement equals to
        defaultDegradationFiltering(
            "dateSignalement.equals=" + DEFAULT_DATE_SIGNALEMENT,
            "dateSignalement.equals=" + UPDATED_DATE_SIGNALEMENT
        );
    }

    @Test
    @Transactional
    void getAllDegradationsByDateSignalementIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where dateSignalement in
        defaultDegradationFiltering(
            "dateSignalement.in=" + DEFAULT_DATE_SIGNALEMENT + "," + UPDATED_DATE_SIGNALEMENT,
            "dateSignalement.in=" + UPDATED_DATE_SIGNALEMENT
        );
    }

    @Test
    @Transactional
    void getAllDegradationsByDateSignalementIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where dateSignalement is not null
        defaultDegradationFiltering("dateSignalement.specified=true", "dateSignalement.specified=false");
    }

    @Test
    @Transactional
    void getAllDegradationsByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where statut equals to
        defaultDegradationFiltering("statut.equals=" + DEFAULT_STATUT, "statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllDegradationsByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where statut in
        defaultDegradationFiltering("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT, "statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllDegradationsByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where statut is not null
        defaultDegradationFiltering("statut.specified=true", "statut.specified=false");
    }

    @Test
    @Transactional
    void getAllDegradationsByStatutContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where statut contains
        defaultDegradationFiltering("statut.contains=" + DEFAULT_STATUT, "statut.contains=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllDegradationsByStatutNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDegradation = degradationRepository.saveAndFlush(degradation);

        // Get all the degradationList where statut does not contain
        defaultDegradationFiltering("statut.doesNotContain=" + UPDATED_STATUT, "statut.doesNotContain=" + DEFAULT_STATUT);
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
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateSignalement").value(hasItem(DEFAULT_DATE_SIGNALEMENT.toString())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)));

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
        updatedDegradation.description(UPDATED_DESCRIPTION).dateSignalement(UPDATED_DATE_SIGNALEMENT).statut(UPDATED_STATUT);
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

        partialUpdatedDegradation.description(UPDATED_DESCRIPTION).dateSignalement(UPDATED_DATE_SIGNALEMENT);

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

        partialUpdatedDegradation.description(UPDATED_DESCRIPTION).dateSignalement(UPDATED_DATE_SIGNALEMENT).statut(UPDATED_STATUT);

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
