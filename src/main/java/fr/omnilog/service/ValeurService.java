package fr.omnilog.service;

import fr.omnilog.service.dto.ValeurDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.omnilog.domain.Valeur}.
 */
public interface ValeurService {
    /**
     * Save a valeur.
     *
     * @param valeurDTO the entity to save.
     * @return the persisted entity.
     */
    ValeurDTO save(ValeurDTO valeurDTO);

    /**
     * Updates a valeur.
     *
     * @param valeurDTO the entity to update.
     * @return the persisted entity.
     */
    ValeurDTO update(ValeurDTO valeurDTO);

    /**
     * Partially updates a valeur.
     *
     * @param valeurDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ValeurDTO> partialUpdate(ValeurDTO valeurDTO);

    /**
     * Get all the valeurs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ValeurDTO> findAll(Pageable pageable);

    /**
     * Get the "id" valeur.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ValeurDTO> findOne(Long id);

    /**
     * Delete the "id" valeur.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the valeur corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ValeurDTO> search(String query, Pageable pageable);
}
