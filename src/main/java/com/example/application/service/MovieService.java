package com.example.application.service;

import com.example.application.entity.dto.MovieDto;
import com.example.application.entity.movie.Movie;
import com.example.application.mapper.MovieMapper;
import com.example.application.repository.GenreRepository;
import com.example.application.repository.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository, GenreRepository genreRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
    }

    public void createMovie(MovieDto movieDto) {
        movieRepository.save(MovieMapper.dtoToEntity(movieDto, genreRepository));
    }

    public List<MovieDto> findAll(String filterText) {
        List<Movie> movies =
                (filterText == null || filterText.isEmpty())
                        ? movieRepository.findAll()
                        : movieRepository.search(filterText);

        return movies.stream()
                .map(MovieMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public Set<String> getSetOfLanguages() {
        return movieRepository.findAll().stream()
                .map(Movie::getOriginalLanguage)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void deleteMovie(MovieDto movieDto) {
        Optional<Movie> movie = movieRepository.findById(movieDto.getId());
        if (movie.isPresent()) {
            Movie tempMovie = movie.get();
            tempMovie.getGenres().clear();
            movieRepository.save(tempMovie);
            movieRepository.delete(tempMovie);
        }
    }

    @Transactional
    public void updateMovie(MovieDto movieDto) {
        Movie oldMovie = movieRepository.findById(movieDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("Movie object is empty or null"));

        Movie newMovie = MovieMapper.dtoToEntity(movieDto, genreRepository);
        newMovie.setId(oldMovie.getId());

        movieRepository.save(newMovie);
    }
}
