package io.aurigraph.healthcare.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.aurigraph.healthcare.IntegrationTest;
import io.aurigraph.healthcare.domain.Disability;
import io.aurigraph.healthcare.repository.DisabilityRepository;
import io.aurigraph.healthcare.service.criteria.DisabilityCriteria;
import io.aurigraph.healthcare.service.dto.DisabilityDTO;
import io.aurigraph.healthcare.service.mapper.DisabilityMapper;
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
 * Integration tests for the {@link DisabilityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DisabilityResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/disabilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DisabilityRepository disabilityRepository;

    @Autowired
    private DisabilityMapper disabilityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDisabilityMockMvc;

    private Disability disability;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disability createEntity(EntityManager em) {
        Disability disability = new Disability().name(DEFAULT_NAME);
        return disability;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Disability createUpdatedEntity(EntityManager em) {
        Disability disability = new Disability().name(UPDATED_NAME);
        return disability;
    }

    @BeforeEach
    public void initTest() {
        disability = createEntity(em);
    }

    @Test
    @Transactional
    void createDisability() throws Exception {
        int databaseSizeBeforeCreate = disabilityRepository.findAll().size();
        // Create the Disability
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);
        restDisabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(disabilityDTO)))
            .andExpect(status().isCreated());

        // Validate the Disability in the database
        List<Disability> disabilityList = disabilityRepository.findAll();
        assertThat(disabilityList).hasSize(databaseSizeBeforeCreate + 1);
        Disability testDisability = disabilityList.get(disabilityList.size() - 1);
        assertThat(testDisability.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createDisabilityWithExistingId() throws Exception {
        // Create the Disability with an existing ID
        disability.setId(1L);
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);

        int databaseSizeBeforeCreate = disabilityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDisabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(disabilityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Disability in the database
        List<Disability> disabilityList = disabilityRepository.findAll();
        assertThat(disabilityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = disabilityRepository.findAll().size();
        // set the field null
        disability.setName(null);

        // Create the Disability, which fails.
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);

        restDisabilityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(disabilityDTO)))
            .andExpect(status().isBadRequest());

        List<Disability> disabilityList = disabilityRepository.findAll();
        assertThat(disabilityList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDisabilities() throws Exception {
        // Initialize the database
        disabilityRepository.saveAndFlush(disability);

        // Get all the disabilityList
        restDisabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disability.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getDisability() throws Exception {
        // Initialize the database
        disabilityRepository.saveAndFlush(disability);

        // Get the disability
        restDisabilityMockMvc
            .perform(get(ENTITY_API_URL_ID, disability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(disability.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getDisabilitiesByIdFiltering() throws Exception {
        // Initialize the database
        disabilityRepository.saveAndFlush(disability);

        Long id = disability.getId();

        defaultDisabilityShouldBeFound("id.equals=" + id);
        defaultDisabilityShouldNotBeFound("id.notEquals=" + id);

        defaultDisabilityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDisabilityShouldNotBeFound("id.greaterThan=" + id);

        defaultDisabilityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDisabilityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDisabilitiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        disabilityRepository.saveAndFlush(disability);

        // Get all the disabilityList where name equals to DEFAULT_NAME
        defaultDisabilityShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the disabilityList where name equals to UPDATED_NAME
        defaultDisabilityShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDisabilitiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        disabilityRepository.saveAndFlush(disability);

        // Get all the disabilityList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDisabilityShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the disabilityList where name equals to UPDATED_NAME
        defaultDisabilityShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDisabilitiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        disabilityRepository.saveAndFlush(disability);

        // Get all the disabilityList where name is not null
        defaultDisabilityShouldBeFound("name.specified=true");

        // Get all the disabilityList where name is null
        defaultDisabilityShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllDisabilitiesByNameContainsSomething() throws Exception {
        // Initialize the database
        disabilityRepository.saveAndFlush(disability);

        // Get all the disabilityList where name contains DEFAULT_NAME
        defaultDisabilityShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the disabilityList where name contains UPDATED_NAME
        defaultDisabilityShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDisabilitiesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        disabilityRepository.saveAndFlush(disability);

        // Get all the disabilityList where name does not contain DEFAULT_NAME
        defaultDisabilityShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the disabilityList where name does not contain UPDATED_NAME
        defaultDisabilityShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDisabilityShouldBeFound(String filter) throws Exception {
        restDisabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(disability.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restDisabilityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDisabilityShouldNotBeFound(String filter) throws Exception {
        restDisabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDisabilityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDisability() throws Exception {
        // Get the disability
        restDisabilityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDisability() throws Exception {
        // Initialize the database
        disabilityRepository.saveAndFlush(disability);

        int databaseSizeBeforeUpdate = disabilityRepository.findAll().size();

        // Update the disability
        Disability updatedDisability = disabilityRepository.findById(disability.getId()).get();
        // Disconnect from session so that the updates on updatedDisability are not directly saved in db
        em.detach(updatedDisability);
        updatedDisability.name(UPDATED_NAME);
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(updatedDisability);

        restDisabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disabilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disabilityDTO))
            )
            .andExpect(status().isOk());

        // Validate the Disability in the database
        List<Disability> disabilityList = disabilityRepository.findAll();
        assertThat(disabilityList).hasSize(databaseSizeBeforeUpdate);
        Disability testDisability = disabilityList.get(disabilityList.size() - 1);
        assertThat(testDisability.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingDisability() throws Exception {
        int databaseSizeBeforeUpdate = disabilityRepository.findAll().size();
        disability.setId(count.incrementAndGet());

        // Create the Disability
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, disabilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disability in the database
        List<Disability> disabilityList = disabilityRepository.findAll();
        assertThat(disabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDisability() throws Exception {
        int databaseSizeBeforeUpdate = disabilityRepository.findAll().size();
        disability.setId(count.incrementAndGet());

        // Create the Disability
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(disabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disability in the database
        List<Disability> disabilityList = disabilityRepository.findAll();
        assertThat(disabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDisability() throws Exception {
        int databaseSizeBeforeUpdate = disabilityRepository.findAll().size();
        disability.setId(count.incrementAndGet());

        // Create the Disability
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisabilityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(disabilityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disability in the database
        List<Disability> disabilityList = disabilityRepository.findAll();
        assertThat(disabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDisabilityWithPatch() throws Exception {
        // Initialize the database
        disabilityRepository.saveAndFlush(disability);

        int databaseSizeBeforeUpdate = disabilityRepository.findAll().size();

        // Update the disability using partial update
        Disability partialUpdatedDisability = new Disability();
        partialUpdatedDisability.setId(disability.getId());

        partialUpdatedDisability.name(UPDATED_NAME);

        restDisabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisability.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisability))
            )
            .andExpect(status().isOk());

        // Validate the Disability in the database
        List<Disability> disabilityList = disabilityRepository.findAll();
        assertThat(disabilityList).hasSize(databaseSizeBeforeUpdate);
        Disability testDisability = disabilityList.get(disabilityList.size() - 1);
        assertThat(testDisability.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateDisabilityWithPatch() throws Exception {
        // Initialize the database
        disabilityRepository.saveAndFlush(disability);

        int databaseSizeBeforeUpdate = disabilityRepository.findAll().size();

        // Update the disability using partial update
        Disability partialUpdatedDisability = new Disability();
        partialUpdatedDisability.setId(disability.getId());

        partialUpdatedDisability.name(UPDATED_NAME);

        restDisabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDisability.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDisability))
            )
            .andExpect(status().isOk());

        // Validate the Disability in the database
        List<Disability> disabilityList = disabilityRepository.findAll();
        assertThat(disabilityList).hasSize(databaseSizeBeforeUpdate);
        Disability testDisability = disabilityList.get(disabilityList.size() - 1);
        assertThat(testDisability.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingDisability() throws Exception {
        int databaseSizeBeforeUpdate = disabilityRepository.findAll().size();
        disability.setId(count.incrementAndGet());

        // Create the Disability
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDisabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, disabilityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disability in the database
        List<Disability> disabilityList = disabilityRepository.findAll();
        assertThat(disabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDisability() throws Exception {
        int databaseSizeBeforeUpdate = disabilityRepository.findAll().size();
        disability.setId(count.incrementAndGet());

        // Create the Disability
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(disabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Disability in the database
        List<Disability> disabilityList = disabilityRepository.findAll();
        assertThat(disabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDisability() throws Exception {
        int databaseSizeBeforeUpdate = disabilityRepository.findAll().size();
        disability.setId(count.incrementAndGet());

        // Create the Disability
        DisabilityDTO disabilityDTO = disabilityMapper.toDto(disability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDisabilityMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(disabilityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Disability in the database
        List<Disability> disabilityList = disabilityRepository.findAll();
        assertThat(disabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDisability() throws Exception {
        // Initialize the database
        disabilityRepository.saveAndFlush(disability);

        int databaseSizeBeforeDelete = disabilityRepository.findAll().size();

        // Delete the disability
        restDisabilityMockMvc
            .perform(delete(ENTITY_API_URL_ID, disability.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Disability> disabilityList = disabilityRepository.findAll();
        assertThat(disabilityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
