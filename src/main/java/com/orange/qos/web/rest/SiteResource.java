package com.orange.qos.web.rest;

import com.orange.qos.repository.SiteRepository;
import com.orange.qos.service.SiteQueryService;
import com.orange.qos.service.SiteService;
import com.orange.qos.service.criteria.SiteCriteria;
import com.orange.qos.service.dto.SiteDTO;
import com.orange.qos.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.orange.qos.domain.Site}.
 */
@RestController
@RequestMapping("/api/sites")
public class SiteResource {

    private static final Logger LOG = LoggerFactory.getLogger(SiteResource.class);

    private static final String ENTITY_NAME = "site";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SiteService siteService;

    private final SiteRepository siteRepository;

    private final SiteQueryService siteQueryService;

    public SiteResource(SiteService siteService, SiteRepository siteRepository, SiteQueryService siteQueryService) {
        this.siteService = siteService;
        this.siteRepository = siteRepository;
        this.siteQueryService = siteQueryService;
    }

    /**
     * {@code POST  /sites} : Create a new site.
     *
     * @param siteDTO the siteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new siteDTO, or with status {@code 400 (Bad Request)} if the site has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SiteDTO> createSite(@Valid @RequestBody SiteDTO siteDTO) throws URISyntaxException {
        LOG.debug("REST request to save Site : {}", siteDTO);
        if (siteDTO.getId() != null) {
            throw new BadRequestAlertException("A new site cannot already have an ID", ENTITY_NAME, "idexists");
        }
        siteDTO = siteService.save(siteDTO);
        return ResponseEntity.created(new URI("/api/sites/" + siteDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, siteDTO.getId().toString()))
            .body(siteDTO);
    }

    /**
     * {@code PUT  /sites/:id} : Updates an existing site.
     *
     * @param id the id of the siteDTO to save.
     * @param siteDTO the siteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteDTO,
     * or with status {@code 400 (Bad Request)} if the siteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the siteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SiteDTO> updateSite(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SiteDTO siteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Site : {}, {}", id, siteDTO);
        if (siteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, siteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!siteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        siteDTO = siteService.update(siteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteDTO.getId().toString()))
            .body(siteDTO);
    }

    /**
     * {@code PATCH  /sites/:id} : Partial updates given fields of an existing site, field will ignore if it is null
     *
     * @param id the id of the siteDTO to save.
     * @param siteDTO the siteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated siteDTO,
     * or with status {@code 400 (Bad Request)} if the siteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the siteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the siteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SiteDTO> partialUpdateSite(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SiteDTO siteDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Site partially : {}, {}", id, siteDTO);
        if (siteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, siteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!siteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SiteDTO> result = siteService.partialUpdate(siteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, siteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sites} : get all the sites.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sites in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SiteDTO>> getAllSites(
        SiteCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Sites by criteria: {}", criteria);

        Page<SiteDTO> page = siteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sites/count} : count all the sites.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSites(SiteCriteria criteria) {
        LOG.debug("REST request to count Sites by criteria: {}", criteria);
        return ResponseEntity.ok().body(siteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sites/:id} : get the "id" site.
     *
     * @param id the id of the siteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the siteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SiteDTO> getSite(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Site : {}", id);
        Optional<SiteDTO> siteDTO = siteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(siteDTO);
    }

    /**
     * {@code DELETE  /sites/:id} : delete the "id" site.
     *
     * @param id the id of the siteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSite(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Site : {}", id);
        siteService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
