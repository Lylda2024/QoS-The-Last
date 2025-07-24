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

    /**
     * POST /api/degradations : Create a new degradation.
     */
    @PostMapping("")
    public ResponseEntity<DegradationDTO> createDegradation(@Valid @RequestBody DegradationDTO degradationDTO) throws URISyntaxException {
        LOG.debug("REST request to save Degradation : {}", degradationDTO);
        if (degradationDTO.getId() != null) {
            throw new BadRequestAlertException("A new degradation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DegradationDTO result = degradationService.save(degradationDTO);
        return ResponseEntity.created(new URI("/api/degradations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET /api/degradations : get all degradations.
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
     * GET /api/degradations/{id} : get the "id" degradation.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DegradationDTO> getDegradation(@PathVariable Long id) {
        LOG.debug("REST request to get Degradation : {}", id);
        Optional<DegradationDTO> degradationDTO = degradationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(degradationDTO);
    }

    /**
     * PUT /api/degradations/{id} : updates an existing degradation.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DegradationDTO> updateDegradation(@PathVariable Long id, @Valid @RequestBody DegradationDTO dto) {
        LOG.debug("REST request to update Degradation : {}, {}", id, dto);
        dto.setId(id);
        return ResponseEntity.ok(degradationService.update(dto));
    }

    /**
     * GET /api/degradations/{id}/delais : get all DelaiInterventions for a degradation.
     */
    @GetMapping("/{id}/delais")
    public ResponseEntity<List<DelaiInterventionDTO>> getDelaisByDegradation(@PathVariable Long id) {
        List<DelaiInterventionDTO> delais = degradationService.findDelaisByDegradationId(id);
        return ResponseEntity.ok(delais);
    }

    /**
     * PUT /api/degradations/{id}/delais : update a DelaiIntervention for a degradation.
     */
    @PutMapping("/{id}/delais")
    public ResponseEntity<DelaiInterventionDTO> updateDelai(@PathVariable Long id, @Valid @RequestBody DelaiInterventionDTO delaiDTO) {
        LOG.debug("REST request to update Delai : {}", delaiDTO);
        DelaiInterventionDTO updatedDelai = degradationService.updateDelai(id, delaiDTO);
        return ResponseEntity.ok(updatedDelai);
    }

    /**
     * POST /api/degradations/{id}/delais : add a new DelaiIntervention for a degradation.
     */
    @PostMapping("/{id}/delais")
    public ResponseEntity<DelaiInterventionDTO> addDelai(@PathVariable Long id, @Valid @RequestBody DelaiInterventionDTO delaiDTO) {
        LOG.debug("REST request to add Delai : {}", delaiDTO);
        DelaiInterventionDTO createdDelai = degradationService.addDelai(id, delaiDTO);
        return ResponseEntity.ok(createdDelai);
    }
}
