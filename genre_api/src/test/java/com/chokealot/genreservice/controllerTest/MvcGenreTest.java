package com.chokealot.genreservice.controllerTest;

import com.chokealot.genreservice.controllers.GenreController;
import com.chokealot.genreservice.dto.GenreDto;
import com.chokealot.genreservice.models.Genre;
import com.chokealot.genreservice.service.GenreService;
import com.chokealot.genreservice.service.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.List;
import java.util.Optional;

@WebMvcTest(GenreController.class)
public class MvcGenreTest {

    @MockBean
    GenreService genreService;

    @Autowired
    private MockMvc mockMvc;


//Valid tests
    @Test
    void getAllGenresWithUrlAsJson() throws Exception   {

        Mockito.when(genreService.getAllGenres())
                .thenReturn(List.of(new GenreDto(24,"test")));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/genre")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void getOneGenreWithUrlAsJson() throws Exception    {
        Mockito.when(genreService.getOne(1))
                .thenReturn(Optional.of(new GenreDto(1,"test")));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/genre/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void createNewGenre() throws Exception  {
        GenreDto genreDto = new GenreDto(24,"test");

        Mockito.when(
                genreService.createGenre(any(GenreDto.class)))
                .thenReturn(genreDto);

        mockMvc.perform(MockMvcRequestBuilders
        .post("/genre")
        .content(asJsonString(genreDto))
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(genreDto.getGenre()));
    }

    @Test
    void deleteGenreWithId() throws Exception    {
        mockMvc.perform(MockMvcRequestBuilders.delete("/genre/{id}",1))
                .andExpect(status().isOk());
    }

    @Test
    void replaceGenreWithId() throws Exception  {
        GenreDto genreDtoReplacement = new GenreDto(11,"TestGenre");
        Mockito.when(
                genreService.replace(eq(11),any(GenreDto.class)))
                .thenReturn(genreDtoReplacement);

        mockMvc.perform(
                MockMvcRequestBuilders
                .put("/genre/{id}", 11)
                .content(asJsonString(genreDtoReplacement))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    void updateGenreWithId() throws Exception   {
        GenreDto updatedGenreDto = new GenreDto(11,"TestGenre");

        Mockito.when(
                genreService.update(eq(11), any()))
                .thenReturn(updatedGenreDto);

        mockMvc.perform(
                MockMvcRequestBuilders
                .patch("/genre/{id}",11)
                .content(asJsonString(updatedGenreDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre")
                        .value(updatedGenreDto.getGenre()));

    }

    @Test
    void findGenreByStringInput() throws Exception  {
        Optional<GenreDto> searchResults = Optional.of(new GenreDto(11, "TestGenre"));

        Mockito.when(
                genreService.findGenreByGenre(anyString()))
                .thenReturn(searchResults);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/genre/search?genre=TestGenre")
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(searchResults))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

//Invalid Tests

    @Test
    void getOneGenreWithInvalidTarget() throws Exception    {
        Mockito.when(genreService.getOne(11))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/genre/{id}",11)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void deleteGenreWithInvalidTarget() throws Exception   {
        GenreDto genreDto = new GenreDto(11,"test");

        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
                .when(genreService).delete(11);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/genre/{id}",11))
                .andExpect(status().isNotFound());
    }

    @Test
    void replaceGenreWithInvalidTarget() throws Exception    {
        GenreDto replacementGenreDto = new GenreDto(11,"test");

        Mockito.when(genreService.replace(eq(11),any(GenreDto.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(
                MockMvcRequestBuilders
                .put("/genre/{id}",11)
                .content(asJsonString(replacementGenreDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void updateGenreWithInvalidTarget() throws Exception    {
        GenreDto updatedGenreDto = new GenreDto(11,"test");

        Mockito.when(
                genreService.update(anyInt(),any(GenreDto.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/genre/{id}",11)
                .content(asJsonString(updatedGenreDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void searchForGenreWithStringWithInvalidTarget() throws Exception   {
        Optional<GenreDto> searchResults = Optional.of(new GenreDto(11, "TestGenre"));

        Mockito.when(
                genreService.findGenreByGenre(anyString()))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/genre/{id}",11)
                .content(asJsonString(searchResults))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }



    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
