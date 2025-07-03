package com.orange.qos.web.rest;

import static com.orange.qos.domain.SiteAsserts.*;
import static com.orange.qos.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.qos.IntegrationTest;
import com.orange.qos.domain.Site;
import com.orange.qos.repository.SiteRepository;
import com.orange.qos.service.dto.SiteDTO;
import com.orange.qos.service.mapper.SiteMapper;
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
 * Integration tests for the {@link SiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SiteResourceIT {

    private static final String DEFAULT_NOM_SITE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_SITE = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_OCI = "AAAAAAAAAA";
    private static final String UPDATED_CODE_OCI = "BBBBBBBBBB";

    private static final Float DEFAULT_LONGITUDE = 1F;
    private static final Float UPDATED_LONGITUDE = 2F;
    private static final Float SMALLER_LONGITUDE = 1F - 1F;

    private static final Float DEFAULT_LATITUDE = 1F;
    private static final Float UPDATED_LATITUDE = 2F;
    private static final Float SMALLER_LATITUDE = 1F - 1F;

    private static final String DEFAULT_STATUT = "AAAAAAAAAA";
    private static final String UPDATED_STATUT = "BBBBBBBBBB";

    private static final String DEFAULT_TECHNOLOGIE = "AAAAAAAAAA";
    private static final String UPDATED_TECHNOLOGIE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EN_SERVICE = false;
    private static final Boolean UPDATED_EN_SERVICE = true;

    private static final String ENTITY_API_URL = "/api/sites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private SiteMapper siteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSiteMockMvc;

    private Site site;

    private Site insertedSite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Site createEntity() {
        return new Site()
            .nomSite(DEFAULT_NOM_SITE)
            .codeOCI(DEFAULT_CODE_OCI)
            .longitude(DEFAULT_LONGITUDE)
            .latitude(DEFAULT_LATITUDE)
            .statut(DEFAULT_STATUT)
            .technologie(DEFAULT_TECHNOLOGIE)
            .enService(DEFAULT_EN_SERVICE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Site createUpdatedEntity() {
        return new Site()
            .nomSite(UPDATED_NOM_SITE)
            .codeOCI(UPDATED_CODE_OCI)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .statut(UPDATED_STATUT)
            .technologie(UPDATED_TECHNOLOGIE)
            .enService(UPDATED_EN_SERVICE);
    }

    @BeforeEach
    void initTest() {
        site = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSite != null) {
            siteRepository.delete(insertedSite);
            insertedSite = null;
        }
    }

    @Test
    @Transactional
    void createSite() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);
        var returnedSiteDTO = om.readValue(
            restSiteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SiteDTO.class
        );

        // Validate the Site in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSite = siteMapper.toEntity(returnedSiteDTO);
        assertSiteUpdatableFieldsEquals(returnedSite, getPersistedSite(returnedSite));

        insertedSite = returnedSite;
    }

    @Test
    @Transactional
    void createSiteWithExistingId() throws Exception {
        // Create the Site with an existing ID
        site.setId(1L);
        SiteDTO siteDTO = siteMapper.toDto(site);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomSiteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        site.setNomSite(null);

        // Create the Site, which fails.
        SiteDTO siteDTO = siteMapper.toDto(site);

        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLongitudeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        site.setLongitude(null);

        // Create the Site, which fails.
        SiteDTO siteDTO = siteMapper.toDto(site);

        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLatitudeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        site.setLatitude(null);

        // Create the Site, which fails.
        SiteDTO siteDTO = siteMapper.toDto(site);

        restSiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSites() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList
        restSiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(site.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomSite").value(hasItem(DEFAULT_NOM_SITE)))
            .andExpect(jsonPath("$.[*].codeOCI").value(hasItem(DEFAULT_CODE_OCI)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)))
            .andExpect(jsonPath("$.[*].technologie").value(hasItem(DEFAULT_TECHNOLOGIE)))
            .andExpect(jsonPath("$.[*].enService").value(hasItem(DEFAULT_EN_SERVICE)));
    }

    @Test
    @Transactional
    void getSite() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get the site
        restSiteMockMvc
            .perform(get(ENTITY_API_URL_ID, site.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(site.getId().intValue()))
            .andExpect(jsonPath("$.nomSite").value(DEFAULT_NOM_SITE))
            .andExpect(jsonPath("$.codeOCI").value(DEFAULT_CODE_OCI))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT))
            .andExpect(jsonPath("$.technologie").value(DEFAULT_TECHNOLOGIE))
            .andExpect(jsonPath("$.enService").value(DEFAULT_EN_SERVICE));
    }

    @Test
    @Transactional
    void getSitesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        Long id = site.getId();

        defaultSiteFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSiteFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSiteFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSitesByNomSiteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where nomSite equals to
        defaultSiteFiltering("nomSite.equals=" + DEFAULT_NOM_SITE, "nomSite.equals=" + UPDATED_NOM_SITE);
    }

    @Test
    @Transactional
    void getAllSitesByNomSiteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where nomSite in
        defaultSiteFiltering("nomSite.in=" + DEFAULT_NOM_SITE + "," + UPDATED_NOM_SITE, "nomSite.in=" + UPDATED_NOM_SITE);
    }

    @Test
    @Transactional
    void getAllSitesByNomSiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where nomSite is not null
        defaultSiteFiltering("nomSite.specified=true", "nomSite.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByNomSiteContainsSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where nomSite contains
        defaultSiteFiltering("nomSite.contains=" + DEFAULT_NOM_SITE, "nomSite.contains=" + UPDATED_NOM_SITE);
    }

    @Test
    @Transactional
    void getAllSitesByNomSiteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where nomSite does not contain
        defaultSiteFiltering("nomSite.doesNotContain=" + UPDATED_NOM_SITE, "nomSite.doesNotContain=" + DEFAULT_NOM_SITE);
    }

    @Test
    @Transactional
    void getAllSitesByCodeOCIIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where codeOCI equals to
        defaultSiteFiltering("codeOCI.equals=" + DEFAULT_CODE_OCI, "codeOCI.equals=" + UPDATED_CODE_OCI);
    }

    @Test
    @Transactional
    void getAllSitesByCodeOCIIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where codeOCI in
        defaultSiteFiltering("codeOCI.in=" + DEFAULT_CODE_OCI + "," + UPDATED_CODE_OCI, "codeOCI.in=" + UPDATED_CODE_OCI);
    }

    @Test
    @Transactional
    void getAllSitesByCodeOCIIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where codeOCI is not null
        defaultSiteFiltering("codeOCI.specified=true", "codeOCI.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByCodeOCIContainsSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where codeOCI contains
        defaultSiteFiltering("codeOCI.contains=" + DEFAULT_CODE_OCI, "codeOCI.contains=" + UPDATED_CODE_OCI);
    }

    @Test
    @Transactional
    void getAllSitesByCodeOCINotContainsSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where codeOCI does not contain
        defaultSiteFiltering("codeOCI.doesNotContain=" + UPDATED_CODE_OCI, "codeOCI.doesNotContain=" + DEFAULT_CODE_OCI);
    }

    @Test
    @Transactional
    void getAllSitesByLongitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where longitude equals to
        defaultSiteFiltering("longitude.equals=" + DEFAULT_LONGITUDE, "longitude.equals=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllSitesByLongitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where longitude in
        defaultSiteFiltering("longitude.in=" + DEFAULT_LONGITUDE + "," + UPDATED_LONGITUDE, "longitude.in=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllSitesByLongitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where longitude is not null
        defaultSiteFiltering("longitude.specified=true", "longitude.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByLongitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where longitude is greater than or equal to
        defaultSiteFiltering("longitude.greaterThanOrEqual=" + DEFAULT_LONGITUDE, "longitude.greaterThanOrEqual=" + UPDATED_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllSitesByLongitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where longitude is less than or equal to
        defaultSiteFiltering("longitude.lessThanOrEqual=" + DEFAULT_LONGITUDE, "longitude.lessThanOrEqual=" + SMALLER_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllSitesByLongitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where longitude is less than
        defaultSiteFiltering("longitude.lessThan=" + UPDATED_LONGITUDE, "longitude.lessThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllSitesByLongitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where longitude is greater than
        defaultSiteFiltering("longitude.greaterThan=" + SMALLER_LONGITUDE, "longitude.greaterThan=" + DEFAULT_LONGITUDE);
    }

    @Test
    @Transactional
    void getAllSitesByLatitudeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where latitude equals to
        defaultSiteFiltering("latitude.equals=" + DEFAULT_LATITUDE, "latitude.equals=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllSitesByLatitudeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where latitude in
        defaultSiteFiltering("latitude.in=" + DEFAULT_LATITUDE + "," + UPDATED_LATITUDE, "latitude.in=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllSitesByLatitudeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where latitude is not null
        defaultSiteFiltering("latitude.specified=true", "latitude.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByLatitudeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where latitude is greater than or equal to
        defaultSiteFiltering("latitude.greaterThanOrEqual=" + DEFAULT_LATITUDE, "latitude.greaterThanOrEqual=" + UPDATED_LATITUDE);
    }

    @Test
    @Transactional
    void getAllSitesByLatitudeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where latitude is less than or equal to
        defaultSiteFiltering("latitude.lessThanOrEqual=" + DEFAULT_LATITUDE, "latitude.lessThanOrEqual=" + SMALLER_LATITUDE);
    }

    @Test
    @Transactional
    void getAllSitesByLatitudeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where latitude is less than
        defaultSiteFiltering("latitude.lessThan=" + UPDATED_LATITUDE, "latitude.lessThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllSitesByLatitudeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where latitude is greater than
        defaultSiteFiltering("latitude.greaterThan=" + SMALLER_LATITUDE, "latitude.greaterThan=" + DEFAULT_LATITUDE);
    }

    @Test
    @Transactional
    void getAllSitesByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where statut equals to
        defaultSiteFiltering("statut.equals=" + DEFAULT_STATUT, "statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllSitesByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where statut in
        defaultSiteFiltering("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT, "statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllSitesByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where statut is not null
        defaultSiteFiltering("statut.specified=true", "statut.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByStatutContainsSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where statut contains
        defaultSiteFiltering("statut.contains=" + DEFAULT_STATUT, "statut.contains=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    void getAllSitesByStatutNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where statut does not contain
        defaultSiteFiltering("statut.doesNotContain=" + UPDATED_STATUT, "statut.doesNotContain=" + DEFAULT_STATUT);
    }

    @Test
    @Transactional
    void getAllSitesByTechnologieIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where technologie equals to
        defaultSiteFiltering("technologie.equals=" + DEFAULT_TECHNOLOGIE, "technologie.equals=" + UPDATED_TECHNOLOGIE);
    }

    @Test
    @Transactional
    void getAllSitesByTechnologieIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where technologie in
        defaultSiteFiltering("technologie.in=" + DEFAULT_TECHNOLOGIE + "," + UPDATED_TECHNOLOGIE, "technologie.in=" + UPDATED_TECHNOLOGIE);
    }

    @Test
    @Transactional
    void getAllSitesByTechnologieIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where technologie is not null
        defaultSiteFiltering("technologie.specified=true", "technologie.specified=false");
    }

    @Test
    @Transactional
    void getAllSitesByTechnologieContainsSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where technologie contains
        defaultSiteFiltering("technologie.contains=" + DEFAULT_TECHNOLOGIE, "technologie.contains=" + UPDATED_TECHNOLOGIE);
    }

    @Test
    @Transactional
    void getAllSitesByTechnologieNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where technologie does not contain
        defaultSiteFiltering("technologie.doesNotContain=" + UPDATED_TECHNOLOGIE, "technologie.doesNotContain=" + DEFAULT_TECHNOLOGIE);
    }

    @Test
    @Transactional
    void getAllSitesByEnServiceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where enService equals to
        defaultSiteFiltering("enService.equals=" + DEFAULT_EN_SERVICE, "enService.equals=" + UPDATED_EN_SERVICE);
    }

    @Test
    @Transactional
    void getAllSitesByEnServiceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where enService in
        defaultSiteFiltering("enService.in=" + DEFAULT_EN_SERVICE + "," + UPDATED_EN_SERVICE, "enService.in=" + UPDATED_EN_SERVICE);
    }

    @Test
    @Transactional
    void getAllSitesByEnServiceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        // Get all the siteList where enService is not null
        defaultSiteFiltering("enService.specified=true", "enService.specified=false");
    }

    private void defaultSiteFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSiteShouldBeFound(shouldBeFound);
        defaultSiteShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSiteShouldBeFound(String filter) throws Exception {
        restSiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(site.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomSite").value(hasItem(DEFAULT_NOM_SITE)))
            .andExpect(jsonPath("$.[*].codeOCI").value(hasItem(DEFAULT_CODE_OCI)))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT)))
            .andExpect(jsonPath("$.[*].technologie").value(hasItem(DEFAULT_TECHNOLOGIE)))
            .andExpect(jsonPath("$.[*].enService").value(hasItem(DEFAULT_EN_SERVICE)));

        // Check, that the count call also returns 1
        restSiteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSiteShouldNotBeFound(String filter) throws Exception {
        restSiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSiteMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSite() throws Exception {
        // Get the site
        restSiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSite() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the site
        Site updatedSite = siteRepository.findById(site.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSite are not directly saved in db
        em.detach(updatedSite);
        updatedSite
            .nomSite(UPDATED_NOM_SITE)
            .codeOCI(UPDATED_CODE_OCI)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .statut(UPDATED_STATUT)
            .technologie(UPDATED_TECHNOLOGIE)
            .enService(UPDATED_EN_SERVICE);
        SiteDTO siteDTO = siteMapper.toDto(updatedSite);

        restSiteMockMvc
            .perform(put(ENTITY_API_URL_ID, siteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteDTO)))
            .andExpect(status().isOk());

        // Validate the Site in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSiteToMatchAllProperties(updatedSite);
    }

    @Test
    @Transactional
    void putNonExistingSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        site.setId(longCount.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(put(ENTITY_API_URL_ID, siteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        site.setId(longCount.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(siteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        site.setId(longCount.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Site in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        partialUpdatedSite.codeOCI(UPDATED_CODE_OCI).longitude(UPDATED_LONGITUDE).statut(UPDATED_STATUT).enService(UPDATED_EN_SERVICE);

        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSite))
            )
            .andExpect(status().isOk());

        // Validate the Site in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSiteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSite, site), getPersistedSite(site));
    }

    @Test
    @Transactional
    void fullUpdateSiteWithPatch() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the site using partial update
        Site partialUpdatedSite = new Site();
        partialUpdatedSite.setId(site.getId());

        partialUpdatedSite
            .nomSite(UPDATED_NOM_SITE)
            .codeOCI(UPDATED_CODE_OCI)
            .longitude(UPDATED_LONGITUDE)
            .latitude(UPDATED_LATITUDE)
            .statut(UPDATED_STATUT)
            .technologie(UPDATED_TECHNOLOGIE)
            .enService(UPDATED_EN_SERVICE);

        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSite.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSite))
            )
            .andExpect(status().isOk());

        // Validate the Site in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSiteUpdatableFieldsEquals(partialUpdatedSite, getPersistedSite(partialUpdatedSite));
    }

    @Test
    @Transactional
    void patchNonExistingSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        site.setId(longCount.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, siteDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(siteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        site.setId(longCount.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(siteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Site in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSite() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        site.setId(longCount.incrementAndGet());

        // Create the Site
        SiteDTO siteDTO = siteMapper.toDto(site);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSiteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(siteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Site in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSite() throws Exception {
        // Initialize the database
        insertedSite = siteRepository.saveAndFlush(site);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the site
        restSiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, site.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return siteRepository.count();
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

    protected Site getPersistedSite(Site site) {
        return siteRepository.findById(site.getId()).orElseThrow();
    }

    protected void assertPersistedSiteToMatchAllProperties(Site expectedSite) {
        assertSiteAllPropertiesEquals(expectedSite, getPersistedSite(expectedSite));
    }

    protected void assertPersistedSiteToMatchUpdatableProperties(Site expectedSite) {
        assertSiteAllUpdatablePropertiesEquals(expectedSite, getPersistedSite(expectedSite));
    }
}
