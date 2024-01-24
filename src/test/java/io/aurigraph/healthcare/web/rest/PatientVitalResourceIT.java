package io.aurigraph.healthcare.web.rest;

import static io.aurigraph.healthcare.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.aurigraph.healthcare.IntegrationTest;
import io.aurigraph.healthcare.domain.Patient;
import io.aurigraph.healthcare.domain.PatientVital;
import io.aurigraph.healthcare.repository.PatientVitalRepository;
import io.aurigraph.healthcare.service.PatientVitalService;
import io.aurigraph.healthcare.service.criteria.PatientVitalCriteria;
import io.aurigraph.healthcare.service.dto.PatientVitalDTO;
import io.aurigraph.healthcare.service.mapper.PatientVitalMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link PatientVitalResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PatientVitalResourceIT {

    private static final String DEFAULT_PULSE_RATE = "AAAAAAAAAA";
    private static final String UPDATED_PULSE_RATE = "BBBBBBBBBB";

    private static final String DEFAULT_BLOOD_PRESSURE = "AAAAAAAAAA";
    private static final String UPDATED_BLOOD_PRESSURE = "BBBBBBBBBB";

    private static final String DEFAULT_RESPIRATION = "AAAAAAAAAA";
    private static final String UPDATED_RESPIRATION = "BBBBBBBBBB";

    private static final String DEFAULT_SPO_2 = "AAAAAAAAAA";
    private static final String UPDATED_SPO_2 = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIME_OF_MEASUREMENT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME_OF_MEASUREMENT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_TIME_OF_MEASUREMENT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/patient-vitals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PatientVitalRepository patientVitalRepository;

    @Mock
    private PatientVitalRepository patientVitalRepositoryMock;

    @Autowired
    private PatientVitalMapper patientVitalMapper;

    @Mock
    private PatientVitalService patientVitalServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientVitalMockMvc;

    private PatientVital patientVital;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientVital createEntity(EntityManager em) {
        PatientVital patientVital = new PatientVital()
            .pulseRate(DEFAULT_PULSE_RATE)
            .bloodPressure(DEFAULT_BLOOD_PRESSURE)
            .respiration(DEFAULT_RESPIRATION)
            .spo2(DEFAULT_SPO_2)
            .timeOfMeasurement(DEFAULT_TIME_OF_MEASUREMENT);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        patientVital.setPatient(patient);
        return patientVital;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientVital createUpdatedEntity(EntityManager em) {
        PatientVital patientVital = new PatientVital()
            .pulseRate(UPDATED_PULSE_RATE)
            .bloodPressure(UPDATED_BLOOD_PRESSURE)
            .respiration(UPDATED_RESPIRATION)
            .spo2(UPDATED_SPO_2)
            .timeOfMeasurement(UPDATED_TIME_OF_MEASUREMENT);
        // Add required entity
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patient = PatientResourceIT.createUpdatedEntity(em);
            em.persist(patient);
            em.flush();
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        patientVital.setPatient(patient);
        return patientVital;
    }

    @BeforeEach
    public void initTest() {
        patientVital = createEntity(em);
    }

    @Test
    @Transactional
    void createPatientVital() throws Exception {
        int databaseSizeBeforeCreate = patientVitalRepository.findAll().size();
        // Create the PatientVital
        PatientVitalDTO patientVitalDTO = patientVitalMapper.toDto(patientVital);
        restPatientVitalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientVitalDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PatientVital in the database
        List<PatientVital> patientVitalList = patientVitalRepository.findAll();
        assertThat(patientVitalList).hasSize(databaseSizeBeforeCreate + 1);
        PatientVital testPatientVital = patientVitalList.get(patientVitalList.size() - 1);
        assertThat(testPatientVital.getPulseRate()).isEqualTo(DEFAULT_PULSE_RATE);
        assertThat(testPatientVital.getBloodPressure()).isEqualTo(DEFAULT_BLOOD_PRESSURE);
        assertThat(testPatientVital.getRespiration()).isEqualTo(DEFAULT_RESPIRATION);
        assertThat(testPatientVital.getSpo2()).isEqualTo(DEFAULT_SPO_2);
        assertThat(testPatientVital.getTimeOfMeasurement()).isEqualTo(DEFAULT_TIME_OF_MEASUREMENT);
    }

    @Test
    @Transactional
    void createPatientVitalWithExistingId() throws Exception {
        // Create the PatientVital with an existing ID
        patientVital.setId(1L);
        PatientVitalDTO patientVitalDTO = patientVitalMapper.toDto(patientVital);

        int databaseSizeBeforeCreate = patientVitalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientVitalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientVitalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientVital in the database
        List<PatientVital> patientVitalList = patientVitalRepository.findAll();
        assertThat(patientVitalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTimeOfMeasurementIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientVitalRepository.findAll().size();
        // set the field null
        patientVital.setTimeOfMeasurement(null);

        // Create the PatientVital, which fails.
        PatientVitalDTO patientVitalDTO = patientVitalMapper.toDto(patientVital);

        restPatientVitalMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientVitalDTO))
            )
            .andExpect(status().isBadRequest());

        List<PatientVital> patientVitalList = patientVitalRepository.findAll();
        assertThat(patientVitalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPatientVitals() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList
        restPatientVitalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patientVital.getId().intValue())))
            .andExpect(jsonPath("$.[*].pulseRate").value(hasItem(DEFAULT_PULSE_RATE)))
            .andExpect(jsonPath("$.[*].bloodPressure").value(hasItem(DEFAULT_BLOOD_PRESSURE)))
            .andExpect(jsonPath("$.[*].respiration").value(hasItem(DEFAULT_RESPIRATION)))
            .andExpect(jsonPath("$.[*].spo2").value(hasItem(DEFAULT_SPO_2)))
            .andExpect(jsonPath("$.[*].timeOfMeasurement").value(hasItem(sameInstant(DEFAULT_TIME_OF_MEASUREMENT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPatientVitalsWithEagerRelationshipsIsEnabled() throws Exception {
        when(patientVitalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPatientVitalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(patientVitalServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPatientVitalsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(patientVitalServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPatientVitalMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(patientVitalRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPatientVital() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get the patientVital
        restPatientVitalMockMvc
            .perform(get(ENTITY_API_URL_ID, patientVital.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patientVital.getId().intValue()))
            .andExpect(jsonPath("$.pulseRate").value(DEFAULT_PULSE_RATE))
            .andExpect(jsonPath("$.bloodPressure").value(DEFAULT_BLOOD_PRESSURE))
            .andExpect(jsonPath("$.respiration").value(DEFAULT_RESPIRATION))
            .andExpect(jsonPath("$.spo2").value(DEFAULT_SPO_2))
            .andExpect(jsonPath("$.timeOfMeasurement").value(sameInstant(DEFAULT_TIME_OF_MEASUREMENT)));
    }

    @Test
    @Transactional
    void getPatientVitalsByIdFiltering() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        Long id = patientVital.getId();

        defaultPatientVitalShouldBeFound("id.equals=" + id);
        defaultPatientVitalShouldNotBeFound("id.notEquals=" + id);

        defaultPatientVitalShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPatientVitalShouldNotBeFound("id.greaterThan=" + id);

        defaultPatientVitalShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPatientVitalShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByPulseRateIsEqualToSomething() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where pulseRate equals to DEFAULT_PULSE_RATE
        defaultPatientVitalShouldBeFound("pulseRate.equals=" + DEFAULT_PULSE_RATE);

        // Get all the patientVitalList where pulseRate equals to UPDATED_PULSE_RATE
        defaultPatientVitalShouldNotBeFound("pulseRate.equals=" + UPDATED_PULSE_RATE);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByPulseRateIsInShouldWork() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where pulseRate in DEFAULT_PULSE_RATE or UPDATED_PULSE_RATE
        defaultPatientVitalShouldBeFound("pulseRate.in=" + DEFAULT_PULSE_RATE + "," + UPDATED_PULSE_RATE);

        // Get all the patientVitalList where pulseRate equals to UPDATED_PULSE_RATE
        defaultPatientVitalShouldNotBeFound("pulseRate.in=" + UPDATED_PULSE_RATE);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByPulseRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where pulseRate is not null
        defaultPatientVitalShouldBeFound("pulseRate.specified=true");

        // Get all the patientVitalList where pulseRate is null
        defaultPatientVitalShouldNotBeFound("pulseRate.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientVitalsByPulseRateContainsSomething() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where pulseRate contains DEFAULT_PULSE_RATE
        defaultPatientVitalShouldBeFound("pulseRate.contains=" + DEFAULT_PULSE_RATE);

        // Get all the patientVitalList where pulseRate contains UPDATED_PULSE_RATE
        defaultPatientVitalShouldNotBeFound("pulseRate.contains=" + UPDATED_PULSE_RATE);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByPulseRateNotContainsSomething() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where pulseRate does not contain DEFAULT_PULSE_RATE
        defaultPatientVitalShouldNotBeFound("pulseRate.doesNotContain=" + DEFAULT_PULSE_RATE);

        // Get all the patientVitalList where pulseRate does not contain UPDATED_PULSE_RATE
        defaultPatientVitalShouldBeFound("pulseRate.doesNotContain=" + UPDATED_PULSE_RATE);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByBloodPressureIsEqualToSomething() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where bloodPressure equals to DEFAULT_BLOOD_PRESSURE
        defaultPatientVitalShouldBeFound("bloodPressure.equals=" + DEFAULT_BLOOD_PRESSURE);

        // Get all the patientVitalList where bloodPressure equals to UPDATED_BLOOD_PRESSURE
        defaultPatientVitalShouldNotBeFound("bloodPressure.equals=" + UPDATED_BLOOD_PRESSURE);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByBloodPressureIsInShouldWork() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where bloodPressure in DEFAULT_BLOOD_PRESSURE or UPDATED_BLOOD_PRESSURE
        defaultPatientVitalShouldBeFound("bloodPressure.in=" + DEFAULT_BLOOD_PRESSURE + "," + UPDATED_BLOOD_PRESSURE);

        // Get all the patientVitalList where bloodPressure equals to UPDATED_BLOOD_PRESSURE
        defaultPatientVitalShouldNotBeFound("bloodPressure.in=" + UPDATED_BLOOD_PRESSURE);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByBloodPressureIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where bloodPressure is not null
        defaultPatientVitalShouldBeFound("bloodPressure.specified=true");

        // Get all the patientVitalList where bloodPressure is null
        defaultPatientVitalShouldNotBeFound("bloodPressure.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientVitalsByBloodPressureContainsSomething() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where bloodPressure contains DEFAULT_BLOOD_PRESSURE
        defaultPatientVitalShouldBeFound("bloodPressure.contains=" + DEFAULT_BLOOD_PRESSURE);

        // Get all the patientVitalList where bloodPressure contains UPDATED_BLOOD_PRESSURE
        defaultPatientVitalShouldNotBeFound("bloodPressure.contains=" + UPDATED_BLOOD_PRESSURE);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByBloodPressureNotContainsSomething() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where bloodPressure does not contain DEFAULT_BLOOD_PRESSURE
        defaultPatientVitalShouldNotBeFound("bloodPressure.doesNotContain=" + DEFAULT_BLOOD_PRESSURE);

        // Get all the patientVitalList where bloodPressure does not contain UPDATED_BLOOD_PRESSURE
        defaultPatientVitalShouldBeFound("bloodPressure.doesNotContain=" + UPDATED_BLOOD_PRESSURE);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByRespirationIsEqualToSomething() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where respiration equals to DEFAULT_RESPIRATION
        defaultPatientVitalShouldBeFound("respiration.equals=" + DEFAULT_RESPIRATION);

        // Get all the patientVitalList where respiration equals to UPDATED_RESPIRATION
        defaultPatientVitalShouldNotBeFound("respiration.equals=" + UPDATED_RESPIRATION);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByRespirationIsInShouldWork() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where respiration in DEFAULT_RESPIRATION or UPDATED_RESPIRATION
        defaultPatientVitalShouldBeFound("respiration.in=" + DEFAULT_RESPIRATION + "," + UPDATED_RESPIRATION);

        // Get all the patientVitalList where respiration equals to UPDATED_RESPIRATION
        defaultPatientVitalShouldNotBeFound("respiration.in=" + UPDATED_RESPIRATION);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByRespirationIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where respiration is not null
        defaultPatientVitalShouldBeFound("respiration.specified=true");

        // Get all the patientVitalList where respiration is null
        defaultPatientVitalShouldNotBeFound("respiration.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientVitalsByRespirationContainsSomething() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where respiration contains DEFAULT_RESPIRATION
        defaultPatientVitalShouldBeFound("respiration.contains=" + DEFAULT_RESPIRATION);

        // Get all the patientVitalList where respiration contains UPDATED_RESPIRATION
        defaultPatientVitalShouldNotBeFound("respiration.contains=" + UPDATED_RESPIRATION);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByRespirationNotContainsSomething() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where respiration does not contain DEFAULT_RESPIRATION
        defaultPatientVitalShouldNotBeFound("respiration.doesNotContain=" + DEFAULT_RESPIRATION);

        // Get all the patientVitalList where respiration does not contain UPDATED_RESPIRATION
        defaultPatientVitalShouldBeFound("respiration.doesNotContain=" + UPDATED_RESPIRATION);
    }

    @Test
    @Transactional
    void getAllPatientVitalsBySpo2IsEqualToSomething() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where spo2 equals to DEFAULT_SPO_2
        defaultPatientVitalShouldBeFound("spo2.equals=" + DEFAULT_SPO_2);

        // Get all the patientVitalList where spo2 equals to UPDATED_SPO_2
        defaultPatientVitalShouldNotBeFound("spo2.equals=" + UPDATED_SPO_2);
    }

    @Test
    @Transactional
    void getAllPatientVitalsBySpo2IsInShouldWork() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where spo2 in DEFAULT_SPO_2 or UPDATED_SPO_2
        defaultPatientVitalShouldBeFound("spo2.in=" + DEFAULT_SPO_2 + "," + UPDATED_SPO_2);

        // Get all the patientVitalList where spo2 equals to UPDATED_SPO_2
        defaultPatientVitalShouldNotBeFound("spo2.in=" + UPDATED_SPO_2);
    }

    @Test
    @Transactional
    void getAllPatientVitalsBySpo2IsNullOrNotNull() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where spo2 is not null
        defaultPatientVitalShouldBeFound("spo2.specified=true");

        // Get all the patientVitalList where spo2 is null
        defaultPatientVitalShouldNotBeFound("spo2.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientVitalsBySpo2ContainsSomething() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where spo2 contains DEFAULT_SPO_2
        defaultPatientVitalShouldBeFound("spo2.contains=" + DEFAULT_SPO_2);

        // Get all the patientVitalList where spo2 contains UPDATED_SPO_2
        defaultPatientVitalShouldNotBeFound("spo2.contains=" + UPDATED_SPO_2);
    }

    @Test
    @Transactional
    void getAllPatientVitalsBySpo2NotContainsSomething() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where spo2 does not contain DEFAULT_SPO_2
        defaultPatientVitalShouldNotBeFound("spo2.doesNotContain=" + DEFAULT_SPO_2);

        // Get all the patientVitalList where spo2 does not contain UPDATED_SPO_2
        defaultPatientVitalShouldBeFound("spo2.doesNotContain=" + UPDATED_SPO_2);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByTimeOfMeasurementIsEqualToSomething() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where timeOfMeasurement equals to DEFAULT_TIME_OF_MEASUREMENT
        defaultPatientVitalShouldBeFound("timeOfMeasurement.equals=" + DEFAULT_TIME_OF_MEASUREMENT);

        // Get all the patientVitalList where timeOfMeasurement equals to UPDATED_TIME_OF_MEASUREMENT
        defaultPatientVitalShouldNotBeFound("timeOfMeasurement.equals=" + UPDATED_TIME_OF_MEASUREMENT);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByTimeOfMeasurementIsInShouldWork() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where timeOfMeasurement in DEFAULT_TIME_OF_MEASUREMENT or UPDATED_TIME_OF_MEASUREMENT
        defaultPatientVitalShouldBeFound("timeOfMeasurement.in=" + DEFAULT_TIME_OF_MEASUREMENT + "," + UPDATED_TIME_OF_MEASUREMENT);

        // Get all the patientVitalList where timeOfMeasurement equals to UPDATED_TIME_OF_MEASUREMENT
        defaultPatientVitalShouldNotBeFound("timeOfMeasurement.in=" + UPDATED_TIME_OF_MEASUREMENT);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByTimeOfMeasurementIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where timeOfMeasurement is not null
        defaultPatientVitalShouldBeFound("timeOfMeasurement.specified=true");

        // Get all the patientVitalList where timeOfMeasurement is null
        defaultPatientVitalShouldNotBeFound("timeOfMeasurement.specified=false");
    }

    @Test
    @Transactional
    void getAllPatientVitalsByTimeOfMeasurementIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where timeOfMeasurement is greater than or equal to DEFAULT_TIME_OF_MEASUREMENT
        defaultPatientVitalShouldBeFound("timeOfMeasurement.greaterThanOrEqual=" + DEFAULT_TIME_OF_MEASUREMENT);

        // Get all the patientVitalList where timeOfMeasurement is greater than or equal to UPDATED_TIME_OF_MEASUREMENT
        defaultPatientVitalShouldNotBeFound("timeOfMeasurement.greaterThanOrEqual=" + UPDATED_TIME_OF_MEASUREMENT);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByTimeOfMeasurementIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where timeOfMeasurement is less than or equal to DEFAULT_TIME_OF_MEASUREMENT
        defaultPatientVitalShouldBeFound("timeOfMeasurement.lessThanOrEqual=" + DEFAULT_TIME_OF_MEASUREMENT);

        // Get all the patientVitalList where timeOfMeasurement is less than or equal to SMALLER_TIME_OF_MEASUREMENT
        defaultPatientVitalShouldNotBeFound("timeOfMeasurement.lessThanOrEqual=" + SMALLER_TIME_OF_MEASUREMENT);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByTimeOfMeasurementIsLessThanSomething() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where timeOfMeasurement is less than DEFAULT_TIME_OF_MEASUREMENT
        defaultPatientVitalShouldNotBeFound("timeOfMeasurement.lessThan=" + DEFAULT_TIME_OF_MEASUREMENT);

        // Get all the patientVitalList where timeOfMeasurement is less than UPDATED_TIME_OF_MEASUREMENT
        defaultPatientVitalShouldBeFound("timeOfMeasurement.lessThan=" + UPDATED_TIME_OF_MEASUREMENT);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByTimeOfMeasurementIsGreaterThanSomething() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        // Get all the patientVitalList where timeOfMeasurement is greater than DEFAULT_TIME_OF_MEASUREMENT
        defaultPatientVitalShouldNotBeFound("timeOfMeasurement.greaterThan=" + DEFAULT_TIME_OF_MEASUREMENT);

        // Get all the patientVitalList where timeOfMeasurement is greater than SMALLER_TIME_OF_MEASUREMENT
        defaultPatientVitalShouldBeFound("timeOfMeasurement.greaterThan=" + SMALLER_TIME_OF_MEASUREMENT);
    }

    @Test
    @Transactional
    void getAllPatientVitalsByPatientIsEqualToSomething() throws Exception {
        Patient patient;
        if (TestUtil.findAll(em, Patient.class).isEmpty()) {
            patientVitalRepository.saveAndFlush(patientVital);
            patient = PatientResourceIT.createEntity(em);
        } else {
            patient = TestUtil.findAll(em, Patient.class).get(0);
        }
        em.persist(patient);
        em.flush();
        patientVital.setPatient(patient);
        patientVitalRepository.saveAndFlush(patientVital);
        Long patientId = patient.getId();

        // Get all the patientVitalList where patient equals to patientId
        defaultPatientVitalShouldBeFound("patientId.equals=" + patientId);

        // Get all the patientVitalList where patient equals to (patientId + 1)
        defaultPatientVitalShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPatientVitalShouldBeFound(String filter) throws Exception {
        restPatientVitalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patientVital.getId().intValue())))
            .andExpect(jsonPath("$.[*].pulseRate").value(hasItem(DEFAULT_PULSE_RATE)))
            .andExpect(jsonPath("$.[*].bloodPressure").value(hasItem(DEFAULT_BLOOD_PRESSURE)))
            .andExpect(jsonPath("$.[*].respiration").value(hasItem(DEFAULT_RESPIRATION)))
            .andExpect(jsonPath("$.[*].spo2").value(hasItem(DEFAULT_SPO_2)))
            .andExpect(jsonPath("$.[*].timeOfMeasurement").value(hasItem(sameInstant(DEFAULT_TIME_OF_MEASUREMENT))));

        // Check, that the count call also returns 1
        restPatientVitalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPatientVitalShouldNotBeFound(String filter) throws Exception {
        restPatientVitalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPatientVitalMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPatientVital() throws Exception {
        // Get the patientVital
        restPatientVitalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPatientVital() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        int databaseSizeBeforeUpdate = patientVitalRepository.findAll().size();

        // Update the patientVital
        PatientVital updatedPatientVital = patientVitalRepository.findById(patientVital.getId()).get();
        // Disconnect from session so that the updates on updatedPatientVital are not directly saved in db
        em.detach(updatedPatientVital);
        updatedPatientVital
            .pulseRate(UPDATED_PULSE_RATE)
            .bloodPressure(UPDATED_BLOOD_PRESSURE)
            .respiration(UPDATED_RESPIRATION)
            .spo2(UPDATED_SPO_2)
            .timeOfMeasurement(UPDATED_TIME_OF_MEASUREMENT);
        PatientVitalDTO patientVitalDTO = patientVitalMapper.toDto(updatedPatientVital);

        restPatientVitalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientVitalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientVitalDTO))
            )
            .andExpect(status().isOk());

        // Validate the PatientVital in the database
        List<PatientVital> patientVitalList = patientVitalRepository.findAll();
        assertThat(patientVitalList).hasSize(databaseSizeBeforeUpdate);
        PatientVital testPatientVital = patientVitalList.get(patientVitalList.size() - 1);
        assertThat(testPatientVital.getPulseRate()).isEqualTo(UPDATED_PULSE_RATE);
        assertThat(testPatientVital.getBloodPressure()).isEqualTo(UPDATED_BLOOD_PRESSURE);
        assertThat(testPatientVital.getRespiration()).isEqualTo(UPDATED_RESPIRATION);
        assertThat(testPatientVital.getSpo2()).isEqualTo(UPDATED_SPO_2);
        assertThat(testPatientVital.getTimeOfMeasurement()).isEqualTo(UPDATED_TIME_OF_MEASUREMENT);
    }

    @Test
    @Transactional
    void putNonExistingPatientVital() throws Exception {
        int databaseSizeBeforeUpdate = patientVitalRepository.findAll().size();
        patientVital.setId(count.incrementAndGet());

        // Create the PatientVital
        PatientVitalDTO patientVitalDTO = patientVitalMapper.toDto(patientVital);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientVitalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientVitalDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientVitalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientVital in the database
        List<PatientVital> patientVitalList = patientVitalRepository.findAll();
        assertThat(patientVitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatientVital() throws Exception {
        int databaseSizeBeforeUpdate = patientVitalRepository.findAll().size();
        patientVital.setId(count.incrementAndGet());

        // Create the PatientVital
        PatientVitalDTO patientVitalDTO = patientVitalMapper.toDto(patientVital);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientVitalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientVitalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientVital in the database
        List<PatientVital> patientVitalList = patientVitalRepository.findAll();
        assertThat(patientVitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatientVital() throws Exception {
        int databaseSizeBeforeUpdate = patientVitalRepository.findAll().size();
        patientVital.setId(count.incrementAndGet());

        // Create the PatientVital
        PatientVitalDTO patientVitalDTO = patientVitalMapper.toDto(patientVital);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientVitalMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientVitalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatientVital in the database
        List<PatientVital> patientVitalList = patientVitalRepository.findAll();
        assertThat(patientVitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatientVitalWithPatch() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        int databaseSizeBeforeUpdate = patientVitalRepository.findAll().size();

        // Update the patientVital using partial update
        PatientVital partialUpdatedPatientVital = new PatientVital();
        partialUpdatedPatientVital.setId(patientVital.getId());

        partialUpdatedPatientVital.pulseRate(UPDATED_PULSE_RATE).spo2(UPDATED_SPO_2);

        restPatientVitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatientVital.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatientVital))
            )
            .andExpect(status().isOk());

        // Validate the PatientVital in the database
        List<PatientVital> patientVitalList = patientVitalRepository.findAll();
        assertThat(patientVitalList).hasSize(databaseSizeBeforeUpdate);
        PatientVital testPatientVital = patientVitalList.get(patientVitalList.size() - 1);
        assertThat(testPatientVital.getPulseRate()).isEqualTo(UPDATED_PULSE_RATE);
        assertThat(testPatientVital.getBloodPressure()).isEqualTo(DEFAULT_BLOOD_PRESSURE);
        assertThat(testPatientVital.getRespiration()).isEqualTo(DEFAULT_RESPIRATION);
        assertThat(testPatientVital.getSpo2()).isEqualTo(UPDATED_SPO_2);
        assertThat(testPatientVital.getTimeOfMeasurement()).isEqualTo(DEFAULT_TIME_OF_MEASUREMENT);
    }

    @Test
    @Transactional
    void fullUpdatePatientVitalWithPatch() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        int databaseSizeBeforeUpdate = patientVitalRepository.findAll().size();

        // Update the patientVital using partial update
        PatientVital partialUpdatedPatientVital = new PatientVital();
        partialUpdatedPatientVital.setId(patientVital.getId());

        partialUpdatedPatientVital
            .pulseRate(UPDATED_PULSE_RATE)
            .bloodPressure(UPDATED_BLOOD_PRESSURE)
            .respiration(UPDATED_RESPIRATION)
            .spo2(UPDATED_SPO_2)
            .timeOfMeasurement(UPDATED_TIME_OF_MEASUREMENT);

        restPatientVitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatientVital.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatientVital))
            )
            .andExpect(status().isOk());

        // Validate the PatientVital in the database
        List<PatientVital> patientVitalList = patientVitalRepository.findAll();
        assertThat(patientVitalList).hasSize(databaseSizeBeforeUpdate);
        PatientVital testPatientVital = patientVitalList.get(patientVitalList.size() - 1);
        assertThat(testPatientVital.getPulseRate()).isEqualTo(UPDATED_PULSE_RATE);
        assertThat(testPatientVital.getBloodPressure()).isEqualTo(UPDATED_BLOOD_PRESSURE);
        assertThat(testPatientVital.getRespiration()).isEqualTo(UPDATED_RESPIRATION);
        assertThat(testPatientVital.getSpo2()).isEqualTo(UPDATED_SPO_2);
        assertThat(testPatientVital.getTimeOfMeasurement()).isEqualTo(UPDATED_TIME_OF_MEASUREMENT);
    }

    @Test
    @Transactional
    void patchNonExistingPatientVital() throws Exception {
        int databaseSizeBeforeUpdate = patientVitalRepository.findAll().size();
        patientVital.setId(count.incrementAndGet());

        // Create the PatientVital
        PatientVitalDTO patientVitalDTO = patientVitalMapper.toDto(patientVital);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientVitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patientVitalDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientVitalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientVital in the database
        List<PatientVital> patientVitalList = patientVitalRepository.findAll();
        assertThat(patientVitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatientVital() throws Exception {
        int databaseSizeBeforeUpdate = patientVitalRepository.findAll().size();
        patientVital.setId(count.incrementAndGet());

        // Create the PatientVital
        PatientVitalDTO patientVitalDTO = patientVitalMapper.toDto(patientVital);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientVitalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientVitalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientVital in the database
        List<PatientVital> patientVitalList = patientVitalRepository.findAll();
        assertThat(patientVitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatientVital() throws Exception {
        int databaseSizeBeforeUpdate = patientVitalRepository.findAll().size();
        patientVital.setId(count.incrementAndGet());

        // Create the PatientVital
        PatientVitalDTO patientVitalDTO = patientVitalMapper.toDto(patientVital);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientVitalMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientVitalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatientVital in the database
        List<PatientVital> patientVitalList = patientVitalRepository.findAll();
        assertThat(patientVitalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatientVital() throws Exception {
        // Initialize the database
        patientVitalRepository.saveAndFlush(patientVital);

        int databaseSizeBeforeDelete = patientVitalRepository.findAll().size();

        // Delete the patientVital
        restPatientVitalMockMvc
            .perform(delete(ENTITY_API_URL_ID, patientVital.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PatientVital> patientVitalList = patientVitalRepository.findAll();
        assertThat(patientVitalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
