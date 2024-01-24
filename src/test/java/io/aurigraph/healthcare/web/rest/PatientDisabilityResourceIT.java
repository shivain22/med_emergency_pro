package io.aurigraph.healthcare.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.aurigraph.healthcare.IntegrationTest;
import io.aurigraph.healthcare.domain.Disability;
import io.aurigraph.healthcare.domain.Patient;
import io.aurigraph.healthcare.domain.PatientDisability;
import io.aurigraph.healthcare.repository.PatientDisabilityRepository;
import io.aurigraph.healthcare.service.PatientDisabilityService;
import io.aurigraph.healthcare.service.criteria.PatientDisabilityCriteria;
import io.aurigraph.healthcare.service.dto.PatientDisabilityDTO;
import io.aurigraph.healthcare.service.mapper.PatientDisabilityMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PatientDisabilityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PatientDisabilityResourceIT {

    private static final String ENTITY_API_URL = "/api/patient-disabilities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PatientDisabilityRepository patientDisabilityRepository;

    @Mock
    private PatientDisabilityRepository patientDisabilityRepositoryMock;

    @Autowired
    private PatientDisabilityMapper patientDisabilityMapper;

    @Mock
    private PatientDisabilityService patientDisabilityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientDisabilityMockMvc;

    private PatientDisability patientDisability;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientDisability createEntity(EntityManager em) {
        PatientDisability patientDisability = new PatientDisability();
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        patientDisability.setPatient(patient);
        // Add required entity
        Disability disability;
        if (TestUtil.findAll(em, Disability.class).isEmpty()) {
            disability = DisabilityResourceIT.createEntity(em);
            em.persist(disability);
            em.flush();
        } else {
            disability = TestUtil.findAll(em, Disability.class).get(0);
        }
        patientDisability.setDisability(disability);
        return patientDisability;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientDisability createUpdatedEntity(EntityManager em) {
        PatientDisability patientDisability = new PatientDisability();
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        patientDisability.setPatient(patient);
        // Add required entity
        Disability disability;
        if (TestUtil.findAll(em, Disability.class).isEmpty()) {
            disability = DisabilityResourceIT.createUpdatedEntity(em);
            em.persist(disability);
            em.flush();
        } else {
            disability = TestUtil.findAll(em, Disability.class).get(0);
        }
        patientDisability.setDisability(disability);
        return patientDisability;
    }

    @BeforeEach
    public void initTest() {
        patientDisability = createEntity(em);
    }

    @Test
    @Transactional
    void createPatientDisability() throws Exception {
        int databaseSizeBeforeCreate = patientDisabilityRepository.findAll().size();
        // Create the PatientDisability
        PatientDisabilityDTO patientDisabilityDTO = patientDisabilityMapper.toDto(patientDisability);
        restPatientDisabilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientDisabilityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PatientDisability in the database
        List<PatientDisability> patientDisabilityList = patientDisabilityRepository.findAll();
        assertThat(patientDisabilityList).hasSize(databaseSizeBeforeCreate + 1);
        PatientDisability testPatientDisability = patientDisabilityList.get(patientDisabilityList.size() - 1);
    }

    @Test
    @Transactional
    void createPatientDisabilityWithExistingId() throws Exception {
        // Create the PatientDisability with an existing ID
        patientDisability.setId(1L);
        PatientDisabilityDTO patientDisabilityDTO = patientDisabilityMapper.toDto(patientDisability);

        int databaseSizeBeforeCreate = patientDisabilityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientDisabilityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientDisabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientDisability in the database
        List<PatientDisability> patientDisabilityList = patientDisabilityRepository.findAll();
        assertThat(patientDisabilityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPatientDisabilities() throws Exception {
        // Initialize the database
        patientDisabilityRepository.saveAndFlush(patientDisability);

        // Get all the patientDisabilityList
        restPatientDisabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patientDisability.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPatientDisabilitiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(patientDisabilityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPatientDisabilityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(patientDisabilityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPatientDisabilitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(patientDisabilityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPatientDisabilityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(patientDisabilityRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPatientDisability() throws Exception {
        // Initialize the database
        patientDisabilityRepository.saveAndFlush(patientDisability);

        // Get the patientDisability
        restPatientDisabilityMockMvc
            .perform(get(ENTITY_API_URL_ID, patientDisability.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patientDisability.getId().intValue()));
    }

    @Test
    @Transactional
    void getPatientDisabilitiesByIdFiltering() throws Exception {
        // Initialize the database
        patientDisabilityRepository.saveAndFlush(patientDisability);

        Long id = patientDisability.getId();

        defaultPatientDisabilityShouldBeFound("id.equals=" + id);
        defaultPatientDisabilityShouldNotBeFound("id.notEquals=" + id);

        defaultPatientDisabilityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPatientDisabilityShouldNotBeFound("id.greaterThan=" + id);

        defaultPatientDisabilityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPatientDisabilityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPatientDisabilitiesByPatientIsEqualToSomething() throws Exception {
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patientDisabilityRepository.saveAndFlush(patientDisability);
            patient = PatientResourceIT.createEntity(em);
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        em.persist(patient);
        em.flush();
        patientDisability.setPatient(patient);
        patientDisabilityRepository.saveAndFlush(patientDisability);
        Long patientId = patient.getId();

        // Get all the patientDisabilityList where patient equals to patientId
        defaultPatientDisabilityShouldBeFound("patientId.equals=" + patientId);

        // Get all the patientDisabilityList where patient equals to (patientId + 1)
        defaultPatientDisabilityShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }

    @Test
    @Transactional
    void getAllPatientDisabilitiesByDisabilityIsEqualToSomething() throws Exception {
        Disability disability;
        if (TestUtil.findAll(em, Disability.class).isEmpty()) {
            patientDisabilityRepository.saveAndFlush(patientDisability);
            disability = DisabilityResourceIT.createEntity(em);
        } else {
            disability = TestUtil.findAll(em, Disability.class).get(0);
        }
        em.persist(disability);
        em.flush();
        patientDisability.setDisability(disability);
        patientDisabilityRepository.saveAndFlush(patientDisability);
        Long disabilityId = disability.getId();

        // Get all the patientDisabilityList where disability equals to disabilityId
        defaultPatientDisabilityShouldBeFound("disabilityId.equals=" + disabilityId);

        // Get all the patientDisabilityList where disability equals to (disabilityId + 1)
        defaultPatientDisabilityShouldNotBeFound("disabilityId.equals=" + (disabilityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPatientDisabilityShouldBeFound(String filter) throws Exception {
        restPatientDisabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patientDisability.getId().intValue())));

        // Check, that the count call also returns 1
        restPatientDisabilityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPatientDisabilityShouldNotBeFound(String filter) throws Exception {
        restPatientDisabilityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPatientDisabilityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPatientDisability() throws Exception {
        // Get the patientDisability
        restPatientDisabilityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPatientDisability() throws Exception {
        // Initialize the database
        patientDisabilityRepository.saveAndFlush(patientDisability);

        int databaseSizeBeforeUpdate = patientDisabilityRepository.findAll().size();

        // Update the patientDisability
        PatientDisability updatedPatientDisability = patientDisabilityRepository.findById(patientDisability.getId()).get();
        // Disconnect from session so that the updates on updatedPatientDisability are not directly saved in db
        em.detach(updatedPatientDisability);
        PatientDisabilityDTO patientDisabilityDTO = patientDisabilityMapper.toDto(updatedPatientDisability);

        restPatientDisabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientDisabilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientDisabilityDTO))
            )
            .andExpect(status().isOk());

        // Validate the PatientDisability in the database
        List<PatientDisability> patientDisabilityList = patientDisabilityRepository.findAll();
        assertThat(patientDisabilityList).hasSize(databaseSizeBeforeUpdate);
        PatientDisability testPatientDisability = patientDisabilityList.get(patientDisabilityList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingPatientDisability() throws Exception {
        int databaseSizeBeforeUpdate = patientDisabilityRepository.findAll().size();
        patientDisability.setId(count.incrementAndGet());

        // Create the PatientDisability
        PatientDisabilityDTO patientDisabilityDTO = patientDisabilityMapper.toDto(patientDisability);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientDisabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientDisabilityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientDisabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientDisability in the database
        List<PatientDisability> patientDisabilityList = patientDisabilityRepository.findAll();
        assertThat(patientDisabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatientDisability() throws Exception {
        int databaseSizeBeforeUpdate = patientDisabilityRepository.findAll().size();
        patientDisability.setId(count.incrementAndGet());

        // Create the PatientDisability
        PatientDisabilityDTO patientDisabilityDTO = patientDisabilityMapper.toDto(patientDisability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientDisabilityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientDisabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientDisability in the database
        List<PatientDisability> patientDisabilityList = patientDisabilityRepository.findAll();
        assertThat(patientDisabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatientDisability() throws Exception {
        int databaseSizeBeforeUpdate = patientDisabilityRepository.findAll().size();
        patientDisability.setId(count.incrementAndGet());

        // Create the PatientDisability
        PatientDisabilityDTO patientDisabilityDTO = patientDisabilityMapper.toDto(patientDisability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientDisabilityMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientDisabilityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatientDisability in the database
        List<PatientDisability> patientDisabilityList = patientDisabilityRepository.findAll();
        assertThat(patientDisabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatientDisabilityWithPatch() throws Exception {
        // Initialize the database
        patientDisabilityRepository.saveAndFlush(patientDisability);

        int databaseSizeBeforeUpdate = patientDisabilityRepository.findAll().size();

        // Update the patientDisability using partial update
        PatientDisability partialUpdatedPatientDisability = new PatientDisability();
        partialUpdatedPatientDisability.setId(patientDisability.getId());

        restPatientDisabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatientDisability.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatientDisability))
            )
            .andExpect(status().isOk());

        // Validate the PatientDisability in the database
        List<PatientDisability> patientDisabilityList = patientDisabilityRepository.findAll();
        assertThat(patientDisabilityList).hasSize(databaseSizeBeforeUpdate);
        PatientDisability testPatientDisability = patientDisabilityList.get(patientDisabilityList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdatePatientDisabilityWithPatch() throws Exception {
        // Initialize the database
        patientDisabilityRepository.saveAndFlush(patientDisability);

        int databaseSizeBeforeUpdate = patientDisabilityRepository.findAll().size();

        // Update the patientDisability using partial update
        PatientDisability partialUpdatedPatientDisability = new PatientDisability();
        partialUpdatedPatientDisability.setId(patientDisability.getId());

        restPatientDisabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatientDisability.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatientDisability))
            )
            .andExpect(status().isOk());

        // Validate the PatientDisability in the database
        List<PatientDisability> patientDisabilityList = patientDisabilityRepository.findAll();
        assertThat(patientDisabilityList).hasSize(databaseSizeBeforeUpdate);
        PatientDisability testPatientDisability = patientDisabilityList.get(patientDisabilityList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingPatientDisability() throws Exception {
        int databaseSizeBeforeUpdate = patientDisabilityRepository.findAll().size();
        patientDisability.setId(count.incrementAndGet());

        // Create the PatientDisability
        PatientDisabilityDTO patientDisabilityDTO = patientDisabilityMapper.toDto(patientDisability);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientDisabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patientDisabilityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientDisabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientDisability in the database
        List<PatientDisability> patientDisabilityList = patientDisabilityRepository.findAll();
        assertThat(patientDisabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatientDisability() throws Exception {
        int databaseSizeBeforeUpdate = patientDisabilityRepository.findAll().size();
        patientDisability.setId(count.incrementAndGet());

        // Create the PatientDisability
        PatientDisabilityDTO patientDisabilityDTO = patientDisabilityMapper.toDto(patientDisability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientDisabilityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientDisabilityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientDisability in the database
        List<PatientDisability> patientDisabilityList = patientDisabilityRepository.findAll();
        assertThat(patientDisabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatientDisability() throws Exception {
        int databaseSizeBeforeUpdate = patientDisabilityRepository.findAll().size();
        patientDisability.setId(count.incrementAndGet());

        // Create the PatientDisability
        PatientDisabilityDTO patientDisabilityDTO = patientDisabilityMapper.toDto(patientDisability);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientDisabilityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientDisabilityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatientDisability in the database
        List<PatientDisability> patientDisabilityList = patientDisabilityRepository.findAll();
        assertThat(patientDisabilityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatientDisability() throws Exception {
        // Initialize the database
        patientDisabilityRepository.saveAndFlush(patientDisability);

        int databaseSizeBeforeDelete = patientDisabilityRepository.findAll().size();

        // Delete the patientDisability
        restPatientDisabilityMockMvc
            .perform(delete(ENTITY_API_URL_ID, patientDisability.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PatientDisability> patientDisabilityList = patientDisabilityRepository.findAll();
        assertThat(patientDisabilityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
