package com.example.application.service;

import com.example.application.entity.dto.GenreDto;
import com.example.application.mapper.GenreMapper;
import com.example.application.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(GenreMapper::entityToDto)
                .toList();
    }
}
