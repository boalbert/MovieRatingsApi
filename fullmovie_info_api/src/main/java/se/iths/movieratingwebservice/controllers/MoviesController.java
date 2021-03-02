package se.iths.movieratingwebservice.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import se.iths.movieratingwebservice.dtos.*;
import se.iths.movieratingwebservice.service.MovieService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
public class MoviesController {

	final String moviesUrl = "http://localhost:5054/movies/";
	final String directorsUrl = "http://localhost:5050/directors/";
	final String languageUrl = "http://localhost:5052/languages/";
	final String genreUrl = "http://localhost:5053/genre/";

	MovieService movieService;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/fullmovies/{id}")
	public MovieWithInfoDto getFullMovie(@PathVariable long id) {

		// Finds the object which holds references to the other movies
		MovieDto movieDto = getOneMovie(id);

		// Which Ids to find in the other services
		long genreId = movieDto.getGenreId();
		long languageId = movieDto.getLanguageId();
		long directorId = movieDto.getDirectorId();

		// Search for the ids in the other APIs
		GenreDto genreDto = getOneGenre(genreId);
		LanguageDto languageDto = getOneLanguage(languageId);
		DirectorDto directorDto = getOneDirector(directorId);

		// Return to client
		return new MovieWithInfoDto(movieDto, directorDto, genreDto.getGenre(), languageDto.getLanguage());
	}

	@GetMapping("/fullmovies")
	public List<MovieWithInfoDto> getAllFullMovies() {

		MovieDto[] allMovies = getAllMovies();
		List<DirectorDto> allDirectors = Arrays.asList(getAllDirectors());
		List<LanguageDto> allLanguages = Arrays.asList(getAllLanguages());
		List<GenreDto> allGenres = Arrays.asList(getAllGenres());

		List<MovieWithInfoDto> movieWithInfoList = new ArrayList<>();

		for (MovieDto movieDto : allMovies) {
			MovieWithInfoDto newMovie = new MovieWithInfoDto();

			newMovie.setMovieDto(movieDto);
			newMovie.setDirectorDto(allDirectors.get(((int) movieDto.getDirectorId()) - 1 ));
			newMovie.setLanguage(allLanguages.get(((int) movieDto.getLanguageId()) - 1 ).getLanguage());
			newMovie.setGenre(allGenres.get(((int) movieDto.getGenreId()) - 1 ).getGenre());

			movieWithInfoList.add(newMovie);
		}
		return movieWithInfoList;
	}

	public MovieDto[] getAllMovies() {

		return restTemplate.getForObject(moviesUrl, MovieDto[].class);
	}

	public DirectorDto[] getAllDirectors() {

		return restTemplate.getForObject(directorsUrl, DirectorDto[].class);
	}

	public LanguageDto[] getAllLanguages() {

		return restTemplate.getForObject(languageUrl, LanguageDto[].class);
	}

	public GenreDto[] getAllGenres() {

		return restTemplate.getForObject(genreUrl, GenreDto[].class);
	}

	public MovieDto getOneMovie(long id) {

		return restTemplate.getForObject( moviesUrl + id, MovieDto.class);

	}

	public DirectorDto getOneDirector(long id) {

		return restTemplate.getForObject(directorsUrl + id, DirectorDto.class);

	}

	public GenreDto getOneGenre(long id) {

		return restTemplate.getForObject(genreUrl + id, GenreDto.class);

	}

	public LanguageDto getOneLanguage(long id) {

		return restTemplate.getForObject(languageUrl + id, LanguageDto.class);
	}
}




