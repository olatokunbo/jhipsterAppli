package fr.omnilog.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.omnilog.domain.UniteAdministrative} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniteAdministrativeDTO implements Serializable {

    private Long id;

    private String code;

    private Integer ordre;

    private String libelle;

    private ValeurDTO typeUniteAdministrative;

    private UniteAdministrativeDTO uniteAdministrativeParent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public ValeurDTO getTypeUniteAdministrative() {
        return typeUniteAdministrative;
    }

    public void setTypeUniteAdministrative(ValeurDTO typeUniteAdministrative) {
        this.typeUniteAdministrative = typeUniteAdministrative;
    }

    public UniteAdministrativeDTO getUniteAdministrativeParent() {
        return uniteAdministrativeParent;
    }

    public void setUniteAdministrativeParent(UniteAdministrativeDTO uniteAdministrativeParent) {
        this.uniteAdministrativeParent = uniteAdministrativeParent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UniteAdministrativeDTO)) {
            return false;
        }

        UniteAdministrativeDTO uniteAdministrativeDTO = (UniteAdministrativeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, uniteAdministrativeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UniteAdministrativeDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", ordre=" + getOrdre() +
            ", libelle='" + getLibelle() + "'" +
            ", typeUniteAdministrative=" + getTypeUniteAdministrative() +
            ", uniteAdministrativeParent=" + getUniteAdministrativeParent() +
            "}";
    }
}
