package io.aurigraph.healthcare.service.impl;

import io.aurigraph.healthcare.domain.PatientComorbidity;
import io.aurigraph.healthcare.repository.PatientComorbidityRepository;
import io.aurigraph.healthcare.service.PatientComorbidityService;
import io.aurigraph.healthcare.service.dto.PatientComorbidityDTO;
import io.aurigraph.healthcare.service.mapper.PatientComorbidityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PatientComorbidity}.
 */
@Service
@Transactional
public class PatientComorbidityServiceImpl implements PatientComorbidityService {

    private final Logger log = LoggerFactory.getLogger(PatientComorbidityServiceImpl.class);

    private final PatientComorbidityRepository patientComorbidityRepository;

    private final PatientComorbidityMapper patientComorbidityMapper;

    public PatientComorbidityServiceImpl(
        PatientComorbidityRepository patientComorbidityRepository,
        PatientComorbidityMapper patientComorbidityMapper
    ) {
        this.patientComorbidityRepository = patientComorbidityRepository;
        this.patientComorbidityMapper = patientComorbidityMapper;
    }

    @Override
    public PatientComorbidityDTO save(PatientComorbidityDTO patientComorbidityDTO) {
        log.debug("Request to save PatientComorbidity : {}", patientComorbidityDTO);
        PatientComorbidity patientComorbidity = patientComorbidityMapper.toEntity(patientComorbidityDTO);
        patientComorbidity = patientComorbidityRepository.save(patientComorbidity);
        return patientComorbidityMapper.toDto(patientComorbidity);
    }

    @Override
    public PatientComorbidityDTO update(PatientComorbidityDTO patientComorbidityDTO) {
        log.debug("Request to update PatientComorbidity : {}", patientComorbidityDTO);
        PatientComorbidity patientComorbidity = patientComorbidityMapper.toEntity(patientComorbidityDTO);
        patientComorbidity = patientComorbidityRepository.save(patientComorbidity);
        return patientComorbidityMapper.toDto(patientComorbidity);
    }

    @Override
    public Optional<PatientComorbidityDTO> partialUpdate(PatientComorbidityDTO patientComorbidityDTO) {
        log.debug("Request to partially update PatientComorbidity : {}", patientComorbidityDTO);

        return patientComorbidityRepository
            .findById(patientComorbidityDTO.getId())
            .map(existingPatientComorbidity -> {
                patientComorbidityMapper.partialUpdate(existingPatientComorbidity, patientComorbidityDTO);

                return existingPatientComorbidity;
            })
            .map(patientComorbidityRepository::save)
            .map(patientComorbidityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientComorbidityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PatientComorbidities");
        return patientComorbidityRepository.findAll(pageable).map(patientComorbidityMapper::toDto);
    }

    public Page<PatientComorbidityDTO> findAllWithEagerRelationships(Pageable pageable) {
        return patientComorbidityRepository.findAllWithEagerRelationships(pageable).map(patientComorbidityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PatientComorbidityDTO> findOne(Long id) {
        log.debug("Request to get PatientComorbidity : {}", id);
        return patientComorbidityRepository.findOneWithEagerRelationships(id).map(patientComorbidityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PatientComorbidity : {}", id);
        patientComorbidityRepository.deleteById(id);
    }
}
