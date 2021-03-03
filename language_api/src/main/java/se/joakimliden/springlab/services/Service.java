package se.joakimliden.springlab.services;

import se.joakimliden.springlab.dtos.LanguageDto;

import java.util.List;
import java.util.Optional;

public interface Service {
    List<LanguageDto> getAllLanguages();

    Optional<LanguageDto> getOne(Long id);

    LanguageDto createLanguage(LanguageDto language);

    void delete(Long id);

    LanguageDto replace(Long id, LanguageDto languageDto);

    LanguageDto update(Long id, LanguageDto languageDto);

    Optional<LanguageDto> findByLanguage(String language);

}
