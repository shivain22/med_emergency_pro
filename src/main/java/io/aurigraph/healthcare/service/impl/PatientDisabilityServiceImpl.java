package io.aurigraph.healthcare.service.impl;

import io.aurigraph.healthcare.domain.PatientDisability;
import io.aurigraph.healthcare.repository.PatientDisabilityRepository;
import io.aurigraph.healthcare.service.PatientDisabilityService;
import io.aurigraph.healthcare.service.dto.PatientDisabilityDTO;
import io.aurigraph.healthcare.service.mapper.PatientDisabilityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PatientDisability}.
 */
@Service
@Transactional
public class PatientDisabilityServiceImpl implements PatientDisabilityService {

    private final Logger log = LoggerFactory.getLogger(PatientDisabilityServiceImpl.class);

    private final PatientDisabilityRepository patientDisabilityRepository;

    private final PatientDisabilityMapper patientDisabilityMapper;

    public PatientDisabilityServiceImpl(
        PatientDisabilityRepository patientDisabilityRepository,
        PatientDisabilityMapper patientDisabilityMapper
    ) {
        this.patientDisabilityRepository = patientDisabilityRepository;
        this.patientDisabilityMapper = patientDisabilityMapper;
    }

    @Override
    public PatientDisabilityDTO save(PatientDisabilityDTO patientDisabilityDTO) {
        log.debug("Request to save PatientDisability : {}", patientDisabilityDTO);
        PatientDisability patientDisability = patientDisabilityMapper.toEntity(patientDisabilityDTO);
        patientDisability = patientDisabilityRepository.save(patientDisability);
        return patientDisabilityMapper.toDto(patientDisability);
    }

    @Override
    public PatientDisabilityDTO update(PatientDisabilityDTO patientDisabilityDTO) {
        log.debug("Request to update PatientDisability : {}", patientDisabilityDTO);
        PatientDisability patientDisability = patientDisabilityMapper.toEntity(patientDisabilityDTO);
        patientDisability = patientDisabilityRepository.save(patientDisability);
        return patientDisabilityMapper.toDto(patientDisability);
    }

    @Override
    public Optional<PatientDisabilityDTO> partialUpdate(PatientDisabilityDTO patientDisabilityDTO) {
        log.debug("Request to partially update PatientDisability : {}", patientDisabilityDTO);

        return patientDisabilityRepository
            .findById(patientDisabilityDTO.getId())
            .map(existingPatientDisability -> {
                patientDisabilityMapper.partialUpdate(existingPatientDisability, patientDisabilityDTO);

                return existingPatientDisability;
            })
            .map(patientDisabilityRepository::save)
            .map(patientDisabilityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientDisabilityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PatientDisabilities");
        return patientDisabilityRepository.findAll(pageable).map(patientDisabilityMapper::toDto);
    }

    public Page<PatientDisabilityDTO> findAllWithEagerRelationships(Pageable pageable) {
        return patientDisabilityRepository.findAllWithEagerRelationships(pageable).map(patientDisabilityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PatientDisabilityDTO> findOne(Long id) {
        log.debug("Request to get PatientDisability : {}", id);
        return patientDisabilityRepository.findOneWithEagerRelationships(id).map(patientDisabilityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PatientDisability : {}", id);
        patientDisabilityRepository.deleteById(id);
    }
}
