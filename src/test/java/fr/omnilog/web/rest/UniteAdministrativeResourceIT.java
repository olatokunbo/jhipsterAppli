package fr.omnilog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.omnilog.IntegrationTest;
import fr.omnilog.domain.UniteAdministrative;
import fr.omnilog.repository.UniteAdministrativeRepository;
import fr.omnilog.repository.search.UniteAdministrativeSearchRepository;
import fr.omnilog.service.dto.UniteAdministrativeDTO;
import fr.omnilog.service.mapper.UniteAdministrativeMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.collections4.IterableUtils;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UniteAdministrativeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UniteAdministrativeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDRE = 1;
    private static final Integer UPDATED_ORDRE = 2;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/unite-administratives";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/unite-administratives/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UniteAdministrativeRepository uniteAdministrativeRepository;

    @Autowired
    private UniteAdministrativeMapper uniteAdministrativeMapper;

    @Autowired
    private UniteAdministrativeSearchRepository uniteAdministrativeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUniteAdministrativeMockMvc;

    private UniteAdministrative uniteAdministrative;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UniteAdministrative createEntity(EntityManager em) {
        UniteAdministrative uniteAdministrative = new UniteAdministrative()
            .code(DEFAULT_CODE)
            .ordre(DEFAULT_ORDRE)
            .libelle(DEFAULT_LIBELLE);
        return uniteAdministrative;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UniteAdministrative createUpdatedEntity(EntityManager em) {
        UniteAdministrative uniteAdministrative = new UniteAdministrative()
            .code(UPDATED_CODE)
            .ordre(UPDATED_ORDRE)
            .libelle(UPDATED_LIBELLE);
        return uniteAdministrative;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        uniteAdministrativeSearchRepository.deleteAll();
        assertThat(uniteAdministrativeSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        uniteAdministrative = createEntity(em);
    }

    @Test
    @Transactional
    void createUniteAdministrative() throws Exception {
        int databaseSizeBeforeCreate = uniteAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
        // Create the UniteAdministrative
        UniteAdministrativeDTO uniteAdministrativeDTO = uniteAdministrativeMapper.toDto(uniteAdministrative);
        restUniteAdministrativeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uniteAdministrativeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the UniteAdministrative in the database
        List<UniteAdministrative> uniteAdministrativeList = uniteAdministrativeRepository.findAll();
        assertThat(uniteAdministrativeList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        UniteAdministrative testUniteAdministrative = uniteAdministrativeList.get(uniteAdministrativeList.size() - 1);
        assertThat(testUniteAdministrative.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testUniteAdministrative.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testUniteAdministrative.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    void createUniteAdministrativeWithExistingId() throws Exception {
        // Create the UniteAdministrative with an existing ID
        uniteAdministrative.setId(1L);
        UniteAdministrativeDTO uniteAdministrativeDTO = uniteAdministrativeMapper.toDto(uniteAdministrative);

        int databaseSizeBeforeCreate = uniteAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restUniteAdministrativeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uniteAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniteAdministrative in the database
        List<UniteAdministrative> uniteAdministrativeList = uniteAdministrativeRepository.findAll();
        assertThat(uniteAdministrativeList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllUniteAdministratives() throws Exception {
        // Initialize the database
        uniteAdministrativeRepository.saveAndFlush(uniteAdministrative);

        // Get all the uniteAdministrativeList
        restUniteAdministrativeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uniteAdministrative.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }

    @Test
    @Transactional
    void getUniteAdministrative() throws Exception {
        // Initialize the database
        uniteAdministrativeRepository.saveAndFlush(uniteAdministrative);

        // Get the uniteAdministrative
        restUniteAdministrativeMockMvc
            .perform(get(ENTITY_API_URL_ID, uniteAdministrative.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uniteAdministrative.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE));
    }

    @Test
    @Transactional
    void getNonExistingUniteAdministrative() throws Exception {
        // Get the uniteAdministrative
        restUniteAdministrativeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUniteAdministrative() throws Exception {
        // Initialize the database
        uniteAdministrativeRepository.saveAndFlush(uniteAdministrative);

        int databaseSizeBeforeUpdate = uniteAdministrativeRepository.findAll().size();
        uniteAdministrativeSearchRepository.save(uniteAdministrative);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());

        // Update the uniteAdministrative
        UniteAdministrative updatedUniteAdministrative = uniteAdministrativeRepository.findById(uniteAdministrative.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUniteAdministrative are not directly saved in db
        em.detach(updatedUniteAdministrative);
        updatedUniteAdministrative.code(UPDATED_CODE).ordre(UPDATED_ORDRE).libelle(UPDATED_LIBELLE);
        UniteAdministrativeDTO uniteAdministrativeDTO = uniteAdministrativeMapper.toDto(updatedUniteAdministrative);

        restUniteAdministrativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uniteAdministrativeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uniteAdministrativeDTO))
            )
            .andExpect(status().isOk());

        // Validate the UniteAdministrative in the database
        List<UniteAdministrative> uniteAdministrativeList = uniteAdministrativeRepository.findAll();
        assertThat(uniteAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        UniteAdministrative testUniteAdministrative = uniteAdministrativeList.get(uniteAdministrativeList.size() - 1);
        assertThat(testUniteAdministrative.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUniteAdministrative.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testUniteAdministrative.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<UniteAdministrative> uniteAdministrativeSearchList = IterableUtils.toList(
                    uniteAdministrativeSearchRepository.findAll()
                );
                UniteAdministrative testUniteAdministrativeSearch = uniteAdministrativeSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testUniteAdministrativeSearch.getCode()).isEqualTo(UPDATED_CODE);
                assertThat(testUniteAdministrativeSearch.getOrdre()).isEqualTo(UPDATED_ORDRE);
                assertThat(testUniteAdministrativeSearch.getLibelle()).isEqualTo(UPDATED_LIBELLE);
            });
    }

    @Test
    @Transactional
    void putNonExistingUniteAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = uniteAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
        uniteAdministrative.setId(longCount.incrementAndGet());

        // Create the UniteAdministrative
        UniteAdministrativeDTO uniteAdministrativeDTO = uniteAdministrativeMapper.toDto(uniteAdministrative);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUniteAdministrativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uniteAdministrativeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uniteAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniteAdministrative in the database
        List<UniteAdministrative> uniteAdministrativeList = uniteAdministrativeRepository.findAll();
        assertThat(uniteAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchUniteAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = uniteAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
        uniteAdministrative.setId(longCount.incrementAndGet());

        // Create the UniteAdministrative
        UniteAdministrativeDTO uniteAdministrativeDTO = uniteAdministrativeMapper.toDto(uniteAdministrative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniteAdministrativeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uniteAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniteAdministrative in the database
        List<UniteAdministrative> uniteAdministrativeList = uniteAdministrativeRepository.findAll();
        assertThat(uniteAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUniteAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = uniteAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
        uniteAdministrative.setId(longCount.incrementAndGet());

        // Create the UniteAdministrative
        UniteAdministrativeDTO uniteAdministrativeDTO = uniteAdministrativeMapper.toDto(uniteAdministrative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniteAdministrativeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uniteAdministrativeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UniteAdministrative in the database
        List<UniteAdministrative> uniteAdministrativeList = uniteAdministrativeRepository.findAll();
        assertThat(uniteAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateUniteAdministrativeWithPatch() throws Exception {
        // Initialize the database
        uniteAdministrativeRepository.saveAndFlush(uniteAdministrative);

        int databaseSizeBeforeUpdate = uniteAdministrativeRepository.findAll().size();

        // Update the uniteAdministrative using partial update
        UniteAdministrative partialUpdatedUniteAdministrative = new UniteAdministrative();
        partialUpdatedUniteAdministrative.setId(uniteAdministrative.getId());

        partialUpdatedUniteAdministrative.code(UPDATED_CODE).ordre(UPDATED_ORDRE).libelle(UPDATED_LIBELLE);

        restUniteAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUniteAdministrative.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUniteAdministrative))
            )
            .andExpect(status().isOk());

        // Validate the UniteAdministrative in the database
        List<UniteAdministrative> uniteAdministrativeList = uniteAdministrativeRepository.findAll();
        assertThat(uniteAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        UniteAdministrative testUniteAdministrative = uniteAdministrativeList.get(uniteAdministrativeList.size() - 1);
        assertThat(testUniteAdministrative.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUniteAdministrative.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testUniteAdministrative.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void fullUpdateUniteAdministrativeWithPatch() throws Exception {
        // Initialize the database
        uniteAdministrativeRepository.saveAndFlush(uniteAdministrative);

        int databaseSizeBeforeUpdate = uniteAdministrativeRepository.findAll().size();

        // Update the uniteAdministrative using partial update
        UniteAdministrative partialUpdatedUniteAdministrative = new UniteAdministrative();
        partialUpdatedUniteAdministrative.setId(uniteAdministrative.getId());

        partialUpdatedUniteAdministrative.code(UPDATED_CODE).ordre(UPDATED_ORDRE).libelle(UPDATED_LIBELLE);

        restUniteAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUniteAdministrative.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUniteAdministrative))
            )
            .andExpect(status().isOk());

        // Validate the UniteAdministrative in the database
        List<UniteAdministrative> uniteAdministrativeList = uniteAdministrativeRepository.findAll();
        assertThat(uniteAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        UniteAdministrative testUniteAdministrative = uniteAdministrativeList.get(uniteAdministrativeList.size() - 1);
        assertThat(testUniteAdministrative.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testUniteAdministrative.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testUniteAdministrative.getLibelle()).isEqualTo(UPDATED_LIBELLE);
    }

    @Test
    @Transactional
    void patchNonExistingUniteAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = uniteAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
        uniteAdministrative.setId(longCount.incrementAndGet());

        // Create the UniteAdministrative
        UniteAdministrativeDTO uniteAdministrativeDTO = uniteAdministrativeMapper.toDto(uniteAdministrative);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUniteAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uniteAdministrativeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uniteAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniteAdministrative in the database
        List<UniteAdministrative> uniteAdministrativeList = uniteAdministrativeRepository.findAll();
        assertThat(uniteAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUniteAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = uniteAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
        uniteAdministrative.setId(longCount.incrementAndGet());

        // Create the UniteAdministrative
        UniteAdministrativeDTO uniteAdministrativeDTO = uniteAdministrativeMapper.toDto(uniteAdministrative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniteAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uniteAdministrativeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UniteAdministrative in the database
        List<UniteAdministrative> uniteAdministrativeList = uniteAdministrativeRepository.findAll();
        assertThat(uniteAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUniteAdministrative() throws Exception {
        int databaseSizeBeforeUpdate = uniteAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
        uniteAdministrative.setId(longCount.incrementAndGet());

        // Create the UniteAdministrative
        UniteAdministrativeDTO uniteAdministrativeDTO = uniteAdministrativeMapper.toDto(uniteAdministrative);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUniteAdministrativeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uniteAdministrativeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UniteAdministrative in the database
        List<UniteAdministrative> uniteAdministrativeList = uniteAdministrativeRepository.findAll();
        assertThat(uniteAdministrativeList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteUniteAdministrative() throws Exception {
        // Initialize the database
        uniteAdministrativeRepository.saveAndFlush(uniteAdministrative);
        uniteAdministrativeRepository.save(uniteAdministrative);
        uniteAdministrativeSearchRepository.save(uniteAdministrative);

        int databaseSizeBeforeDelete = uniteAdministrativeRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the uniteAdministrative
        restUniteAdministrativeMockMvc
            .perform(delete(ENTITY_API_URL_ID, uniteAdministrative.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UniteAdministrative> uniteAdministrativeList = uniteAdministrativeRepository.findAll();
        assertThat(uniteAdministrativeList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uniteAdministrativeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchUniteAdministrative() throws Exception {
        // Initialize the database
        uniteAdministrative = uniteAdministrativeRepository.saveAndFlush(uniteAdministrative);
        uniteAdministrativeSearchRepository.save(uniteAdministrative);

        // Search the uniteAdministrative
        restUniteAdministrativeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + uniteAdministrative.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uniteAdministrative.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)));
    }
}
