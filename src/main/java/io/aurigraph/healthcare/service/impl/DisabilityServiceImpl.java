package io.aurigraph.healthcare.service.impl;

import io.aurigraph.healthcare.domain.Disability;
import io.aurigraph.healthcare.repository.DisabilityRepository;
import io.aurigraph.healthcare.service.DisabilityService;
import io.aurigraph.healthcare.service.dto.DisabilityDTO;
import io.aurigraph.healthcare.service.mapper.DisabilityMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Disability}.
 */
@Service
@Transactional
public class DisabilityServiceImpl implements DisabilityService {

    private final Logger log = LoggerFactory.getLogger(DisabilityServiceImpl.class);

    private final DisabilityRepository disabilityRepository;

    private final DisabilityMapper disabilityMapper;

    public DisabilityServiceImpl(DisabilityRepository disabilityRepository, DisabilityMapper disabilityMapper) {
        this.disabilityRepository = disabilityRepository;
        this.disabilityMapper = disabilityMapper;
    }

    @Override
    public DisabilityDTO save(DisabilityDTO disabilityDTO) {
        log.debug("Request to save Disability : {}", disabilityDTO);
        Disability disability = disabilityMapper.toEntity(disabilityDTO);
        disability = disabilityRepository.save(disability);
        return disabilityMapper.toDto(disability);
    }

    @Override
    public DisabilityDTO update(DisabilityDTO disabilityDTO) {
        log.debug("Request to update Disability : {}", disabilityDTO);
        Disability disability = disabilityMapper.toEntity(disabilityDTO);
        disability = disabilityRepository.save(disability);
        return disabilityMapper.toDto(disability);
    }

    @Override
    public Optional<DisabilityDTO> partialUpdate(DisabilityDTO disabilityDTO) {
        log.debug("Request to partially update Disability : {}", disabilityDTO);

        return disabilityRepository
            .findById(disabilityDTO.getId())
            .map(existingDisability -> {
                disabilityMapper.partialUpdate(existingDisability, disabilityDTO);

                return existingDisability;
            })
            .map(disabilityRepository::save)
            .map(disabilityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DisabilityDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Disabilities");
        return disabilityRepository.findAll(pageable).map(disabilityMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DisabilityDTO> findOne(Long id) {
        log.debug("Request to get Disability : {}", id);
        return disabilityRepository.findById(id).map(disabilityMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Disability : {}", id);
        disabilityRepository.deleteById(id);
    }
}
