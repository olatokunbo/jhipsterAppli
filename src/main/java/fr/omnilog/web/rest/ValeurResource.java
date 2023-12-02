package fr.omnilog.web.rest;

import fr.omnilog.repository.ValeurRepository;
import fr.omnilog.service.ValeurService;
import fr.omnilog.service.dto.ValeurDTO;
import fr.omnilog.web.rest.errors.BadRequestAlertException;
import fr.omnilog.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link fr.omnilog.domain.Valeur}.
 */
@RestController
@RequestMapping("/api/valeurs")
public class ValeurResource {

    private final Logger log = LoggerFactory.getLogger(ValeurResource.class);

    private static final String ENTITY_NAME = "valeur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValeurService valeurService;

    private final ValeurRepository valeurRepository;

    public ValeurResource(ValeurService valeurService, ValeurRepository valeurRepository) {
        this.valeurService = valeurService;
        this.valeurRepository = valeurRepository;
    }

    /**
     * {@code POST  /valeurs} : Create a new valeur.
     *
     * @param valeurDTO the valeurDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new valeurDTO, or with status {@code 400 (Bad Request)} if the valeur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ValeurDTO> createValeur(@RequestBody ValeurDTO valeurDTO) throws URISyntaxException {
        log.debug("REST request to save Valeur : {}", valeurDTO);
        if (valeurDTO.getId() != null) {
            throw new BadRequestAlertException("A new valeur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ValeurDTO result = valeurService.save(valeurDTO);
        return ResponseEntity
            .created(new URI("/api/valeurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /valeurs/:id} : Updates an existing valeur.
     *
     * @param id the id of the valeurDTO to save.
     * @param valeurDTO the valeurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated valeurDTO,
     * or with status {@code 400 (Bad Request)} if the valeurDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the valeurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ValeurDTO> updateValeur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ValeurDTO valeurDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Valeur : {}, {}", id, valeurDTO);
        if (valeurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, valeurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!valeurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ValeurDTO result = valeurService.update(valeurDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, valeurDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /valeurs/:id} : Partial updates given fields of an existing valeur, field will ignore if it is null
     *
     * @param id the id of the valeurDTO to save.
     * @param valeurDTO the valeurDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated valeurDTO,
     * or with status {@code 400 (Bad Request)} if the valeurDTO is not valid,
     * or with status {@code 404 (Not Found)} if the valeurDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the valeurDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ValeurDTO> partialUpdateValeur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ValeurDTO valeurDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Valeur partially : {}, {}", id, valeurDTO);
        if (valeurDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, valeurDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!valeurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ValeurDTO> result = valeurService.partialUpdate(valeurDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, valeurDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /valeurs} : get all the valeurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of valeurs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ValeurDTO>> getAllValeurs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Valeurs");
        Page<ValeurDTO> page = valeurService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /valeurs/:id} : get the "id" valeur.
     *
     * @param id the id of the valeurDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the valeurDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ValeurDTO> getValeur(@PathVariable Long id) {
        log.debug("REST request to get Valeur : {}", id);
        Optional<ValeurDTO> valeurDTO = valeurService.findOne(id);
        return ResponseUtil.wrapOrNotFound(valeurDTO);
    }

    /**
     * {@code DELETE  /valeurs/:id} : delete the "id" valeur.
     *
     * @param id the id of the valeurDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteValeur(@PathVariable Long id) {
        log.debug("REST request to delete Valeur : {}", id);
        valeurService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /valeurs/_search?query=:query} : search for the valeur corresponding
     * to the query.
     *
     * @param query the query of the valeur search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ValeurDTO>> searchValeurs(
        @RequestParam String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Valeurs for query {}", query);
        try {
            Page<ValeurDTO> page = valeurService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
