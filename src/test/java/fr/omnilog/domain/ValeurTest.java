package fr.omnilog.domain;

import static fr.omnilog.domain.ValeurTestSamples.*;
import static fr.omnilog.domain.ValeurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.omnilog.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ValeurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Valeur.class);
        Valeur valeur1 = getValeurSample1();
        Valeur valeur2 = new Valeur();
        assertThat(valeur1).isNotEqualTo(valeur2);

        valeur2.setId(valeur1.getId());
        assertThat(valeur1).isEqualTo(valeur2);

        valeur2 = getValeurSample2();
        assertThat(valeur1).isNotEqualTo(valeur2);
    }

    @Test
    void valeurParentTest() throws Exception {
        Valeur valeur = getValeurRandomSampleGenerator();
        Valeur valeurBack = getValeurRandomSampleGenerator();

        valeur.addValeurParent(valeurBack);
        assertThat(valeur.getValeurParents()).containsOnly(valeurBack);
        assertThat(valeurBack.getValeurListFils()).isEqualTo(valeur);

        valeur.removeValeurParent(valeurBack);
        assertThat(valeur.getValeurParents()).doesNotContain(valeurBack);
        assertThat(valeurBack.getValeurListFils()).isNull();

        valeur.valeurParents(new HashSet<>(Set.of(valeurBack)));
        assertThat(valeur.getValeurParents()).containsOnly(valeurBack);
        assertThat(valeurBack.getValeurListFils()).isEqualTo(valeur);

        valeur.setValeurParents(new HashSet<>());
        assertThat(valeur.getValeurParents()).doesNotContain(valeurBack);
        assertThat(valeurBack.getValeurListFils()).isNull();
    }

    @Test
    void valeurListFilsTest() throws Exception {
        Valeur valeur = getValeurRandomSampleGenerator();
        Valeur valeurBack = getValeurRandomSampleGenerator();

        valeur.setValeurListFils(valeurBack);
        assertThat(valeur.getValeurListFils()).isEqualTo(valeurBack);

        valeur.valeurListFils(null);
        assertThat(valeur.getValeurListFils()).isNull();
    }
}
