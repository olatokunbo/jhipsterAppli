package fr.omnilog.web.rest;

import fr.omnilog.repository.UniteAdministrativeRepository;
import fr.omnilog.service.UniteAdministrativeService;
import fr.omnilog.service.dto.UniteAdministrativeDTO;
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
 * REST controller for managing {@link fr.omnilog.domain.UniteAdministrative}.
 */
@RestController
@RequestMapping("/api/unite-administratives")
public class UniteAdministrativeResource {

    private final Logger log = LoggerFactory.getLogger(UniteAdministrativeResource.class);

    private static final String ENTITY_NAME = "uniteAdministrative";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UniteAdministrativeService uniteAdministrativeService;

    private final UniteAdministrativeRepository uniteAdministrativeRepository;

    public UniteAdministrativeResource(
        UniteAdministrativeService uniteAdministrativeService,
        UniteAdministrativeRepository uniteAdministrativeRepository
    ) {
        this.uniteAdministrativeService = uniteAdministrativeService;
        this.uniteAdministrativeRepository = uniteAdministrativeRepository;
    }

    /**
     * {@code POST  /unite-administratives} : Create a new uniteAdministrative.
     *
     * @param uniteAdministrativeDTO the uniteAdministrativeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uniteAdministrativeDTO, or with status {@code 400 (Bad Request)} if the uniteAdministrative has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UniteAdministrativeDTO> createUniteAdministrative(@RequestBody UniteAdministrativeDTO uniteAdministrativeDTO)
        throws URISyntaxException {
        log.debug("REST request to save UniteAdministrative : {}", uniteAdministrativeDTO);
        if (uniteAdministrativeDTO.getId() != null) {
            throw new BadRequestAlertException("A new uniteAdministrative cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UniteAdministrativeDTO result = uniteAdministrativeService.save(uniteAdministrativeDTO);
        return ResponseEntity
            .created(new URI("/api/unite-administratives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /unite-administratives/:id} : Updates an existing uniteAdministrative.
     *
     * @param id the id of the uniteAdministrativeDTO to save.
     * @param uniteAdministrativeDTO the uniteAdministrativeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uniteAdministrativeDTO,
     * or with status {@code 400 (Bad Request)} if the uniteAdministrativeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uniteAdministrativeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UniteAdministrativeDTO> updateUniteAdministrative(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UniteAdministrativeDTO uniteAdministrativeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UniteAdministrative : {}, {}", id, uniteAdministrativeDTO);
        if (uniteAdministrativeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uniteAdministrativeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uniteAdministrativeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UniteAdministrativeDTO result = uniteAdministrativeService.update(uniteAdministrativeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uniteAdministrativeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /unite-administratives/:id} : Partial updates given fields of an existing uniteAdministrative, field will ignore if it is null
     *
     * @param id the id of the uniteAdministrativeDTO to save.
     * @param uniteAdministrativeDTO the uniteAdministrativeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uniteAdministrativeDTO,
     * or with status {@code 400 (Bad Request)} if the uniteAdministrativeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the uniteAdministrativeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the uniteAdministrativeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UniteAdministrativeDTO> partialUpdateUniteAdministrative(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UniteAdministrativeDTO uniteAdministrativeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UniteAdministrative partially : {}, {}", id, uniteAdministrativeDTO);
        if (uniteAdministrativeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uniteAdministrativeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uniteAdministrativeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UniteAdministrativeDTO> result = uniteAdministrativeService.partialUpdate(uniteAdministrativeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, uniteAdministrativeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /unite-administratives} : get all the uniteAdministratives.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uniteAdministratives in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UniteAdministrativeDTO>> getAllUniteAdministratives(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of UniteAdministratives");
        Page<UniteAdministrativeDTO> page = uniteAdministrativeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /unite-administratives/:id} : get the "id" uniteAdministrative.
     *
     * @param id the id of the uniteAdministrativeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uniteAdministrativeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UniteAdministrativeDTO> getUniteAdministrative(@PathVariable Long id) {
        log.debug("REST request to get UniteAdministrative : {}", id);
        Optional<UniteAdministrativeDTO> uniteAdministrativeDTO = uniteAdministrativeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(uniteAdministrativeDTO);
    }

    /**
     * {@code DELETE  /unite-administratives/:id} : delete the "id" uniteAdministrative.
     *
     * @param id the id of the uniteAdministrativeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniteAdministrative(@PathVariable Long id) {
        log.debug("REST request to delete UniteAdministrative : {}", id);
        uniteAdministrativeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /unite-administratives/_search?query=:query} : search for the uniteAdministrative corresponding
     * to the query.
     *
     * @param query the query of the uniteAdministrative search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<UniteAdministrativeDTO>> searchUniteAdministratives(
        @RequestParam String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of UniteAdministratives for query {}", query);
        try {
            Page<UniteAdministrativeDTO> page = uniteAdministrativeService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
