package com.orange.qos.web.rest;

import com.orange.qos.repository.DegradationRepository;
import com.orange.qos.service.DegradationQueryService;
import com.orange.qos.service.DegradationService;
import com.orange.qos.service.criteria.DegradationCriteria;
import com.orange.qos.service.dto.DegradationDTO;
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
 * REST controller for managing {@link com.orange.qos.domain.Degradation}.
 */
@RestController
@RequestMapping("/api/degradations")
public class DegradationResource {

    private static final Logger LOG = LoggerFactory.getLogger(DegradationResource.class);

    private static final String ENTITY_NAME = "degradation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DegradationService degradationService;

    private final DegradationRepository degradationRepository;

    private final DegradationQueryService degradationQueryService;

    public DegradationResource(
        DegradationService degradationService,
        DegradationRepository degradationRepository,
        DegradationQueryService degradationQueryService
    ) {
        this.degradationService = degradationService;
        this.degradationRepository = degradationRepository;
        this.degradationQueryService = degradationQueryService;
    }

    /**
     * {@code POST  /degradations} : Create a new degradation.
     *
     * @param degradationDTO the degradationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new degradationDTO, or with status {@code 400 (Bad Request)} if the degradation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DegradationDTO> createDegradation(@Valid @RequestBody DegradationDTO degradationDTO) throws URISyntaxException {
        LOG.debug("REST request to save Degradation : {}", degradationDTO);
        if (degradationDTO.getId() != null) {
            throw new BadRequestAlertException("A new degradation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        degradationDTO = degradationService.save(degradationDTO);
        return ResponseEntity.created(new URI("/api/degradations/" + degradationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, degradationDTO.getId().toString()))
            .body(degradationDTO);
    }

    /**
     * {@code PUT  /degradations/:id} : Updates an existing degradation.
     *
     * @param id the id of the degradationDTO to save.
     * @param degradationDTO the degradationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated degradationDTO,
     * or with status {@code 400 (Bad Request)} if the degradationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the degradationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DegradationDTO> updateDegradation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DegradationDTO degradationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Degradation : {}, {}", id, degradationDTO);
        if (degradationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, degradationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!degradationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        degradationDTO = degradationService.update(degradationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, degradationDTO.getId().toString()))
            .body(degradationDTO);
    }

    /**
     * {@code PATCH  /degradations/:id} : Partial updates given fields of an existing degradation, field will ignore if it is null
     *
     * @param id the id of the degradationDTO to save.
     * @param degradationDTO the degradationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated degradationDTO,
     * or with status {@code 400 (Bad Request)} if the degradationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the degradationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the degradationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DegradationDTO> partialUpdateDegradation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DegradationDTO degradationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Degradation partially : {}, {}", id, degradationDTO);
        if (degradationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, degradationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!degradationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DegradationDTO> result = degradationService.partialUpdate(degradationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, degradationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /degradations} : get all the degradations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of degradations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DegradationDTO>> getAllDegradations(
        DegradationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Degradations by criteria: {}", criteria);

        Page<DegradationDTO> page = degradationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /degradations/count} : count all the degradations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDegradations(DegradationCriteria criteria) {
        LOG.debug("REST request to count Degradations by criteria: {}", criteria);
        return ResponseEntity.ok().body(degradationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /degradations/:id} : get the "id" degradation.
     *
     * @param id the id of the degradationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the degradationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DegradationDTO> getDegradation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Degradation : {}", id);
        Optional<DegradationDTO> degradationDTO = degradationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(degradationDTO);
    }

    /**
     * {@code DELETE  /degradations/:id} : delete the "id" degradation.
     *
     * @param id the id of the degradationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDegradation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Degradation : {}", id);
        degradationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/degradations/{id}/delais")
    public ResponseEntity<List<DelaiInterventionDTO>> getDelaisByDegradation(@PathVariable Long id) {
        List<DelaiInterventionDTO> delais = degradationService.findDelaisByDegradationId(id);
        return ResponseEntity.ok(delais);
    }
}
