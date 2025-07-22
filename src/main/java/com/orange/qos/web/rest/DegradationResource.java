package com.orange.qos.web.rest;

import com.orange.qos.repository.DegradationRepository;
import com.orange.qos.service.DegradationQueryService;
import com.orange.qos.service.DegradationService;
import com.orange.qos.service.criteria.DegradationCriteria;
import com.orange.qos.service.dto.DegradationDTO;
import com.orange.qos.service.dto.DelaiInterventionDTO;
import com.orange.qos.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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

    /* ---------- CRUD endpoints standard ---------- */

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

    /* ---------- Endpoint principal (carte/Angular) ---------- */

    /**
     * GET /api/degradations/all-with-site : toutes les dégradations
     * avec leurs sites (latitude/longitude) en une seule requête.
     */
    @GetMapping("/all-with-site")
    public ResponseEntity<List<DegradationDTO>> getAllWithSite() {
        LOG.debug("REST request to get all Degradations with Site");
        return ResponseEntity.ok(degradationService.findAllWithSite());
    }

    /* ---------- Endpoint JHipster paginé ---------- */

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

    @GetMapping("/{id}/delais")
    public ResponseEntity<List<DelaiInterventionDTO>> getDelaisByDegradation(@PathVariable Long id) {
        List<DelaiInterventionDTO> delais = degradationService.findDelaisByDegradationId(id);
        return ResponseEntity.ok(delais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DegradationDTO> getDegradation(@PathVariable Long id) {
        LOG.debug("REST request to get Degradation : {}", id);
        Optional<DegradationDTO> degradationDTO = degradationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(degradationDTO);
    }
}
