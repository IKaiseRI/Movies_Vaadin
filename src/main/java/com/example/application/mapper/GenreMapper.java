package com.example.application.mapper;

import com.example.application.entity.dto.GenreDto;
import com.example.application.entity.genre.Genre;
import com.example.application.entity.movie.Movie;
import com.example.application.repository.GenreRepository;
import com.example.application.utils.EntityUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GenreMapper {

    public static Set<Genre> stringDtoToEntitySet(String genresString, GenreRepository genreRepository) {
        List<String> listOfNames = EntityUtils.parseGenresFromString(genresString);

        return listOfNames.stream()
                .map(name -> genreRepository.findByName(name).orElseGet(() -> {
                    Genre newGenre = new Genre().setName(name);
                    genreRepository.save(newGenre);
                    return newGenre;
                }))
                .collect(Collectors.toSet());
    }

    public static String entitySetToStringDto(Movie movie) {
        return movie.getGenres().stream().map(Genre::getName).collect(Collectors.joining(", "));
    }

    public static Genre dtoToEntity(GenreDto genreDto) {
        return new Genre().setName(genreDto.getName());
    }

    public static GenreDto entityToDto(Genre genre) {
        return new GenreDto().setName(genre.getName());
    }

}
