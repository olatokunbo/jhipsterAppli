package fr.omnilog.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.omnilog.domain.Valeur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ValeurDTO implements Serializable {

    private Long id;

    private String code;

    private Integer ordre;

    private String libelle;

    private String abreviation;

    private String description;

    private ValeurDTO valeurParent;

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

    public String getAbreviation() {
        return abreviation;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ValeurDTO getValeurParent() {
        return valeurParent;
    }

    public void setValeurParent(ValeurDTO valeurParent) {
        this.valeurParent = valeurParent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ValeurDTO)) {
            return false;
        }

        ValeurDTO valeurDTO = (ValeurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, valeurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ValeurDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", ordre=" + getOrdre() +
            ", libelle='" + getLibelle() + "'" +
            ", abreviation='" + getAbreviation() + "'" +
            ", description='" + getDescription() + "'" +
            ", valeurParent=" + getValeurParent() +
            "}";
    }
}
