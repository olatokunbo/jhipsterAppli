package fr.omnilog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.omnilog.IntegrationTest;
import fr.omnilog.domain.Valeur;
import fr.omnilog.repository.ValeurRepository;
import fr.omnilog.repository.search.ValeurSearchRepository;
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
 * Integration tests for the {@link ValeurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ValeurResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDRE = 1;
    private static final Integer UPDATED_ORDRE = 2;

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_ABREVIATION = "AAAAAAAAAA";
    private static final String UPDATED_ABREVIATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/valeurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/valeurs/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ValeurRepository valeurRepository;

    @Autowired
    private ValeurSearchRepository valeurSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restValeurMockMvc;

    private Valeur valeur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Valeur createEntity(EntityManager em) {
        Valeur valeur = new Valeur()
            .code(DEFAULT_CODE)
            .ordre(DEFAULT_ORDRE)
            .libelle(DEFAULT_LIBELLE)
            .abreviation(DEFAULT_ABREVIATION)
            .description(DEFAULT_DESCRIPTION);
        return valeur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Valeur createUpdatedEntity(EntityManager em) {
        Valeur valeur = new Valeur()
            .code(UPDATED_CODE)
            .ordre(UPDATED_ORDRE)
            .libelle(UPDATED_LIBELLE)
            .abreviation(UPDATED_ABREVIATION)
            .description(UPDATED_DESCRIPTION);
        return valeur;
    }

    @AfterEach
    public void cleanupElasticSearchRepository() {
        valeurSearchRepository.deleteAll();
        assertThat(valeurSearchRepository.count()).isEqualTo(0);
    }

    @BeforeEach
    public void initTest() {
        valeur = createEntity(em);
    }

    @Test
    @Transactional
    void createValeur() throws Exception {
        int databaseSizeBeforeCreate = valeurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(valeurSearchRepository.findAll());
        // Create the Valeur
        restValeurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(valeur)))
            .andExpect(status().isCreated());

        // Validate the Valeur in the database
        List<Valeur> valeurList = valeurRepository.findAll();
        assertThat(valeurList).hasSize(databaseSizeBeforeCreate + 1);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(valeurSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });
        Valeur testValeur = valeurList.get(valeurList.size() - 1);
        assertThat(testValeur.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testValeur.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testValeur.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testValeur.getAbreviation()).isEqualTo(DEFAULT_ABREVIATION);
        assertThat(testValeur.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createValeurWithExistingId() throws Exception {
        // Create the Valeur with an existing ID
        valeur.setId(1L);

        int databaseSizeBeforeCreate = valeurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(valeurSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restValeurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(valeur)))
            .andExpect(status().isBadRequest());

        // Validate the Valeur in the database
        List<Valeur> valeurList = valeurRepository.findAll();
        assertThat(valeurList).hasSize(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(valeurSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllValeurs() throws Exception {
        // Initialize the database
        valeurRepository.saveAndFlush(valeur);

        // Get all the valeurList
        restValeurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(valeur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].abreviation").value(hasItem(DEFAULT_ABREVIATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getValeur() throws Exception {
        // Initialize the database
        valeurRepository.saveAndFlush(valeur);

        // Get the valeur
        restValeurMockMvc
            .perform(get(ENTITY_API_URL_ID, valeur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(valeur.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.ordre").value(DEFAULT_ORDRE))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.abreviation").value(DEFAULT_ABREVIATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingValeur() throws Exception {
        // Get the valeur
        restValeurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingValeur() throws Exception {
        // Initialize the database
        valeurRepository.saveAndFlush(valeur);

        int databaseSizeBeforeUpdate = valeurRepository.findAll().size();
        valeurSearchRepository.save(valeur);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(valeurSearchRepository.findAll());

        // Update the valeur
        Valeur updatedValeur = valeurRepository.findById(valeur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedValeur are not directly saved in db
        em.detach(updatedValeur);
        updatedValeur
            .code(UPDATED_CODE)
            .ordre(UPDATED_ORDRE)
            .libelle(UPDATED_LIBELLE)
            .abreviation(UPDATED_ABREVIATION)
            .description(UPDATED_DESCRIPTION);

        restValeurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedValeur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedValeur))
            )
            .andExpect(status().isOk());

        // Validate the Valeur in the database
        List<Valeur> valeurList = valeurRepository.findAll();
        assertThat(valeurList).hasSize(databaseSizeBeforeUpdate);
        Valeur testValeur = valeurList.get(valeurList.size() - 1);
        assertThat(testValeur.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testValeur.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testValeur.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testValeur.getAbreviation()).isEqualTo(UPDATED_ABREVIATION);
        assertThat(testValeur.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(valeurSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Valeur> valeurSearchList = IterableUtils.toList(valeurSearchRepository.findAll());
                Valeur testValeurSearch = valeurSearchList.get(searchDatabaseSizeAfter - 1);
                assertThat(testValeurSearch.getCode()).isEqualTo(UPDATED_CODE);
                assertThat(testValeurSearch.getOrdre()).isEqualTo(UPDATED_ORDRE);
                assertThat(testValeurSearch.getLibelle()).isEqualTo(UPDATED_LIBELLE);
                assertThat(testValeurSearch.getAbreviation()).isEqualTo(UPDATED_ABREVIATION);
                assertThat(testValeurSearch.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
            });
    }

    @Test
    @Transactional
    void putNonExistingValeur() throws Exception {
        int databaseSizeBeforeUpdate = valeurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(valeurSearchRepository.findAll());
        valeur.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValeurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, valeur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(valeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Valeur in the database
        List<Valeur> valeurList = valeurRepository.findAll();
        assertThat(valeurList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(valeurSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchValeur() throws Exception {
        int databaseSizeBeforeUpdate = valeurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(valeurSearchRepository.findAll());
        valeur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValeurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(valeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Valeur in the database
        List<Valeur> valeurList = valeurRepository.findAll();
        assertThat(valeurList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(valeurSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamValeur() throws Exception {
        int databaseSizeBeforeUpdate = valeurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(valeurSearchRepository.findAll());
        valeur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValeurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(valeur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Valeur in the database
        List<Valeur> valeurList = valeurRepository.findAll();
        assertThat(valeurList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(valeurSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateValeurWithPatch() throws Exception {
        // Initialize the database
        valeurRepository.saveAndFlush(valeur);

        int databaseSizeBeforeUpdate = valeurRepository.findAll().size();

        // Update the valeur using partial update
        Valeur partialUpdatedValeur = new Valeur();
        partialUpdatedValeur.setId(valeur.getId());

        partialUpdatedValeur.code(UPDATED_CODE).libelle(UPDATED_LIBELLE).abreviation(UPDATED_ABREVIATION);

        restValeurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValeur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedValeur))
            )
            .andExpect(status().isOk());

        // Validate the Valeur in the database
        List<Valeur> valeurList = valeurRepository.findAll();
        assertThat(valeurList).hasSize(databaseSizeBeforeUpdate);
        Valeur testValeur = valeurList.get(valeurList.size() - 1);
        assertThat(testValeur.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testValeur.getOrdre()).isEqualTo(DEFAULT_ORDRE);
        assertThat(testValeur.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testValeur.getAbreviation()).isEqualTo(UPDATED_ABREVIATION);
        assertThat(testValeur.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateValeurWithPatch() throws Exception {
        // Initialize the database
        valeurRepository.saveAndFlush(valeur);

        int databaseSizeBeforeUpdate = valeurRepository.findAll().size();

        // Update the valeur using partial update
        Valeur partialUpdatedValeur = new Valeur();
        partialUpdatedValeur.setId(valeur.getId());

        partialUpdatedValeur
            .code(UPDATED_CODE)
            .ordre(UPDATED_ORDRE)
            .libelle(UPDATED_LIBELLE)
            .abreviation(UPDATED_ABREVIATION)
            .description(UPDATED_DESCRIPTION);

        restValeurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedValeur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedValeur))
            )
            .andExpect(status().isOk());

        // Validate the Valeur in the database
        List<Valeur> valeurList = valeurRepository.findAll();
        assertThat(valeurList).hasSize(databaseSizeBeforeUpdate);
        Valeur testValeur = valeurList.get(valeurList.size() - 1);
        assertThat(testValeur.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testValeur.getOrdre()).isEqualTo(UPDATED_ORDRE);
        assertThat(testValeur.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testValeur.getAbreviation()).isEqualTo(UPDATED_ABREVIATION);
        assertThat(testValeur.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingValeur() throws Exception {
        int databaseSizeBeforeUpdate = valeurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(valeurSearchRepository.findAll());
        valeur.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValeurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, valeur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(valeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Valeur in the database
        List<Valeur> valeurList = valeurRepository.findAll();
        assertThat(valeurList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(valeurSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchValeur() throws Exception {
        int databaseSizeBeforeUpdate = valeurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(valeurSearchRepository.findAll());
        valeur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValeurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(valeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Valeur in the database
        List<Valeur> valeurList = valeurRepository.findAll();
        assertThat(valeurList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(valeurSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamValeur() throws Exception {
        int databaseSizeBeforeUpdate = valeurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(valeurSearchRepository.findAll());
        valeur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restValeurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(valeur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Valeur in the database
        List<Valeur> valeurList = valeurRepository.findAll();
        assertThat(valeurList).hasSize(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(valeurSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteValeur() throws Exception {
        // Initialize the database
        valeurRepository.saveAndFlush(valeur);
        valeurRepository.save(valeur);
        valeurSearchRepository.save(valeur);

        int databaseSizeBeforeDelete = valeurRepository.findAll().size();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(valeurSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the valeur
        restValeurMockMvc
            .perform(delete(ENTITY_API_URL_ID, valeur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Valeur> valeurList = valeurRepository.findAll();
        assertThat(valeurList).hasSize(databaseSizeBeforeDelete - 1);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(valeurSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchValeur() throws Exception {
        // Initialize the database
        valeur = valeurRepository.saveAndFlush(valeur);
        valeurSearchRepository.save(valeur);

        // Search the valeur
        restValeurMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + valeur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(valeur.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].ordre").value(hasItem(DEFAULT_ORDRE)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].abreviation").value(hasItem(DEFAULT_ABREVIATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
