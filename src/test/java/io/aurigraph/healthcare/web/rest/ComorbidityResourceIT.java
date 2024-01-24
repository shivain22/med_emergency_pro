package io.aurigraph.healthcare.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.aurigraph.healthcare.IntegrationTest;
import io.aurigraph.healthcare.domain.Comorbidity;
import io.aurigraph.healthcare.repository.ComorbidityRepository;
import io.aurigraph.healthcare.service.criteria.ComorbidityCriteria;
import io.aurigraph.healthcare.service.dto.ComorbidityDTO;
import io.aurigraph.healthcare.service.mapper.ComorbidityMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ComorbidityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComorbidityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/comorbidities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComorbidityRepository comorbidityRepository;

    @Autowired
    private ComorbidityMapper comorbidityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComorbidityMockMvc;

    private Comorbidity comorbidity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comorbidity createEntity(EntityManager em) {
        Comorbidity comorbidity = new Comorbidity().name(DEFAULT_NAME);
        return comorbidity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Comorbidity createUpdatedEntity(EntityManager em) {
        Comorbidity comorbidity = new Comorbidity().name(UPDATED_NAME);
        return comorbidity;
    }

    @BeforeEach
    public void initTest() {
        comorbidity = createEntity(em);
    }

    @Test
    @Transactional
    void createComorbidity() throws Exception {
        int databaseSizeBeforeCreate = comorbidityRepository.findAll().size();
        // Create the Comorbidity
        ComorbidityDTO comorbidityDTO = comorbidityMapper.toDto(comorbidity);
        restComorbidityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comorbidityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Comorbidity in the database
        List<Comorbidity> comorbidityList = comorbidityRepository.findAll();
        assertThat(comorbidityList).hasSize(databaseSizeBeforeCreate + 1);
        Comorbidity testComorbidity = comorbidityList.get(comorbidityList.size() - 1);
        assertThat(testComorbidity.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createComorbidityWithExistingId() throws Exception {
        // Create the Comorbidity with an existing ID
        comorbidity.setId(1L);
        ComorbidityDTO comorbidityDTO = comorbidityMapper.toDto(comorbidity);

        int databaseSizeBeforeCreate = comorbidityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComorbidityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comorbidityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comorbidity in the database
        List<Comorbidity> comorbidityList = comorbidityRepository.findAll();
        assertThat(comorbidityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = comorbidityRepository.findAll().size();
        // set the field null
        comorbidity.setName(null);

        // Create the Comorbidity, which fails.
        ComorbidityDTO comorbidityDTO = comorbidityMapper.toDto(comorbidity);

        restComorbidityMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comorbidityDTO))
            )
            .andExpect(status().isBadRequest());

        List<Comorbidity> comorbidityList = comorbidityRepository.findAll();
        assertThat(comorbidityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComorbidities() throws Exception {
        // Initialize the database
        comorbidityRepository.saveAndFlush(comorbidity);

        // Get all the comorbidityList
        restComorbidityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comorbidity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getComorbidity() throws Exception {
        // Initialize the database
        comorbidityRepository.saveAndFlush(comorbidity);

        // Get the comorbidity
        restComorbidityMockMvc
            .perform(get(ENTITY_API_URL_ID, comorbidity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(comorbidity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getComorbiditiesByIdFiltering() throws Exception {
        // Initialize the database
        comorbidityRepository.saveAndFlush(comorbidity);

        Long id = comorbidity.getId();

        defaultComorbidityShouldBeFound("id.equals=" + id);
        defaultComorbidityShouldNotBeFound("id.notEquals=" + id);

        defaultComorbidityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultComorbidityShouldNotBeFound("id.greaterThan=" + id);

        defaultComorbidityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultComorbidityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllComorbiditiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        comorbidityRepository.saveAndFlush(comorbidity);

        // Get all the comorbidityList where name equals to DEFAULT_NAME
        defaultComorbidityShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the comorbidityList where name equals to UPDATED_NAME
        defaultComorbidityShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllComorbiditiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        comorbidityRepository.saveAndFlush(comorbidity);

        // Get all the comorbidityList where name in DEFAULT_NAME or UPDATED_NAME
        defaultComorbidityShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the comorbidityList where name equals to UPDATED_NAME
        defaultComorbidityShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllComorbiditiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        comorbidityRepository.saveAndFlush(comorbidity);

        // Get all the comorbidityList where name is not null
        defaultComorbidityShouldBeFound("name.specified=true");

        // Get all the comorbidityList where name is null
        defaultComorbidityShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllComorbiditiesByNameContainsSomething() throws Exception {
        // Initialize the database
        comorbidityRepository.saveAndFlush(comorbidity);

        // Get all the comorbidityList where name contains DEFAULT_NAME
        defaultComorbidityShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the comorbidityList where name contains UPDATED_NAME
        defaultComorbidityShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllComorbiditiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        comorbidityRepository.saveAndFlush(comorbidity);

        // Get all the comorbidityList where name does not contain DEFAULT_NAME
        defaultComorbidityShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the comorbidityList where name does not contain UPDATED_NAME
        defaultComorbidityShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultComorbidityShouldBeFound(String filter) throws Exception {
        restComorbidityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comorbidity.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restComorbidityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultComorbidityShouldNotBeFound(String filter) throws Exception {
        restComorbidityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restComorbidityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingComorbidity() throws Exception {
        // Get the comorbidity
        restComorbidityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingComorbidity() throws Exception {
        // Initialize the database
        comorbidityRepository.saveAndFlush(comorbidity);

        int databaseSizeBeforeUpdate = comorbidityRepository.findAll().size();

        // Update the comorbidity
        Comorbidity updatedComorbidity = comorbidityRepository.findById(comorbidity.getId()).get();
        // Disconnect from session so that the updates on updatedComorbidity are not directly saved in db
        em.detach(updatedComorbidity);
        updatedComorbidity.name(UPDATED_NAME);
        ComorbidityDTO comorbidityDTO = comorbidityMapper.toDto(updatedComorbidity);

        restComorbidityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, comorbidityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comorbidityDTO))
            )
            .andExpect(status().isOk());

        // Validate the Comorbidity in the database
        List<Comorbidity> comorbidityList = comorbidityRepository.findAll();
        assertThat(comorbidityList).hasSize(databaseSizeBeforeUpdate);
        Comorbidity testComorbidity = comorbidityList.get(comorbidityList.size() - 1);
        assertThat(testComorbidity.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingComorbidity() throws Exception {
        int databaseSizeBeforeUpdate = comorbidityRepository.findAll().size();
        comorbidity.setId(count.incrementAndGet());

        // Create the Comorbidity
        ComorbidityDTO comorbidityDTO = comorbidityMapper.toDto(comorbidity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComorbidityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, comorbidityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comorbidityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comorbidity in the database
        List<Comorbidity> comorbidityList = comorbidityRepository.findAll();
        assertThat(comorbidityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComorbidity() throws Exception {
        int databaseSizeBeforeUpdate = comorbidityRepository.findAll().size();
        comorbidity.setId(count.incrementAndGet());

        // Create the Comorbidity
        ComorbidityDTO comorbidityDTO = comorbidityMapper.toDto(comorbidity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComorbidityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(comorbidityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comorbidity in the database
        List<Comorbidity> comorbidityList = comorbidityRepository.findAll();
        assertThat(comorbidityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComorbidity() throws Exception {
        int databaseSizeBeforeUpdate = comorbidityRepository.findAll().size();
        comorbidity.setId(count.incrementAndGet());

        // Create the Comorbidity
        ComorbidityDTO comorbidityDTO = comorbidityMapper.toDto(comorbidity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComorbidityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(comorbidityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Comorbidity in the database
        List<Comorbidity> comorbidityList = comorbidityRepository.findAll();
        assertThat(comorbidityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComorbidityWithPatch() throws Exception {
        // Initialize the database
        comorbidityRepository.saveAndFlush(comorbidity);

        int databaseSizeBeforeUpdate = comorbidityRepository.findAll().size();

        // Update the comorbidity using partial update
        Comorbidity partialUpdatedComorbidity = new Comorbidity();
        partialUpdatedComorbidity.setId(comorbidity.getId());

        partialUpdatedComorbidity.name(UPDATED_NAME);

        restComorbidityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComorbidity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComorbidity))
            )
            .andExpect(status().isOk());

        // Validate the Comorbidity in the database
        List<Comorbidity> comorbidityList = comorbidityRepository.findAll();
        assertThat(comorbidityList).hasSize(databaseSizeBeforeUpdate);
        Comorbidity testComorbidity = comorbidityList.get(comorbidityList.size() - 1);
        assertThat(testComorbidity.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateComorbidityWithPatch() throws Exception {
        // Initialize the database
        comorbidityRepository.saveAndFlush(comorbidity);

        int databaseSizeBeforeUpdate = comorbidityRepository.findAll().size();

        // Update the comorbidity using partial update
        Comorbidity partialUpdatedComorbidity = new Comorbidity();
        partialUpdatedComorbidity.setId(comorbidity.getId());

        partialUpdatedComorbidity.name(UPDATED_NAME);

        restComorbidityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComorbidity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComorbidity))
            )
            .andExpect(status().isOk());

        // Validate the Comorbidity in the database
        List<Comorbidity> comorbidityList = comorbidityRepository.findAll();
        assertThat(comorbidityList).hasSize(databaseSizeBeforeUpdate);
        Comorbidity testComorbidity = comorbidityList.get(comorbidityList.size() - 1);
        assertThat(testComorbidity.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingComorbidity() throws Exception {
        int databaseSizeBeforeUpdate = comorbidityRepository.findAll().size();
        comorbidity.setId(count.incrementAndGet());

        // Create the Comorbidity
        ComorbidityDTO comorbidityDTO = comorbidityMapper.toDto(comorbidity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComorbidityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, comorbidityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comorbidityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comorbidity in the database
        List<Comorbidity> comorbidityList = comorbidityRepository.findAll();
        assertThat(comorbidityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComorbidity() throws Exception {
        int databaseSizeBeforeUpdate = comorbidityRepository.findAll().size();
        comorbidity.setId(count.incrementAndGet());

        // Create the Comorbidity
        ComorbidityDTO comorbidityDTO = comorbidityMapper.toDto(comorbidity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComorbidityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(comorbidityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Comorbidity in the database
        List<Comorbidity> comorbidityList = comorbidityRepository.findAll();
        assertThat(comorbidityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComorbidity() throws Exception {
        int databaseSizeBeforeUpdate = comorbidityRepository.findAll().size();
        comorbidity.setId(count.incrementAndGet());

        // Create the Comorbidity
        ComorbidityDTO comorbidityDTO = comorbidityMapper.toDto(comorbidity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComorbidityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(comorbidityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Comorbidity in the database
        List<Comorbidity> comorbidityList = comorbidityRepository.findAll();
        assertThat(comorbidityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComorbidity() throws Exception {
        // Initialize the database
        comorbidityRepository.saveAndFlush(comorbidity);

        int databaseSizeBeforeDelete = comorbidityRepository.findAll().size();

        // Delete the comorbidity
        restComorbidityMockMvc
            .perform(delete(ENTITY_API_URL_ID, comorbidity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Comorbidity> comorbidityList = comorbidityRepository.findAll();
        assertThat(comorbidityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
