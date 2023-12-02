package fr.omnilog.repository;

import fr.omnilog.domain.Valeur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Valeur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValeurRepository extends JpaRepository<Valeur, Long> {}
