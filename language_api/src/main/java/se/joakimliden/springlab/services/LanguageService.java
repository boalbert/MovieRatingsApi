package se.joakimliden.springlab.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.joakimliden.springlab.dtos.LanguageDto;
import se.joakimliden.springlab.entities.Language;
import se.joakimliden.springlab.mappers.LanguageMapper;
import se.joakimliden.springlab.repositories.LanguageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageService implements se.joakimliden.springlab.services.Service {

    private final LanguageMapper languageMapper;
    private LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository, LanguageMapper languageMapper) {
        this.languageRepository = languageRepository;
        this.languageMapper = languageMapper;
    }

    @Override
    public List<LanguageDto> getAllLanguages() {
        return languageMapper.mapp(languageRepository.findAll());
    }

    @Override
    public Optional<LanguageDto> getOne(Long id) {
        return languageMapper.mapp(languageRepository.findById(id));
    }

    @Override
    public LanguageDto createLanguage(LanguageDto language) {
        if (language.getLanguage().isEmpty()) {
            throw new RuntimeException();
        }
        return languageMapper.mapp(languageRepository.save(languageMapper.mapp(language)));
    }

    @Override
    public void delete(Long id) {
        languageRepository.deleteById(id);
    }

    @Override
    public LanguageDto replace(Long id, LanguageDto languageDto) {
        Optional<Language> language = languageRepository.findById(id);
        if (language.isPresent()) {
            Language updatedLanguage = language.get();
            updatedLanguage.setLanguage(languageDto.getLanguage());
            return languageMapper.mapp(languageRepository.save(updatedLanguage));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Id " + id + " not found.");
        }
    }

    @Override
    public LanguageDto update(Long id, LanguageDto languageDto) {
        Optional<Language> language = languageRepository.findById(id);
        if (language.isPresent()) {
            Language updatedLanguage = language.get();
            if (languageDto.getLanguage() != null)
                updatedLanguage.setLanguage(languageDto.getLanguage());
            return languageMapper.mapp(languageRepository.save(updatedLanguage));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Id " + id + " not found.");
        }
    }

    @Override
    public Optional<LanguageDto> findByLanguage(String language) {
        return languageMapper.mapp(languageRepository.findAllByLanguage(language));
    }
}
