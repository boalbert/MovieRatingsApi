package se.joakimliden.springlab.mappers;

import org.springframework.stereotype.Component;
import se.joakimliden.springlab.dtos.LanguageDto;
import se.joakimliden.springlab.entities.Language;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LanguageMapper {

    public LanguageMapper() {
    }

    public LanguageDto mapp(Language language) {
        return new LanguageDto(language.getId(), language.getLanguage());
    }

    public Language mapp(LanguageDto languageDto) {
        return new Language(languageDto.getId(), languageDto.getLanguage());
    }

    public Optional<LanguageDto> mapp(Optional<Language> optionalLanguage) {
        if (optionalLanguage.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapp(optionalLanguage.get()));
    }

    public List<LanguageDto> mapp(List<Language> all) {

        return all
                .stream()
                .map(this::mapp)
                .collect(Collectors.toList());
    }
}