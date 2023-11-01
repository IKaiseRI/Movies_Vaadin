package com.example.application.entity.record;

import com.example.application.entity.dto.MovieDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Month;
import java.time.Year;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DateRecord {
    private Year year;
    private Month month;
    private MovieDto movieDto;
}
