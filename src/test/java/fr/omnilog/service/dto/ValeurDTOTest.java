package fr.omnilog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.omnilog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ValeurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ValeurDTO.class);
        ValeurDTO valeurDTO1 = new ValeurDTO();
        valeurDTO1.setId(1L);
        ValeurDTO valeurDTO2 = new ValeurDTO();
        assertThat(valeurDTO1).isNotEqualTo(valeurDTO2);
        valeurDTO2.setId(valeurDTO1.getId());
        assertThat(valeurDTO1).isEqualTo(valeurDTO2);
        valeurDTO2.setId(2L);
        assertThat(valeurDTO1).isNotEqualTo(valeurDTO2);
        valeurDTO1.setId(null);
        assertThat(valeurDTO1).isNotEqualTo(valeurDTO2);
    }
}
