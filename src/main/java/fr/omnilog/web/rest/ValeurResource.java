package fr.omnilog.web.rest;

import fr.omnilog.domain.Valeur;
import fr.omnilog.repository.ValeurRepository;
import fr.omnilog.repository.search.ValeurSearchRepository;
import fr.omnilog.web.rest.errors.BadRequestAlertException;
import fr.omnilog.web.rest.errors.ElasticsearchExceptionMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.omnilog.domain.Valeur}.
 */
@RestController
@RequestMapping("/api/valeurs")
@Transactional
public class ValeurResource {

    private final Logger log = LoggerFactory.getLogger(ValeurResource.class);

    private static final String ENTITY_NAME = "valeur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValeurRepository valeurRepository;

    private final ValeurSearchRepository valeurSearchRepository;

    public ValeurResource(ValeurRepository valeurRepository, ValeurSearchRepository valeurSearchRepository) {
        this.valeurRepository = valeurRepository;
        this.valeurSearchRepository = valeurSearchRepository;
    }

    /**
     * {@code POST  /valeurs} : Create a new valeur.
     *
     * @param valeur the valeur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new valeur, or with status {@code 400 (Bad Request)} if the valeur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Valeur> createValeur(@RequestBody Valeur valeur) throws URISyntaxException {
        log.debug("REST request to save Valeur : {}", valeur);
        if (valeur.getId() != null) {
            throw new BadRequestAlertException("A new valeur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Valeur result = valeurRepository.save(valeur);
        valeurSearchRepository.index(result);
        return ResponseEntity
            .created(new URI("/api/valeurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /valeurs/:id} : Updates an existing valeur.
     *
     * @param id the id of the valeur to save.
     * @param valeur the valeur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated valeur,
     * or with status {@code 400 (Bad Request)} if the valeur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the valeur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Valeur> updateValeur(@PathVariable(value = "id", required = false) final Long id, @RequestBody Valeur valeur)
        throws URISyntaxException {
        log.debug("REST request to update Valeur : {}, {}", id, valeur);
        if (valeur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, valeur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!valeurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Valeur result = valeurRepository.save(valeur);
        valeurSearchRepository.index(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, valeur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /valeurs/:id} : Partial updates given fields of an existing valeur, field will ignore if it is null
     *
     * @param id the id of the valeur to save.
     * @param valeur the valeur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated valeur,
     * or with status {@code 400 (Bad Request)} if the valeur is not valid,
     * or with status {@code 404 (Not Found)} if the valeur is not found,
     * or with status {@code 500 (Internal Server Error)} if the valeur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Valeur> partialUpdateValeur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Valeur valeur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Valeur partially : {}, {}", id, valeur);
        if (valeur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, valeur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!valeurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Valeur> result = valeurRepository
            .findById(valeur.getId())
            .map(existingValeur -> {
                if (valeur.getCode() != null) {
                    existingValeur.setCode(valeur.getCode());
                }
                if (valeur.getOrdre() != null) {
                    existingValeur.setOrdre(valeur.getOrdre());
                }
                if (valeur.getLibelle() != null) {
                    existingValeur.setLibelle(valeur.getLibelle());
                }
                if (valeur.getAbreviation() != null) {
                    existingValeur.setAbreviation(valeur.getAbreviation());
                }
                if (valeur.getDescription() != null) {
                    existingValeur.setDescription(valeur.getDescription());
                }

                return existingValeur;
            })
            .map(valeurRepository::save)
            .map(savedValeur -> {
                valeurSearchRepository.index(savedValeur);
                return savedValeur;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, valeur.getId().toString())
        );
    }

    /**
     * {@code GET  /valeurs} : get all the valeurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of valeurs in body.
     */
    @GetMapping("")
    public List<Valeur> getAllValeurs() {
        log.debug("REST request to get all Valeurs");
        return valeurRepository.findAll();
    }

    /**
     * {@code GET  /valeurs/:id} : get the "id" valeur.
     *
     * @param id the id of the valeur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the valeur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Valeur> getValeur(@PathVariable Long id) {
        log.debug("REST request to get Valeur : {}", id);
        Optional<Valeur> valeur = valeurRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(valeur);
    }

    /**
     * {@code DELETE  /valeurs/:id} : delete the "id" valeur.
     *
     * @param id the id of the valeur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteValeur(@PathVariable Long id) {
        log.debug("REST request to delete Valeur : {}", id);
        valeurRepository.deleteById(id);
        valeurSearchRepository.deleteFromIndexById(id);
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
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Valeur> searchValeurs(@RequestParam String query) {
        log.debug("REST request to search Valeurs for query {}", query);
        try {
            return StreamSupport.stream(valeurSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
