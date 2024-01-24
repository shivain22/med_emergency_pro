package io.aurigraph.healthcare.service.impl;

import io.aurigraph.healthcare.domain.Comorbidity;
import io.aurigraph.healthcare.repository.ComorbidityRepository;
import io.aurigraph.healthcare.service.ComorbidityService;
import io.aurigraph.healthcare.service.dto.ComorbidityDTO;
import io.aurigraph.healthcare.service.mapper.ComorbidityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Comorbidity}.
 */
@Service
@Transactional
public class ComorbidityServiceImpl implements ComorbidityService {

    private final Logger log = LoggerFactory.getLogger(ComorbidityServiceImpl.class);

    private final ComorbidityRepository comorbidityRepository;

    private final ComorbidityMapper comorbidityMapper;

    public ComorbidityServiceImpl(ComorbidityRepository comorbidityRepository, ComorbidityMapper comorbidityMapper) {
        this.comorbidityRepository = comorbidityRepository;
        this.comorbidityMapper = comorbidityMapper;
    }

    @Override
    public ComorbidityDTO save(ComorbidityDTO comorbidityDTO) {
        log.debug("Request to save Comorbidity : {}", comorbidityDTO);
        Comorbidity comorbidity = comorbidityMapper.toEntity(comorbidityDTO);
        comorbidity = comorbidityRepository.save(comorbidity);
        return comorbidityMapper.toDto(comorbidity);
    }

    @Override
    public ComorbidityDTO update(ComorbidityDTO comorbidityDTO) {
        log.debug("Request to update Comorbidity : {}", comorbidityDTO);
        Comorbidity comorbidity = comorbidityMapper.toEntity(comorbidityDTO);
        comorbidity = comorbidityRepository.save(comorbidity);
        return comorbidityMapper.toDto(comorbidity);
    }

    @Override
    public Optional<ComorbidityDTO> partialUpdate(ComorbidityDTO comorbidityDTO) {
        log.debug("Request to partially update Comorbidity : {}", comorbidityDTO);

        return comorbidityRepository
            .findById(comorbidityDTO.getId())
            .map(existingComorbidity -> {
                comorbidityMapper.partialUpdate(existingComorbidity, comorbidityDTO);

                return existingComorbidity;
            })
            .map(comorbidityRepository::save)
            .map(comorbidityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComorbidityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Comorbidities");
        return comorbidityRepository.findAll(pageable).map(comorbidityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComorbidityDTO> findOne(Long id) {
        log.debug("Request to get Comorbidity : {}", id);
        return comorbidityRepository.findById(id).map(comorbidityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Comorbidity : {}", id);
        comorbidityRepository.deleteById(id);
    }
}
