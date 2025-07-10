package com.orange.qos.web.rest;

import com.orange.qos.repository.DelaiInterventionRepository;
import com.orange.qos.service.DelaiInterventionQueryService;
import com.orange.qos.service.DelaiInterventionService;
import com.orange.qos.service.criteria.DelaiInterventionCriteria;
import com.orange.qos.service.dto.DelaiInterventionDTO;
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
 * REST controller for managing {@link com.orange.qos.domain.DelaiIntervention}.
 */
@RestController
@RequestMapping("/api/delai-interventions")
public class DelaiInterventionResource {

    private static final Logger LOG = LoggerFactory.getLogger(DelaiInterventionResource.class);

    private static final String ENTITY_NAME = "delaiIntervention";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DelaiInterventionService delaiInterventionService;

    private final DelaiInterventionRepository delaiInterventionRepository;

    private final DelaiInterventionQueryService delaiInterventionQueryService;

    public DelaiInterventionResource(
        DelaiInterventionService delaiInterventionService,
        DelaiInterventionRepository delaiInterventionRepository,
        DelaiInterventionQueryService delaiInterventionQueryService
    ) {
        this.delaiInterventionService = delaiInterventionService;
        this.delaiInterventionRepository = delaiInterventionRepository;
        this.delaiInterventionQueryService = delaiInterventionQueryService;
    }

    /**
     * {@code POST  /delai-interventions} : Create a new delaiIntervention.
     *
     * @param delaiInterventionDTO the delaiInterventionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new delaiInterventionDTO, or with status {@code 400 (Bad Request)} if the delaiIntervention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DelaiInterventionDTO> createDelaiIntervention(@Valid @RequestBody DelaiInterventionDTO delaiInterventionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DelaiIntervention : {}", delaiInterventionDTO);
        if (delaiInterventionDTO.getId() != null) {
            throw new BadRequestAlertException("A new delaiIntervention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        delaiInterventionDTO = delaiInterventionService.save(delaiInterventionDTO);
        return ResponseEntity.created(new URI("/api/delai-interventions/" + delaiInterventionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, delaiInterventionDTO.getId().toString()))
            .body(delaiInterventionDTO);
    }

    /**
     * {@code PUT  /delai-interventions/:id} : Updates an existing delaiIntervention.
     *
     * @param id the id of the delaiInterventionDTO to save.
     * @param delaiInterventionDTO the delaiInterventionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated delaiInterventionDTO,
     * or with status {@code 400 (Bad Request)} if the delaiInterventionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the delaiInterventionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DelaiInterventionDTO> updateDelaiIntervention(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DelaiInterventionDTO delaiInterventionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DelaiIntervention : {}, {}", id, delaiInterventionDTO);
        if (delaiInterventionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, delaiInterventionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!delaiInterventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        delaiInterventionDTO = delaiInterventionService.update(delaiInterventionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, delaiInterventionDTO.getId().toString()))
            .body(delaiInterventionDTO);
    }

    /**
     * {@code PATCH  /delai-interventions/:id} : Partial updates given fields of an existing delaiIntervention, field will ignore if it is null
     *
     * @param id the id of the delaiInterventionDTO to save.
     * @param delaiInterventionDTO the delaiInterventionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated delaiInterventionDTO,
     * or with status {@code 400 (Bad Request)} if the delaiInterventionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the delaiInterventionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the delaiInterventionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DelaiInterventionDTO> partialUpdateDelaiIntervention(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DelaiInterventionDTO delaiInterventionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DelaiIntervention partially : {}, {}", id, delaiInterventionDTO);
        if (delaiInterventionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, delaiInterventionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!delaiInterventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DelaiInterventionDTO> result = delaiInterventionService.partialUpdate(delaiInterventionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, delaiInterventionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /delai-interventions} : get all the delaiInterventions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of delaiInterventions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DelaiInterventionDTO>> getAllDelaiInterventions(
        DelaiInterventionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DelaiInterventions by criteria: {}", criteria);

        Page<DelaiInterventionDTO> page = delaiInterventionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /delai-interventions/count} : count all the delaiInterventions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDelaiInterventions(DelaiInterventionCriteria criteria) {
        LOG.debug("REST request to count DelaiInterventions by criteria: {}", criteria);
        return ResponseEntity.ok().body(delaiInterventionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /delai-interventions/:id} : get the "id" delaiIntervention.
     *
     * @param id the id of the delaiInterventionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the delaiInterventionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DelaiInterventionDTO> getDelaiIntervention(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DelaiIntervention : {}", id);
        Optional<DelaiInterventionDTO> delaiInterventionDTO = delaiInterventionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(delaiInterventionDTO);
    }

    /**
     * {@code DELETE  /delai-interventions/:id} : delete the "id" delaiIntervention.
     *
     * @param id the id of the delaiInterventionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelaiIntervention(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DelaiIntervention : {}", id);
        delaiInterventionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
