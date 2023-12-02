package fr.omnilog.service.mapper;

import fr.omnilog.domain.Valeur;
import fr.omnilog.service.dto.ValeurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Valeur} and its DTO {@link ValeurDTO}.
 */
@Mapper(componentModel = "spring")
public interface ValeurMapper extends EntityMapper<ValeurDTO, Valeur> {
    @Mapping(target = "valeurParent", source = "valeurParent", qualifiedByName = "valeurId")
    ValeurDTO toDto(Valeur s);

    @Named("valeurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ValeurDTO toDtoValeurId(Valeur valeur);
}
