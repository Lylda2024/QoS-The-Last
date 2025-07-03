package com.orange.qos.web.rest;

import com.orange.qos.repository.HistoriqueRepository;
import com.orange.qos.service.HistoriqueQueryService;
import com.orange.qos.service.HistoriqueService;
import com.orange.qos.service.criteria.HistoriqueCriteria;
import com.orange.qos.service.dto.HistoriqueDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.orange.qos.domain.Historique}.
 */
@RestController
@RequestMapping("/api/historiques")
public class HistoriqueResource {

    private static final Logger LOG = LoggerFactory.getLogger(HistoriqueResource.class);

    private static final String ENTITY_NAME = "historique";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoriqueService historiqueService;

    private final HistoriqueRepository historiqueRepository;

    private final HistoriqueQueryService historiqueQueryService;

    public HistoriqueResource(
        HistoriqueService historiqueService,
        HistoriqueRepository historiqueRepository,
        HistoriqueQueryService historiqueQueryService
    ) {
        this.historiqueService = historiqueService;
        this.historiqueRepository = historiqueRepository;
        this.historiqueQueryService = historiqueQueryService;
    }

    /**
     * {@code POST  /historiques} : Create a new historique.
     *
     * @param historiqueDTO the historiqueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historiqueDTO, or with status {@code 400 (Bad Request)} if the historique has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HistoriqueDTO> createHistorique(@Valid @RequestBody HistoriqueDTO historiqueDTO) throws URISyntaxException {
        LOG.debug("REST request to save Historique : {}", historiqueDTO);
        if (historiqueDTO.getId() != null) {
            throw new BadRequestAlertException("A new historique cannot already have an ID", ENTITY_NAME, "idexists");
        }
        historiqueDTO = historiqueService.save(historiqueDTO);
        return ResponseEntity.created(new URI("/api/historiques/" + historiqueDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, historiqueDTO.getId().toString()))
            .body(historiqueDTO);
    }

    /**
     * {@code PUT  /historiques/:id} : Updates an existing historique.
     *
     * @param id the id of the historiqueDTO to save.
     * @param historiqueDTO the historiqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiqueDTO,
     * or with status {@code 400 (Bad Request)} if the historiqueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historiqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HistoriqueDTO> updateHistorique(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HistoriqueDTO historiqueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Historique : {}, {}", id, historiqueDTO);
        if (historiqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historiqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        historiqueDTO = historiqueService.update(historiqueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiqueDTO.getId().toString()))
            .body(historiqueDTO);
    }

    /**
     * {@code PATCH  /historiques/:id} : Partial updates given fields of an existing historique, field will ignore if it is null
     *
     * @param id the id of the historiqueDTO to save.
     * @param historiqueDTO the historiqueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historiqueDTO,
     * or with status {@code 400 (Bad Request)} if the historiqueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the historiqueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the historiqueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HistoriqueDTO> partialUpdateHistorique(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HistoriqueDTO historiqueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Historique partially : {}, {}", id, historiqueDTO);
        if (historiqueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historiqueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historiqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HistoriqueDTO> result = historiqueService.partialUpdate(historiqueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historiqueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /historiques} : get all the historiques.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historiques in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HistoriqueDTO>> getAllHistoriques(HistoriqueCriteria criteria) {
        LOG.debug("REST request to get Historiques by criteria: {}", criteria);

        List<HistoriqueDTO> entityList = historiqueQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /historiques/count} : count all the historiques.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHistoriques(HistoriqueCriteria criteria) {
        LOG.debug("REST request to count Historiques by criteria: {}", criteria);
        return ResponseEntity.ok().body(historiqueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /historiques/:id} : get the "id" historique.
     *
     * @param id the id of the historiqueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historiqueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HistoriqueDTO> getHistorique(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Historique : {}", id);
        Optional<HistoriqueDTO> historiqueDTO = historiqueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historiqueDTO);
    }

    /**
     * {@code DELETE  /historiques/:id} : delete the "id" historique.
     *
     * @param id the id of the historiqueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistorique(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Historique : {}", id);
        historiqueService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
