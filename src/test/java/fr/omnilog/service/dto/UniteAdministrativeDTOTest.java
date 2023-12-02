package fr.omnilog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.omnilog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UniteAdministrativeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UniteAdministrativeDTO.class);
        UniteAdministrativeDTO uniteAdministrativeDTO1 = new UniteAdministrativeDTO();
        uniteAdministrativeDTO1.setId(1L);
        UniteAdministrativeDTO uniteAdministrativeDTO2 = new UniteAdministrativeDTO();
        assertThat(uniteAdministrativeDTO1).isNotEqualTo(uniteAdministrativeDTO2);
        uniteAdministrativeDTO2.setId(uniteAdministrativeDTO1.getId());
        assertThat(uniteAdministrativeDTO1).isEqualTo(uniteAdministrativeDTO2);
        uniteAdministrativeDTO2.setId(2L);
        assertThat(uniteAdministrativeDTO1).isNotEqualTo(uniteAdministrativeDTO2);
        uniteAdministrativeDTO1.setId(null);
        assertThat(uniteAdministrativeDTO1).isNotEqualTo(uniteAdministrativeDTO2);
    }
}
