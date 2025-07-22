package com.orange.qos.web.rest;

import com.orange.qos.domain.DelaiIntervention;
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
import java.util.stream.Collectors;
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

    @PostMapping
    public ResponseEntity<DelaiInterventionDTO> createDelaiIntervention(@Valid @RequestBody DelaiInterventionDTO dto)
        throws URISyntaxException {
        LOG.debug("REST request to save DelaiIntervention : {}", dto);
        if (dto.getId() != null) {
            throw new BadRequestAlertException("A new delaiIntervention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DelaiInterventionDTO result = delaiInterventionService.save(dto);
        return ResponseEntity.created(new URI("/api/delai-interventions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DelaiInterventionDTO> updateDelaiIntervention(
        @PathVariable Long id,
        @Valid @RequestBody DelaiInterventionDTO dto
    ) throws URISyntaxException {
        LOG.debug("REST request to update DelaiIntervention : {}, {}", id, dto);
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!delaiInterventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        DelaiInterventionDTO result = delaiInterventionService.update(dto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DelaiInterventionDTO> partialUpdateDelaiIntervention(
        @PathVariable Long id,
        @NotNull @RequestBody DelaiInterventionDTO dto
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DelaiIntervention : {}, {}", id, dto);
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dto.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!delaiInterventionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        Optional<DelaiInterventionDTO> result = delaiInterventionService.partialUpdate(dto);
        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dto.getId().toString())
        );
    }

    @GetMapping
    public ResponseEntity<List<DelaiInterventionDTO>> getAllDelaiInterventions(
        DelaiInterventionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get DelaiInterventions by criteria: {}", criteria);
        Page<DelaiInterventionDTO> page = delaiInterventionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countDelaiInterventions(DelaiInterventionCriteria criteria) {
        LOG.debug("REST request to count DelaiInterventions by criteria: {}", criteria);
        return ResponseEntity.ok().body(delaiInterventionQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DelaiInterventionDTO> getDelaiIntervention(@PathVariable Long id) {
        LOG.debug("REST request to get DelaiIntervention : {}", id);
        Optional<DelaiInterventionDTO> dto = delaiInterventionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelaiIntervention(@PathVariable Long id) {
        LOG.debug("REST request to delete DelaiIntervention : {}", id);
        delaiInterventionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    // Méthode clé : récupère tous les délais d’une dégradation avec couleur calculée dans le service
    @GetMapping("/delais/degradation/{degradationId}")
    public ResponseEntity<List<DelaiInterventionDTO>> getDelaisByDegradationId(@PathVariable Long degradationId) {
        LOG.debug("REST request to get DelaiIntervention by degradation id : {}", degradationId);
        List<DelaiInterventionDTO> delais = delaiInterventionService.findDelaisByDegradationId(degradationId);
        return ResponseEntity.ok(delais);
    }
}
