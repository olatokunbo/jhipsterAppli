package fr.omnilog.service.mapper;

import fr.omnilog.domain.UniteAdministrative;
import fr.omnilog.domain.Valeur;
import fr.omnilog.service.dto.UniteAdministrativeDTO;
import fr.omnilog.service.dto.ValeurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UniteAdministrative} and its DTO {@link UniteAdministrativeDTO}.
 */
@Mapper(componentModel = "spring")
public interface UniteAdministrativeMapper extends EntityMapper<UniteAdministrativeDTO, UniteAdministrative> {
    @Mapping(target = "typeUniteAdministrative", source = "typeUniteAdministrative", qualifiedByName = "valeurId")
    @Mapping(target = "uniteAdministrativeParent", source = "uniteAdministrativeParent", qualifiedByName = "uniteAdministrativeId")
    UniteAdministrativeDTO toDto(UniteAdministrative s);

    @Named("uniteAdministrativeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UniteAdministrativeDTO toDtoUniteAdministrativeId(UniteAdministrative uniteAdministrative);

    @Named("valeurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ValeurDTO toDtoValeurId(Valeur valeur);
}
