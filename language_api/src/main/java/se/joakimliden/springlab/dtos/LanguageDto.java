package se.joakimliden.springlab.dtos;

public class LanguageDto {

        private long id;
        private String language;

        public LanguageDto(long id, String language) {
            this.id = id;
            this.language = language;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }
    }
