package fr.omnilog.domain;

import static fr.omnilog.domain.UniteAdministrativeTestSamples.*;
import static fr.omnilog.domain.UniteAdministrativeTestSamples.*;
import static fr.omnilog.domain.ValeurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.omnilog.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UniteAdministrativeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UniteAdministrative.class);
        UniteAdministrative uniteAdministrative1 = getUniteAdministrativeSample1();
        UniteAdministrative uniteAdministrative2 = new UniteAdministrative();
        assertThat(uniteAdministrative1).isNotEqualTo(uniteAdministrative2);

        uniteAdministrative2.setId(uniteAdministrative1.getId());
        assertThat(uniteAdministrative1).isEqualTo(uniteAdministrative2);

        uniteAdministrative2 = getUniteAdministrativeSample2();
        assertThat(uniteAdministrative1).isNotEqualTo(uniteAdministrative2);
    }

    @Test
    void uniteAdministrativeListFilsTest() throws Exception {
        UniteAdministrative uniteAdministrative = getUniteAdministrativeRandomSampleGenerator();
        UniteAdministrative uniteAdministrativeBack = getUniteAdministrativeRandomSampleGenerator();

        uniteAdministrative.addUniteAdministrativeListFils(uniteAdministrativeBack);
        assertThat(uniteAdministrative.getUniteAdministrativeListFils()).containsOnly(uniteAdministrativeBack);
        assertThat(uniteAdministrativeBack.getUniteAdministrativeParent()).isEqualTo(uniteAdministrative);

        uniteAdministrative.removeUniteAdministrativeListFils(uniteAdministrativeBack);
        assertThat(uniteAdministrative.getUniteAdministrativeListFils()).doesNotContain(uniteAdministrativeBack);
        assertThat(uniteAdministrativeBack.getUniteAdministrativeParent()).isNull();

        uniteAdministrative.uniteAdministrativeListFils(new HashSet<>(Set.of(uniteAdministrativeBack)));
        assertThat(uniteAdministrative.getUniteAdministrativeListFils()).containsOnly(uniteAdministrativeBack);
        assertThat(uniteAdministrativeBack.getUniteAdministrativeParent()).isEqualTo(uniteAdministrative);

        uniteAdministrative.setUniteAdministrativeListFils(new HashSet<>());
        assertThat(uniteAdministrative.getUniteAdministrativeListFils()).doesNotContain(uniteAdministrativeBack);
        assertThat(uniteAdministrativeBack.getUniteAdministrativeParent()).isNull();
    }

    @Test
    void typeUniteAdministrativeTest() throws Exception {
        UniteAdministrative uniteAdministrative = getUniteAdministrativeRandomSampleGenerator();
        Valeur valeurBack = getValeurRandomSampleGenerator();

        uniteAdministrative.setTypeUniteAdministrative(valeurBack);
        assertThat(uniteAdministrative.getTypeUniteAdministrative()).isEqualTo(valeurBack);

        uniteAdministrative.typeUniteAdministrative(null);
        assertThat(uniteAdministrative.getTypeUniteAdministrative()).isNull();
    }

    @Test
    void uniteAdministrativeParentTest() throws Exception {
        UniteAdministrative uniteAdministrative = getUniteAdministrativeRandomSampleGenerator();
        UniteAdministrative uniteAdministrativeBack = getUniteAdministrativeRandomSampleGenerator();

        uniteAdministrative.setUniteAdministrativeParent(uniteAdministrativeBack);
        assertThat(uniteAdministrative.getUniteAdministrativeParent()).isEqualTo(uniteAdministrativeBack);

        uniteAdministrative.uniteAdministrativeParent(null);
        assertThat(uniteAdministrative.getUniteAdministrativeParent()).isNull();
    }
}
