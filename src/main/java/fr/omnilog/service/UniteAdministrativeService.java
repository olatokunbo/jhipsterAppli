package fr.omnilog.service;

import fr.omnilog.service.dto.UniteAdministrativeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.omnilog.domain.UniteAdministrative}.
 */
public interface UniteAdministrativeService {
    /**
     * Save a uniteAdministrative.
     *
     * @param uniteAdministrativeDTO the entity to save.
     * @return the persisted entity.
     */
    UniteAdministrativeDTO save(UniteAdministrativeDTO uniteAdministrativeDTO);

    /**
     * Updates a uniteAdministrative.
     *
     * @param uniteAdministrativeDTO the entity to update.
     * @return the persisted entity.
     */
    UniteAdministrativeDTO update(UniteAdministrativeDTO uniteAdministrativeDTO);

    /**
     * Partially updates a uniteAdministrative.
     *
     * @param uniteAdministrativeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UniteAdministrativeDTO> partialUpdate(UniteAdministrativeDTO uniteAdministrativeDTO);

    /**
     * Get all the uniteAdministratives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UniteAdministrativeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" uniteAdministrative.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UniteAdministrativeDTO> findOne(Long id);

    /**
     * Delete the "id" uniteAdministrative.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the uniteAdministrative corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UniteAdministrativeDTO> search(String query, Pageable pageable);
}
