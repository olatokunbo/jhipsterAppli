package fr.omnilog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UniteAdministrative.
 */
@Entity
@Table(name = "unite_administrative")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "uniteadministrative")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UniteAdministrative implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String code;

    @Column(name = "ordre")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer ordre;

    @Column(name = "libelle")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String libelle;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "uniteAdministrativeParent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(
        value = { "uniteAdministrativeListFils", "typeUniteAdministrative", "uniteAdministrativeParent" },
        allowSetters = true
    )
    private Set<UniteAdministrative> uniteAdministrativeListFils = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "valeurListFils", "valeurParent" }, allowSetters = true)
    private Valeur typeUniteAdministrative;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "uniteAdministrativeListFils", "typeUniteAdministrative", "uniteAdministrativeParent" },
        allowSetters = true
    )
    private UniteAdministrative uniteAdministrativeParent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UniteAdministrative id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public UniteAdministrative code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getOrdre() {
        return this.ordre;
    }

    public UniteAdministrative ordre(Integer ordre) {
        this.setOrdre(ordre);
        return this;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public UniteAdministrative libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Set<UniteAdministrative> getUniteAdministrativeListFils() {
        return this.uniteAdministrativeListFils;
    }

    public void setUniteAdministrativeListFils(Set<UniteAdministrative> uniteAdministratives) {
        if (this.uniteAdministrativeListFils != null) {
            this.uniteAdministrativeListFils.forEach(i -> i.setUniteAdministrativeParent(null));
        }
        if (uniteAdministratives != null) {
            uniteAdministratives.forEach(i -> i.setUniteAdministrativeParent(this));
        }
        this.uniteAdministrativeListFils = uniteAdministratives;
    }

    public UniteAdministrative uniteAdministrativeListFils(Set<UniteAdministrative> uniteAdministratives) {
        this.setUniteAdministrativeListFils(uniteAdministratives);
        return this;
    }

    public UniteAdministrative addUniteAdministrativeListFils(UniteAdministrative uniteAdministrative) {
        this.uniteAdministrativeListFils.add(uniteAdministrative);
        uniteAdministrative.setUniteAdministrativeParent(this);
        return this;
    }

    public UniteAdministrative removeUniteAdministrativeListFils(UniteAdministrative uniteAdministrative) {
        this.uniteAdministrativeListFils.remove(uniteAdministrative);
        uniteAdministrative.setUniteAdministrativeParent(null);
        return this;
    }

    public Valeur getTypeUniteAdministrative() {
        return this.typeUniteAdministrative;
    }

    public void setTypeUniteAdministrative(Valeur valeur) {
        this.typeUniteAdministrative = valeur;
    }

    public UniteAdministrative typeUniteAdministrative(Valeur valeur) {
        this.setTypeUniteAdministrative(valeur);
        return this;
    }

    public UniteAdministrative getUniteAdministrativeParent() {
        return this.uniteAdministrativeParent;
    }

    public void setUniteAdministrativeParent(UniteAdministrative uniteAdministrative) {
        this.uniteAdministrativeParent = uniteAdministrative;
    }

    public UniteAdministrative uniteAdministrativeParent(UniteAdministrative uniteAdministrative) {
        this.setUniteAdministrativeParent(uniteAdministrative);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UniteAdministrative)) {
            return false;
        }
        return getId() != null && getId().equals(((UniteAdministrative) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UniteAdministrative{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", ordre=" + getOrdre() +
            ", libelle='" + getLibelle() + "'" +
            "}";
    }
}
