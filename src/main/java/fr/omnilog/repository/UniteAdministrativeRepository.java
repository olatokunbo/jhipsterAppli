package fr.omnilog.repository;

import fr.omnilog.domain.UniteAdministrative;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UniteAdministrative entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UniteAdministrativeRepository extends JpaRepository<UniteAdministrative, Long> {}
