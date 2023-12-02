package fr.omnilog.service.impl;

import fr.omnilog.domain.Valeur;
import fr.omnilog.repository.ValeurRepository;
import fr.omnilog.repository.search.ValeurSearchRepository;
import fr.omnilog.service.ValeurService;
import fr.omnilog.service.dto.ValeurDTO;
import fr.omnilog.service.mapper.ValeurMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.omnilog.domain.Valeur}.
 */
@Service
@Transactional
public class ValeurServiceImpl implements ValeurService {

    private final Logger log = LoggerFactory.getLogger(ValeurServiceImpl.class);

    private final ValeurRepository valeurRepository;

    private final ValeurMapper valeurMapper;

    private final ValeurSearchRepository valeurSearchRepository;

    public ValeurServiceImpl(ValeurRepository valeurRepository, ValeurMapper valeurMapper, ValeurSearchRepository valeurSearchRepository) {
        this.valeurRepository = valeurRepository;
        this.valeurMapper = valeurMapper;
        this.valeurSearchRepository = valeurSearchRepository;
    }

    @Override
    public ValeurDTO save(ValeurDTO valeurDTO) {
        log.debug("Request to save Valeur : {}", valeurDTO);
        Valeur valeur = valeurMapper.toEntity(valeurDTO);
        valeur = valeurRepository.save(valeur);
        ValeurDTO result = valeurMapper.toDto(valeur);
        valeurSearchRepository.index(valeur);
        return result;
    }

    @Override
    public ValeurDTO update(ValeurDTO valeurDTO) {
        log.debug("Request to update Valeur : {}", valeurDTO);
        Valeur valeur = valeurMapper.toEntity(valeurDTO);
        valeur = valeurRepository.save(valeur);
        ValeurDTO result = valeurMapper.toDto(valeur);
        valeurSearchRepository.index(valeur);
        return result;
    }

    @Override
    public Optional<ValeurDTO> partialUpdate(ValeurDTO valeurDTO) {
        log.debug("Request to partially update Valeur : {}", valeurDTO);

        return valeurRepository
            .findById(valeurDTO.getId())
            .map(existingValeur -> {
                valeurMapper.partialUpdate(existingValeur, valeurDTO);

                return existingValeur;
            })
            .map(valeurRepository::save)
            .map(savedValeur -> {
                valeurSearchRepository.index(savedValeur);
                return savedValeur;
            })
            .map(valeurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ValeurDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Valeurs");
        return valeurRepository.findAll(pageable).map(valeurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ValeurDTO> findOne(Long id) {
        log.debug("Request to get Valeur : {}", id);
        return valeurRepository.findById(id).map(valeurMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Valeur : {}", id);
        valeurRepository.deleteById(id);
        valeurSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ValeurDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Valeurs for query {}", query);
        return valeurSearchRepository.search(query, pageable).map(valeurMapper::toDto);
    }
}
