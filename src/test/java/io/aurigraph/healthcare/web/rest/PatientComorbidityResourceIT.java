package io.aurigraph.healthcare.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.aurigraph.healthcare.IntegrationTest;
import io.aurigraph.healthcare.domain.Comorbidity;
import io.aurigraph.healthcare.domain.Patient;
import io.aurigraph.healthcare.domain.PatientComorbidity;
import io.aurigraph.healthcare.repository.PatientComorbidityRepository;
import io.aurigraph.healthcare.service.PatientComorbidityService;
import io.aurigraph.healthcare.service.criteria.PatientComorbidityCriteria;
import io.aurigraph.healthcare.service.dto.PatientComorbidityDTO;
import io.aurigraph.healthcare.service.mapper.PatientComorbidityMapper;
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
 * Integration tests for the {@link PatientComorbidityResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PatientComorbidityResourceIT {

    private static final String ENTITY_API_URL = "/api/patient-comorbidities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PatientComorbidityRepository patientComorbidityRepository;

    @Mock
    private PatientComorbidityRepository patientComorbidityRepositoryMock;

    @Autowired
    private PatientComorbidityMapper patientComorbidityMapper;

    @Mock
    private PatientComorbidityService patientComorbidityServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientComorbidityMockMvc;

    private PatientComorbidity patientComorbidity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientComorbidity createEntity(EntityManager em) {
        PatientComorbidity patientComorbidity = new PatientComorbidity();
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        patientComorbidity.setPatient(patient);
        // Add required entity
        Comorbidity comorbidity;
        if (TestUtil.findAll(em, Comorbidity.class).isEmpty()) {
            comorbidity = ComorbidityResourceIT.createEntity(em);
            em.persist(comorbidity);
            em.flush();
        } else {
            comorbidity = TestUtil.findAll(em, Comorbidity.class).get(0);
        }
        patientComorbidity.setComorbidity(comorbidity);
        return patientComorbidity;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientComorbidity createUpdatedEntity(EntityManager em) {
        PatientComorbidity patientComorbidity = new PatientComorbidity();
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        patientComorbidity.setPatient(patient);
        // Add required entity
        Comorbidity comorbidity;
        if (TestUtil.findAll(em, Comorbidity.class).isEmpty()) {
            comorbidity = ComorbidityResourceIT.createUpdatedEntity(em);
            em.persist(comorbidity);
            em.flush();
        } else {
            comorbidity = TestUtil.findAll(em, Comorbidity.class).get(0);
        }
        patientComorbidity.setComorbidity(comorbidity);
        return patientComorbidity;
    }

    @BeforeEach
    public void initTest() {
        patientComorbidity = createEntity(em);
    }

    @Test
    @Transactional
    void createPatientComorbidity() throws Exception {
        int databaseSizeBeforeCreate = patientComorbidityRepository.findAll().size();
        // Create the PatientComorbidity
        PatientComorbidityDTO patientComorbidityDTO = patientComorbidityMapper.toDto(patientComorbidity);
        restPatientComorbidityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientComorbidityDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PatientComorbidity in the database
        List<PatientComorbidity> patientComorbidityList = patientComorbidityRepository.findAll();
        assertThat(patientComorbidityList).hasSize(databaseSizeBeforeCreate + 1);
        PatientComorbidity testPatientComorbidity = patientComorbidityList.get(patientComorbidityList.size() - 1);
    }

    @Test
    @Transactional
    void createPatientComorbidityWithExistingId() throws Exception {
        // Create the PatientComorbidity with an existing ID
        patientComorbidity.setId(1L);
        PatientComorbidityDTO patientComorbidityDTO = patientComorbidityMapper.toDto(patientComorbidity);

        int databaseSizeBeforeCreate = patientComorbidityRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientComorbidityMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientComorbidityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientComorbidity in the database
        List<PatientComorbidity> patientComorbidityList = patientComorbidityRepository.findAll();
        assertThat(patientComorbidityList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPatientComorbidities() throws Exception {
        // Initialize the database
        patientComorbidityRepository.saveAndFlush(patientComorbidity);

        // Get all the patientComorbidityList
        restPatientComorbidityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patientComorbidity.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPatientComorbiditiesWithEagerRelationshipsIsEnabled() throws Exception {
        when(patientComorbidityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPatientComorbidityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(patientComorbidityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPatientComorbiditiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(patientComorbidityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPatientComorbidityMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(patientComorbidityRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPatientComorbidity() throws Exception {
        // Initialize the database
        patientComorbidityRepository.saveAndFlush(patientComorbidity);

        // Get the patientComorbidity
        restPatientComorbidityMockMvc
            .perform(get(ENTITY_API_URL_ID, patientComorbidity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patientComorbidity.getId().intValue()));
    }

    @Test
    @Transactional
    void getPatientComorbiditiesByIdFiltering() throws Exception {
        // Initialize the database
        patientComorbidityRepository.saveAndFlush(patientComorbidity);

        Long id = patientComorbidity.getId();

        defaultPatientComorbidityShouldBeFound("id.equals=" + id);
        defaultPatientComorbidityShouldNotBeFound("id.notEquals=" + id);

        defaultPatientComorbidityShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPatientComorbidityShouldNotBeFound("id.greaterThan=" + id);

        defaultPatientComorbidityShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPatientComorbidityShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPatientComorbiditiesByPatientIsEqualToSomething() throws Exception {
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patientComorbidityRepository.saveAndFlush(patientComorbidity);
            patient = PatientResourceIT.createEntity(em);
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        em.persist(patient);
        em.flush();
        patientComorbidity.setPatient(patient);
        patientComorbidityRepository.saveAndFlush(patientComorbidity);
        Long patientId = patient.getId();

        // Get all the patientComorbidityList where patient equals to patientId
        defaultPatientComorbidityShouldBeFound("patientId.equals=" + patientId);

        // Get all the patientComorbidityList where patient equals to (patientId + 1)
        defaultPatientComorbidityShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }

    @Test
    @Transactional
    void getAllPatientComorbiditiesByComorbidityIsEqualToSomething() throws Exception {
        Comorbidity comorbidity;
        if (TestUtil.findAll(em, Comorbidity.class).isEmpty()) {
            patientComorbidityRepository.saveAndFlush(patientComorbidity);
            comorbidity = ComorbidityResourceIT.createEntity(em);
        } else {
            comorbidity = TestUtil.findAll(em, Comorbidity.class).get(0);
        }
        em.persist(comorbidity);
        em.flush();
        patientComorbidity.setComorbidity(comorbidity);
        patientComorbidityRepository.saveAndFlush(patientComorbidity);
        Long comorbidityId = comorbidity.getId();

        // Get all the patientComorbidityList where comorbidity equals to comorbidityId
        defaultPatientComorbidityShouldBeFound("comorbidityId.equals=" + comorbidityId);

        // Get all the patientComorbidityList where comorbidity equals to (comorbidityId + 1)
        defaultPatientComorbidityShouldNotBeFound("comorbidityId.equals=" + (comorbidityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPatientComorbidityShouldBeFound(String filter) throws Exception {
        restPatientComorbidityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patientComorbidity.getId().intValue())));

        // Check, that the count call also returns 1
        restPatientComorbidityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPatientComorbidityShouldNotBeFound(String filter) throws Exception {
        restPatientComorbidityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPatientComorbidityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPatientComorbidity() throws Exception {
        // Get the patientComorbidity
        restPatientComorbidityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPatientComorbidity() throws Exception {
        // Initialize the database
        patientComorbidityRepository.saveAndFlush(patientComorbidity);

        int databaseSizeBeforeUpdate = patientComorbidityRepository.findAll().size();

        // Update the patientComorbidity
        PatientComorbidity updatedPatientComorbidity = patientComorbidityRepository.findById(patientComorbidity.getId()).get();
        // Disconnect from session so that the updates on updatedPatientComorbidity are not directly saved in db
        em.detach(updatedPatientComorbidity);
        PatientComorbidityDTO patientComorbidityDTO = patientComorbidityMapper.toDto(updatedPatientComorbidity);

        restPatientComorbidityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientComorbidityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientComorbidityDTO))
            )
            .andExpect(status().isOk());

        // Validate the PatientComorbidity in the database
        List<PatientComorbidity> patientComorbidityList = patientComorbidityRepository.findAll();
        assertThat(patientComorbidityList).hasSize(databaseSizeBeforeUpdate);
        PatientComorbidity testPatientComorbidity = patientComorbidityList.get(patientComorbidityList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingPatientComorbidity() throws Exception {
        int databaseSizeBeforeUpdate = patientComorbidityRepository.findAll().size();
        patientComorbidity.setId(count.incrementAndGet());

        // Create the PatientComorbidity
        PatientComorbidityDTO patientComorbidityDTO = patientComorbidityMapper.toDto(patientComorbidity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientComorbidityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientComorbidityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientComorbidityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientComorbidity in the database
        List<PatientComorbidity> patientComorbidityList = patientComorbidityRepository.findAll();
        assertThat(patientComorbidityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatientComorbidity() throws Exception {
        int databaseSizeBeforeUpdate = patientComorbidityRepository.findAll().size();
        patientComorbidity.setId(count.incrementAndGet());

        // Create the PatientComorbidity
        PatientComorbidityDTO patientComorbidityDTO = patientComorbidityMapper.toDto(patientComorbidity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientComorbidityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientComorbidityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientComorbidity in the database
        List<PatientComorbidity> patientComorbidityList = patientComorbidityRepository.findAll();
        assertThat(patientComorbidityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatientComorbidity() throws Exception {
        int databaseSizeBeforeUpdate = patientComorbidityRepository.findAll().size();
        patientComorbidity.setId(count.incrementAndGet());

        // Create the PatientComorbidity
        PatientComorbidityDTO patientComorbidityDTO = patientComorbidityMapper.toDto(patientComorbidity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientComorbidityMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientComorbidityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatientComorbidity in the database
        List<PatientComorbidity> patientComorbidityList = patientComorbidityRepository.findAll();
        assertThat(patientComorbidityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatientComorbidityWithPatch() throws Exception {
        // Initialize the database
        patientComorbidityRepository.saveAndFlush(patientComorbidity);

        int databaseSizeBeforeUpdate = patientComorbidityRepository.findAll().size();

        // Update the patientComorbidity using partial update
        PatientComorbidity partialUpdatedPatientComorbidity = new PatientComorbidity();
        partialUpdatedPatientComorbidity.setId(patientComorbidity.getId());

        restPatientComorbidityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatientComorbidity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatientComorbidity))
            )
            .andExpect(status().isOk());

        // Validate the PatientComorbidity in the database
        List<PatientComorbidity> patientComorbidityList = patientComorbidityRepository.findAll();
        assertThat(patientComorbidityList).hasSize(databaseSizeBeforeUpdate);
        PatientComorbidity testPatientComorbidity = patientComorbidityList.get(patientComorbidityList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdatePatientComorbidityWithPatch() throws Exception {
        // Initialize the database
        patientComorbidityRepository.saveAndFlush(patientComorbidity);

        int databaseSizeBeforeUpdate = patientComorbidityRepository.findAll().size();

        // Update the patientComorbidity using partial update
        PatientComorbidity partialUpdatedPatientComorbidity = new PatientComorbidity();
        partialUpdatedPatientComorbidity.setId(patientComorbidity.getId());

        restPatientComorbidityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatientComorbidity.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatientComorbidity))
            )
            .andExpect(status().isOk());

        // Validate the PatientComorbidity in the database
        List<PatientComorbidity> patientComorbidityList = patientComorbidityRepository.findAll();
        assertThat(patientComorbidityList).hasSize(databaseSizeBeforeUpdate);
        PatientComorbidity testPatientComorbidity = patientComorbidityList.get(patientComorbidityList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingPatientComorbidity() throws Exception {
        int databaseSizeBeforeUpdate = patientComorbidityRepository.findAll().size();
        patientComorbidity.setId(count.incrementAndGet());

        // Create the PatientComorbidity
        PatientComorbidityDTO patientComorbidityDTO = patientComorbidityMapper.toDto(patientComorbidity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientComorbidityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patientComorbidityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientComorbidityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientComorbidity in the database
        List<PatientComorbidity> patientComorbidityList = patientComorbidityRepository.findAll();
        assertThat(patientComorbidityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatientComorbidity() throws Exception {
        int databaseSizeBeforeUpdate = patientComorbidityRepository.findAll().size();
        patientComorbidity.setId(count.incrementAndGet());

        // Create the PatientComorbidity
        PatientComorbidityDTO patientComorbidityDTO = patientComorbidityMapper.toDto(patientComorbidity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientComorbidityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientComorbidityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientComorbidity in the database
        List<PatientComorbidity> patientComorbidityList = patientComorbidityRepository.findAll();
        assertThat(patientComorbidityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatientComorbidity() throws Exception {
        int databaseSizeBeforeUpdate = patientComorbidityRepository.findAll().size();
        patientComorbidity.setId(count.incrementAndGet());

        // Create the PatientComorbidity
        PatientComorbidityDTO patientComorbidityDTO = patientComorbidityMapper.toDto(patientComorbidity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientComorbidityMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientComorbidityDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatientComorbidity in the database
        List<PatientComorbidity> patientComorbidityList = patientComorbidityRepository.findAll();
        assertThat(patientComorbidityList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatientComorbidity() throws Exception {
        // Initialize the database
        patientComorbidityRepository.saveAndFlush(patientComorbidity);

        int databaseSizeBeforeDelete = patientComorbidityRepository.findAll().size();

        // Delete the patientComorbidity
        restPatientComorbidityMockMvc
            .perform(delete(ENTITY_API_URL_ID, patientComorbidity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PatientComorbidity> patientComorbidityList = patientComorbidityRepository.findAll();
        assertThat(patientComorbidityList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
