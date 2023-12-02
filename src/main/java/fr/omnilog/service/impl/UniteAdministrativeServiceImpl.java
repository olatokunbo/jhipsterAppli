package fr.omnilog.service.impl;

import fr.omnilog.domain.UniteAdministrative;
import fr.omnilog.repository.UniteAdministrativeRepository;
import fr.omnilog.repository.search.UniteAdministrativeSearchRepository;
import fr.omnilog.service.UniteAdministrativeService;
import fr.omnilog.service.dto.UniteAdministrativeDTO;
import fr.omnilog.service.mapper.UniteAdministrativeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.omnilog.domain.UniteAdministrative}.
 */
@Service
@Transactional
public class UniteAdministrativeServiceImpl implements UniteAdministrativeService {

    private final Logger log = LoggerFactory.getLogger(UniteAdministrativeServiceImpl.class);

    private final UniteAdministrativeRepository uniteAdministrativeRepository;

    private final UniteAdministrativeMapper uniteAdministrativeMapper;

    private final UniteAdministrativeSearchRepository uniteAdministrativeSearchRepository;

    public UniteAdministrativeServiceImpl(
        UniteAdministrativeRepository uniteAdministrativeRepository,
        UniteAdministrativeMapper uniteAdministrativeMapper,
        UniteAdministrativeSearchRepository uniteAdministrativeSearchRepository
    ) {
        this.uniteAdministrativeRepository = uniteAdministrativeRepository;
        this.uniteAdministrativeMapper = uniteAdministrativeMapper;
        this.uniteAdministrativeSearchRepository = uniteAdministrativeSearchRepository;
    }

    @Override
    public UniteAdministrativeDTO save(UniteAdministrativeDTO uniteAdministrativeDTO) {
        log.debug("Request to save UniteAdministrative : {}", uniteAdministrativeDTO);
        UniteAdministrative uniteAdministrative = uniteAdministrativeMapper.toEntity(uniteAdministrativeDTO);
        uniteAdministrative = uniteAdministrativeRepository.save(uniteAdministrative);
        UniteAdministrativeDTO result = uniteAdministrativeMapper.toDto(uniteAdministrative);
        uniteAdministrativeSearchRepository.index(uniteAdministrative);
        return result;
    }

    @Override
    public UniteAdministrativeDTO update(UniteAdministrativeDTO uniteAdministrativeDTO) {
        log.debug("Request to update UniteAdministrative : {}", uniteAdministrativeDTO);
        UniteAdministrative uniteAdministrative = uniteAdministrativeMapper.toEntity(uniteAdministrativeDTO);
        uniteAdministrative = uniteAdministrativeRepository.save(uniteAdministrative);
        UniteAdministrativeDTO result = uniteAdministrativeMapper.toDto(uniteAdministrative);
        uniteAdministrativeSearchRepository.index(uniteAdministrative);
        return result;
    }

    @Override
    public Optional<UniteAdministrativeDTO> partialUpdate(UniteAdministrativeDTO uniteAdministrativeDTO) {
        log.debug("Request to partially update UniteAdministrative : {}", uniteAdministrativeDTO);

        return uniteAdministrativeRepository
            .findById(uniteAdministrativeDTO.getId())
            .map(existingUniteAdministrative -> {
                uniteAdministrativeMapper.partialUpdate(existingUniteAdministrative, uniteAdministrativeDTO);

                return existingUniteAdministrative;
            })
            .map(uniteAdministrativeRepository::save)
            .map(savedUniteAdministrative -> {
                uniteAdministrativeSearchRepository.index(savedUniteAdministrative);
                return savedUniteAdministrative;
            })
            .map(uniteAdministrativeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UniteAdministrativeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all UniteAdministratives");
        return uniteAdministrativeRepository.findAll(pageable).map(uniteAdministrativeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UniteAdministrativeDTO> findOne(Long id) {
        log.debug("Request to get UniteAdministrative : {}", id);
        return uniteAdministrativeRepository.findById(id).map(uniteAdministrativeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UniteAdministrative : {}", id);
        uniteAdministrativeRepository.deleteById(id);
        uniteAdministrativeSearchRepository.deleteFromIndexById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UniteAdministrativeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of UniteAdministratives for query {}", query);
        return uniteAdministrativeSearchRepository.search(query, pageable).map(uniteAdministrativeMapper::toDto);
    }
}
