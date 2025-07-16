package com.orange.qos.web.rest;

import com.orange.qos.repository.TypeUtilisateurRepository;
import com.orange.qos.service.TypeUtilisateurQueryService;
import com.orange.qos.service.TypeUtilisateurService;
import com.orange.qos.service.criteria.TypeUtilisateurCriteria;
import com.orange.qos.service.dto.TypeUtilisateurDTO;
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
 * REST controller for managing {@link com.orange.qos.domain.TypeUtilisateur}.
 */
@RestController
@RequestMapping("/api/type-utilisateurs")
public class TypeUtilisateurResource {

    private static final Logger LOG = LoggerFactory.getLogger(TypeUtilisateurResource.class);

    private static final String ENTITY_NAME = "typeUtilisateur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeUtilisateurService typeUtilisateurService;

    private final TypeUtilisateurRepository typeUtilisateurRepository;

    private final TypeUtilisateurQueryService typeUtilisateurQueryService;

    public TypeUtilisateurResource(
        TypeUtilisateurService typeUtilisateurService,
        TypeUtilisateurRepository typeUtilisateurRepository,
        TypeUtilisateurQueryService typeUtilisateurQueryService
    ) {
        this.typeUtilisateurService = typeUtilisateurService;
        this.typeUtilisateurRepository = typeUtilisateurRepository;
        this.typeUtilisateurQueryService = typeUtilisateurQueryService;
    }

    /**
     * {@code POST  /type-utilisateurs} : Create a new typeUtilisateur.
     *
     * @param typeUtilisateurDTO the typeUtilisateurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeUtilisateurDTO, or with status {@code 400 (Bad Request)} if the typeUtilisateur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TypeUtilisateurDTO> createTypeUtilisateur(@Valid @RequestBody TypeUtilisateurDTO typeUtilisateurDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TypeUtilisateur : {}", typeUtilisateurDTO);
        if (typeUtilisateurDTO.getId() != null) {
            throw new BadRequestAlertException("A new typeUtilisateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        typeUtilisateurDTO = typeUtilisateurService.save(typeUtilisateurDTO);
        return ResponseEntity.created(new URI("/api/type-utilisateurs/" + typeUtilisateurDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, typeUtilisateurDTO.getId().toString()))
            .body(typeUtilisateurDTO);
    }

    /**
     * {@code PUT  /type-utilisateurs/:id} : Updates an existing typeUtilisateur.
     *
     * @param id the id of the typeUtilisateurDTO to save.
     * @param typeUtilisateurDTO the typeUtilisateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeUtilisateurDTO,
     * or with status {@code 400 (Bad Request)} if the typeUtilisateurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeUtilisateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TypeUtilisateurDTO> updateTypeUtilisateur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TypeUtilisateurDTO typeUtilisateurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TypeUtilisateur : {}, {}", id, typeUtilisateurDTO);
        if (typeUtilisateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeUtilisateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeUtilisateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        typeUtilisateurDTO = typeUtilisateurService.update(typeUtilisateurDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeUtilisateurDTO.getId().toString()))
            .body(typeUtilisateurDTO);
    }

    /**
     * {@code PATCH  /type-utilisateurs/:id} : Partial updates given fields of an existing typeUtilisateur, field will ignore if it is null
     *
     * @param id the id of the typeUtilisateurDTO to save.
     * @param typeUtilisateurDTO the typeUtilisateurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeUtilisateurDTO,
     * or with status {@code 400 (Bad Request)} if the typeUtilisateurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the typeUtilisateurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeUtilisateurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeUtilisateurDTO> partialUpdateTypeUtilisateur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TypeUtilisateurDTO typeUtilisateurDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TypeUtilisateur partially : {}, {}", id, typeUtilisateurDTO);
        if (typeUtilisateurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeUtilisateurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeUtilisateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeUtilisateurDTO> result = typeUtilisateurService.partialUpdate(typeUtilisateurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeUtilisateurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /type-utilisateurs} : get all the typeUtilisateurs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeUtilisateurs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TypeUtilisateurDTO>> getAllTypeUtilisateurs(
        TypeUtilisateurCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TypeUtilisateurs by criteria: {}", criteria);

        Page<TypeUtilisateurDTO> page = typeUtilisateurQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-utilisateurs/count} : count all the typeUtilisateurs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTypeUtilisateurs(TypeUtilisateurCriteria criteria) {
        LOG.debug("REST request to count TypeUtilisateurs by criteria: {}", criteria);
        return ResponseEntity.ok().body(typeUtilisateurQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /type-utilisateurs/:id} : get the "id" typeUtilisateur.
     *
     * @param id the id of the typeUtilisateurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeUtilisateurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TypeUtilisateurDTO> getTypeUtilisateur(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TypeUtilisateur : {}", id);
        Optional<TypeUtilisateurDTO> typeUtilisateurDTO = typeUtilisateurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeUtilisateurDTO);
    }

    /**
     * {@code DELETE  /type-utilisateurs/:id} : delete the "id" typeUtilisateur.
     *
     * @param id the id of the typeUtilisateurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTypeUtilisateur(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TypeUtilisateur : {}", id);
        typeUtilisateurService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
