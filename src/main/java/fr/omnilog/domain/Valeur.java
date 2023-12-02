package fr.omnilog.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Valeur.
 */
@Entity
@Table(name = "valeur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "valeur")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Valeur implements Serializable {

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

    @Column(name = "abreviation")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String abreviation;

    @Column(name = "description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "valeurListFils")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "valeurParents", "valeurListFils" }, allowSetters = true)
    private Set<Valeur> valeurParents = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "valeurParents", "valeurListFils" }, allowSetters = true)
    private Valeur valeurListFils;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Valeur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Valeur code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getOrdre() {
        return this.ordre;
    }

    public Valeur ordre(Integer ordre) {
        this.setOrdre(ordre);
        return this;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Valeur libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getAbreviation() {
        return this.abreviation;
    }

    public Valeur abreviation(String abreviation) {
        this.setAbreviation(abreviation);
        return this;
    }

    public void setAbreviation(String abreviation) {
        this.abreviation = abreviation;
    }

    public String getDescription() {
        return this.description;
    }

    public Valeur description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Valeur> getValeurParents() {
        return this.valeurParents;
    }

    public void setValeurParents(Set<Valeur> valeurs) {
        if (this.valeurParents != null) {
            this.valeurParents.forEach(i -> i.setValeurListFils(null));
        }
        if (valeurs != null) {
            valeurs.forEach(i -> i.setValeurListFils(this));
        }
        this.valeurParents = valeurs;
    }

    public Valeur valeurParents(Set<Valeur> valeurs) {
        this.setValeurParents(valeurs);
        return this;
    }

    public Valeur addValeurParent(Valeur valeur) {
        this.valeurParents.add(valeur);
        valeur.setValeurListFils(this);
        return this;
    }

    public Valeur removeValeurParent(Valeur valeur) {
        this.valeurParents.remove(valeur);
        valeur.setValeurListFils(null);
        return this;
    }

    public Valeur getValeurListFils() {
        return this.valeurListFils;
    }

    public void setValeurListFils(Valeur valeur) {
        this.valeurListFils = valeur;
    }

    public Valeur valeurListFils(Valeur valeur) {
        this.setValeurListFils(valeur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Valeur)) {
            return false;
        }
        return getId() != null && getId().equals(((Valeur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Valeur{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", ordre=" + getOrdre() +
            ", libelle='" + getLibelle() + "'" +
            ", abreviation='" + getAbreviation() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
