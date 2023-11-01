package com.example.application.repository;

import com.example.application.entity.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("select c from Movie c " +
            "where lower(c.title) like lower(concat('%', :searchTerm, '%')) ")
    List<Movie> search(@Param("searchTerm") String searchTerm);
}
