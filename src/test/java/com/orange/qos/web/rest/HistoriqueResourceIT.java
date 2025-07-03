package com.orange.qos.web.rest;

import static com.orange.qos.domain.HistoriqueAsserts.*;
import static com.orange.qos.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.qos.IntegrationTest;
import com.orange.qos.domain.Degradation;
import com.orange.qos.domain.Historique;
import com.orange.qos.repository.HistoriqueRepository;
import com.orange.qos.service.dto.HistoriqueDTO;
import com.orange.qos.service.mapper.HistoriqueMapper;
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
 * Integration tests for the {@link HistoriqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HistoriqueResourceIT {

    private static final String DEFAULT_UTILISATEUR = "AAAAAAAAAA";
    private static final String UPDATED_UTILISATEUR = "BBBBBBBBBB";

    private static final String DEFAULT_SECTION = "AAAAAAAAAA";
    private static final String UPDATED_SECTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_HORODATAGE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_HORODATAGE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/historiques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HistoriqueRepository historiqueRepository;

    @Autowired
    private HistoriqueMapper historiqueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoriqueMockMvc;

    private Historique historique;

    private Historique insertedHistorique;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historique createEntity() {
        return new Historique().utilisateur(DEFAULT_UTILISATEUR).section(DEFAULT_SECTION).horodatage(DEFAULT_HORODATAGE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Historique createUpdatedEntity() {
        return new Historique().utilisateur(UPDATED_UTILISATEUR).section(UPDATED_SECTION).horodatage(UPDATED_HORODATAGE);
    }

    @BeforeEach
    void initTest() {
        historique = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedHistorique != null) {
            historiqueRepository.delete(insertedHistorique);
            insertedHistorique = null;
        }
    }

    @Test
    @Transactional
    void createHistorique() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Historique
        HistoriqueDTO historiqueDTO = historiqueMapper.toDto(historique);
        var returnedHistoriqueDTO = om.readValue(
            restHistoriqueMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HistoriqueDTO.class
        );

        // Validate the Historique in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHistorique = historiqueMapper.toEntity(returnedHistoriqueDTO);
        assertHistoriqueUpdatableFieldsEquals(returnedHistorique, getPersistedHistorique(returnedHistorique));

        insertedHistorique = returnedHistorique;
    }

    @Test
    @Transactional
    void createHistoriqueWithExistingId() throws Exception {
        // Create the Historique with an existing ID
        historique.setId(1L);
        HistoriqueDTO historiqueDTO = historiqueMapper.toDto(historique);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoriqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Historique in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkHorodatageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historique.setHorodatage(null);

        // Create the Historique, which fails.
        HistoriqueDTO historiqueDTO = historiqueMapper.toDto(historique);

        restHistoriqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistoriques() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        // Get all the historiqueList
        restHistoriqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historique.getId().intValue())))
            .andExpect(jsonPath("$.[*].utilisateur").value(hasItem(DEFAULT_UTILISATEUR)))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)))
            .andExpect(jsonPath("$.[*].horodatage").value(hasItem(DEFAULT_HORODATAGE.toString())));
    }

    @Test
    @Transactional
    void getHistorique() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        // Get the historique
        restHistoriqueMockMvc
            .perform(get(ENTITY_API_URL_ID, historique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historique.getId().intValue()))
            .andExpect(jsonPath("$.utilisateur").value(DEFAULT_UTILISATEUR))
            .andExpect(jsonPath("$.section").value(DEFAULT_SECTION))
            .andExpect(jsonPath("$.horodatage").value(DEFAULT_HORODATAGE.toString()));
    }

    @Test
    @Transactional
    void getHistoriquesByIdFiltering() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        Long id = historique.getId();

        defaultHistoriqueFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHistoriqueFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHistoriqueFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHistoriquesByUtilisateurIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        // Get all the historiqueList where utilisateur equals to
        defaultHistoriqueFiltering("utilisateur.equals=" + DEFAULT_UTILISATEUR, "utilisateur.equals=" + UPDATED_UTILISATEUR);
    }

    @Test
    @Transactional
    void getAllHistoriquesByUtilisateurIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        // Get all the historiqueList where utilisateur in
        defaultHistoriqueFiltering(
            "utilisateur.in=" + DEFAULT_UTILISATEUR + "," + UPDATED_UTILISATEUR,
            "utilisateur.in=" + UPDATED_UTILISATEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriquesByUtilisateurIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        // Get all the historiqueList where utilisateur is not null
        defaultHistoriqueFiltering("utilisateur.specified=true", "utilisateur.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriquesByUtilisateurContainsSomething() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        // Get all the historiqueList where utilisateur contains
        defaultHistoriqueFiltering("utilisateur.contains=" + DEFAULT_UTILISATEUR, "utilisateur.contains=" + UPDATED_UTILISATEUR);
    }

    @Test
    @Transactional
    void getAllHistoriquesByUtilisateurNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        // Get all the historiqueList where utilisateur does not contain
        defaultHistoriqueFiltering(
            "utilisateur.doesNotContain=" + UPDATED_UTILISATEUR,
            "utilisateur.doesNotContain=" + DEFAULT_UTILISATEUR
        );
    }

    @Test
    @Transactional
    void getAllHistoriquesBySectionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        // Get all the historiqueList where section equals to
        defaultHistoriqueFiltering("section.equals=" + DEFAULT_SECTION, "section.equals=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllHistoriquesBySectionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        // Get all the historiqueList where section in
        defaultHistoriqueFiltering("section.in=" + DEFAULT_SECTION + "," + UPDATED_SECTION, "section.in=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllHistoriquesBySectionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        // Get all the historiqueList where section is not null
        defaultHistoriqueFiltering("section.specified=true", "section.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriquesBySectionContainsSomething() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        // Get all the historiqueList where section contains
        defaultHistoriqueFiltering("section.contains=" + DEFAULT_SECTION, "section.contains=" + UPDATED_SECTION);
    }

    @Test
    @Transactional
    void getAllHistoriquesBySectionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        // Get all the historiqueList where section does not contain
        defaultHistoriqueFiltering("section.doesNotContain=" + UPDATED_SECTION, "section.doesNotContain=" + DEFAULT_SECTION);
    }

    @Test
    @Transactional
    void getAllHistoriquesByHorodatageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        // Get all the historiqueList where horodatage equals to
        defaultHistoriqueFiltering("horodatage.equals=" + DEFAULT_HORODATAGE, "horodatage.equals=" + UPDATED_HORODATAGE);
    }

    @Test
    @Transactional
    void getAllHistoriquesByHorodatageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        // Get all the historiqueList where horodatage in
        defaultHistoriqueFiltering("horodatage.in=" + DEFAULT_HORODATAGE + "," + UPDATED_HORODATAGE, "horodatage.in=" + UPDATED_HORODATAGE);
    }

    @Test
    @Transactional
    void getAllHistoriquesByHorodatageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        // Get all the historiqueList where horodatage is not null
        defaultHistoriqueFiltering("horodatage.specified=true", "horodatage.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoriquesByDegradationIsEqualToSomething() throws Exception {
        Degradation degradation;
        if (TestUtil.findAll(em, Degradation.class).isEmpty()) {
            historiqueRepository.saveAndFlush(historique);
            degradation = DegradationResourceIT.createEntity();
        } else {
            degradation = TestUtil.findAll(em, Degradation.class).get(0);
        }
        em.persist(degradation);
        em.flush();
        historique.setDegradation(degradation);
        historiqueRepository.saveAndFlush(historique);
        Long degradationId = degradation.getId();
        // Get all the historiqueList where degradation equals to degradationId
        defaultHistoriqueShouldBeFound("degradationId.equals=" + degradationId);

        // Get all the historiqueList where degradation equals to (degradationId + 1)
        defaultHistoriqueShouldNotBeFound("degradationId.equals=" + (degradationId + 1));
    }

    private void defaultHistoriqueFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHistoriqueShouldBeFound(shouldBeFound);
        defaultHistoriqueShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHistoriqueShouldBeFound(String filter) throws Exception {
        restHistoriqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historique.getId().intValue())))
            .andExpect(jsonPath("$.[*].utilisateur").value(hasItem(DEFAULT_UTILISATEUR)))
            .andExpect(jsonPath("$.[*].section").value(hasItem(DEFAULT_SECTION)))
            .andExpect(jsonPath("$.[*].horodatage").value(hasItem(DEFAULT_HORODATAGE.toString())));

        // Check, that the count call also returns 1
        restHistoriqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHistoriqueShouldNotBeFound(String filter) throws Exception {
        restHistoriqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHistoriqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHistorique() throws Exception {
        // Get the historique
        restHistoriqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHistorique() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historique
        Historique updatedHistorique = historiqueRepository.findById(historique.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHistorique are not directly saved in db
        em.detach(updatedHistorique);
        updatedHistorique.utilisateur(UPDATED_UTILISATEUR).section(UPDATED_SECTION).horodatage(UPDATED_HORODATAGE);
        HistoriqueDTO historiqueDTO = historiqueMapper.toDto(updatedHistorique);

        restHistoriqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiqueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueDTO))
            )
            .andExpect(status().isOk());

        // Validate the Historique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHistoriqueToMatchAllProperties(updatedHistorique);
    }

    @Test
    @Transactional
    void putNonExistingHistorique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historique.setId(longCount.incrementAndGet());

        // Create the Historique
        HistoriqueDTO historiqueDTO = historiqueMapper.toDto(historique);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historiqueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistorique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historique.setId(longCount.incrementAndGet());

        // Create the Historique
        HistoriqueDTO historiqueDTO = historiqueMapper.toDto(historique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistorique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historique.setId(longCount.incrementAndGet());

        // Create the Historique
        HistoriqueDTO historiqueDTO = historiqueMapper.toDto(historique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historiqueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Historique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoriqueWithPatch() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historique using partial update
        Historique partialUpdatedHistorique = new Historique();
        partialUpdatedHistorique.setId(historique.getId());

        partialUpdatedHistorique.section(UPDATED_SECTION).horodatage(UPDATED_HORODATAGE);

        restHistoriqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistorique.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistorique))
            )
            .andExpect(status().isOk());

        // Validate the Historique in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoriqueUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHistorique, historique),
            getPersistedHistorique(historique)
        );
    }

    @Test
    @Transactional
    void fullUpdateHistoriqueWithPatch() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historique using partial update
        Historique partialUpdatedHistorique = new Historique();
        partialUpdatedHistorique.setId(historique.getId());

        partialUpdatedHistorique.utilisateur(UPDATED_UTILISATEUR).section(UPDATED_SECTION).horodatage(UPDATED_HORODATAGE);

        restHistoriqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistorique.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistorique))
            )
            .andExpect(status().isOk());

        // Validate the Historique in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoriqueUpdatableFieldsEquals(partialUpdatedHistorique, getPersistedHistorique(partialUpdatedHistorique));
    }

    @Test
    @Transactional
    void patchNonExistingHistorique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historique.setId(longCount.incrementAndGet());

        // Create the Historique
        HistoriqueDTO historiqueDTO = historiqueMapper.toDto(historique);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoriqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historiqueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistorique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historique.setId(longCount.incrementAndGet());

        // Create the Historique
        HistoriqueDTO historiqueDTO = historiqueMapper.toDto(historique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historiqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Historique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistorique() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historique.setId(longCount.incrementAndGet());

        // Create the Historique
        HistoriqueDTO historiqueDTO = historiqueMapper.toDto(historique);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoriqueMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(historiqueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Historique in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistorique() throws Exception {
        // Initialize the database
        insertedHistorique = historiqueRepository.saveAndFlush(historique);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the historique
        restHistoriqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, historique.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return historiqueRepository.count();
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

    protected Historique getPersistedHistorique(Historique historique) {
        return historiqueRepository.findById(historique.getId()).orElseThrow();
    }

    protected void assertPersistedHistoriqueToMatchAllProperties(Historique expectedHistorique) {
        assertHistoriqueAllPropertiesEquals(expectedHistorique, getPersistedHistorique(expectedHistorique));
    }

    protected void assertPersistedHistoriqueToMatchUpdatableProperties(Historique expectedHistorique) {
        assertHistoriqueAllUpdatablePropertiesEquals(expectedHistorique, getPersistedHistorique(expectedHistorique));
    }
}
