package com.example.application.entity.record;

import com.example.application.entity.dto.MovieDto;
import com.example.application.entity.movie.characteristics.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusRecord {
    private Status status;
    private MovieDto movieDto;
}
