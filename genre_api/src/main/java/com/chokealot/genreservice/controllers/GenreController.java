package com.chokealot.genreservice.controllers;

import com.chokealot.genreservice.dto.GenreDto;
import com.chokealot.genreservice.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class GenreController {

    private GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService)   {
        this.genreService = genreService;
    }

    @GetMapping("/genre/{id}")
    public GenreDto getOne(@PathVariable int id)    {
        return genreService.getOne(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id "+id+" not found"));
    }

    @GetMapping("/genre")
    public List<GenreDto> getAll()    {
        return genreService.getAllGenres();
    }

    @PostMapping("/genre")
    @ResponseStatus(HttpStatus.CREATED)
    public GenreDto create(@RequestBody GenreDto genreDto){
        return genreService.createGenre(genreDto);
    }

    @DeleteMapping("/genre/{id}")
    public void delete(@PathVariable int id)    {
        genreService.delete(id);
    }

    @PutMapping("/genre/{id}")
    public GenreDto replace(@RequestBody GenreDto genreDto, @PathVariable int id)   {
        return genreService.replace(id, genreDto);
    }

    @RequestMapping(value = "/genre/search", method = RequestMethod.GET)
    @ResponseBody
    public Optional<GenreDto> findByGenre(@RequestParam(value="genre") String searchTerm) {
        return genreService.findGenreByGenre(searchTerm);
    }


    @PatchMapping("/genre/{id}")
    public GenreDto update(@RequestBody GenreDto genreDto, @PathVariable int id)    {
        return genreService.update(id, genreDto);
    }
}
