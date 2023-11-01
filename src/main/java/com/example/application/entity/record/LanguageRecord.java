package com.example.application.entity.record;

import com.example.application.entity.dto.MovieDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LanguageRecord {
    private String language;
    private MovieDto movieDto;
}
