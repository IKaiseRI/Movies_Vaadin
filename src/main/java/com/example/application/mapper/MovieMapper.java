package com.example.application.mapper;

import com.example.application.entity.dto.MovieDto;
import com.example.application.entity.movie.Movie;
import com.example.application.entity.movie.characteristics.Status;
import com.example.application.repository.GenreRepository;
import com.example.application.utils.EntityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class MovieMapper {
    public static Movie dtoToEntity(MovieDto movieDto, GenreRepository genreRepository) {
        return Movie.builder()
                .id(movieDto.getId())
                .title(movieDto.getTitle())
                .originalLanguage(movieDto.getOriginalLanguage())
                .releaseDate(movieDto.getReleaseDate())
                .runtime(movieDto.getRuntime())
                .status(EntityUtils.convertToStatus(movieDto.getStatus()))
                .genres(GenreMapper.stringDtoToEntitySet(movieDto.getGenres(), genreRepository))
                .build();
    }

    public static MovieDto entityToDto(Movie movie) {
        return MovieDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .originalLanguage(movie.getOriginalLanguage())
                .releaseDate(movie.getReleaseDate())
                .runtime(movie.getRuntime())
                .status(Status.getStringValue(movie.getStatus()))
                .genres(GenreMapper.entitySetToStringDto(movie))
                .build();
    }
}
