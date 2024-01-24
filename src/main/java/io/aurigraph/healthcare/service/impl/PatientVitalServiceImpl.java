package io.aurigraph.healthcare.service.impl;

import io.aurigraph.healthcare.domain.PatientVital;
import io.aurigraph.healthcare.repository.PatientVitalRepository;
import io.aurigraph.healthcare.service.PatientVitalService;
import io.aurigraph.healthcare.service.dto.PatientVitalDTO;
import io.aurigraph.healthcare.service.mapper.PatientVitalMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PatientVital}.
 */
@Service
@Transactional
public class PatientVitalServiceImpl implements PatientVitalService {

    private final Logger log = LoggerFactory.getLogger(PatientVitalServiceImpl.class);

    private final PatientVitalRepository patientVitalRepository;

    private final PatientVitalMapper patientVitalMapper;

    public PatientVitalServiceImpl(PatientVitalRepository patientVitalRepository, PatientVitalMapper patientVitalMapper) {
        this.patientVitalRepository = patientVitalRepository;
        this.patientVitalMapper = patientVitalMapper;
    }

    @Override
    public PatientVitalDTO save(PatientVitalDTO patientVitalDTO) {
        log.debug("Request to save PatientVital : {}", patientVitalDTO);
        PatientVital patientVital = patientVitalMapper.toEntity(patientVitalDTO);
        patientVital = patientVitalRepository.save(patientVital);
        return patientVitalMapper.toDto(patientVital);
    }

    @Override
    public PatientVitalDTO update(PatientVitalDTO patientVitalDTO) {
        log.debug("Request to update PatientVital : {}", patientVitalDTO);
        PatientVital patientVital = patientVitalMapper.toEntity(patientVitalDTO);
        patientVital = patientVitalRepository.save(patientVital);
        return patientVitalMapper.toDto(patientVital);
    }

    @Override
    public Optional<PatientVitalDTO> partialUpdate(PatientVitalDTO patientVitalDTO) {
        log.debug("Request to partially update PatientVital : {}", patientVitalDTO);

        return patientVitalRepository
            .findById(patientVitalDTO.getId())
            .map(existingPatientVital -> {
                patientVitalMapper.partialUpdate(existingPatientVital, patientVitalDTO);

                return existingPatientVital;
            })
            .map(patientVitalRepository::save)
            .map(patientVitalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientVitalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PatientVitals");
        return patientVitalRepository.findAll(pageable).map(patientVitalMapper::toDto);
    }

    public Page<PatientVitalDTO> findAllWithEagerRelationships(Pageable pageable) {
        return patientVitalRepository.findAllWithEagerRelationships(pageable).map(patientVitalMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PatientVitalDTO> findOne(Long id) {
        log.debug("Request to get PatientVital : {}", id);
        return patientVitalRepository.findOneWithEagerRelationships(id).map(patientVitalMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PatientVital : {}", id);
        patientVitalRepository.deleteById(id);
    }
}
