package com.example.application.views.dashboard.tree;

import com.example.application.entity.dto.GenreDto;
import com.example.application.entity.dto.MovieDto;
import com.example.application.entity.record.GenreRecord;
import com.example.application.service.GenreService;
import com.example.application.service.MovieService;
import com.example.application.utils.RecordUtils;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;

import java.util.List;

public class TreeGridGenres extends VerticalLayout {
    private final MovieService movieService;
    private final GenreService genreService;

    public TreeGridGenres(MovieService movieService, GenreService genreService) {
        this.movieService = movieService;
        this.genreService = genreService;

        add(configureTree());
    }

    private TreeGrid<GenreRecord> configureTree() {
        TreeGrid<GenreRecord> treeGrid = new TreeGrid<>();

        treeGrid.setTreeData(createTree());
        treeGrid.addHierarchyColumn(this::getGenreName)
                .setHeader("Genre")
                .setFlexGrow(2);
        treeGrid.addColumn(this::getMovieTitle)
                .setHeader("Movie Title")
                .setFlexGrow(5);

        return treeGrid;
    }

    private String getGenreName(GenreRecord genreRecord) {
        return genreRecord.getGenreDto() != null ? genreRecord.getGenreDto().getName() : "";
    }

    private String getMovieTitle(GenreRecord genreRecord) {
        return genreRecord.getMovieDto() != null ? genreRecord.getMovieDto().getTitle() : "";
    }

    private TreeData<GenreRecord> createTree() {
        TreeData<GenreRecord> tree = new TreeData<>();
        List<GenreDto> genres = genreService.findAll();
        List<MovieDto> movies = movieService.findAll(null);

        for (GenreDto genreDto : genres) {
            GenreRecord parent = new GenreRecord(genreDto, null);
            tree.addItem(null, parent);

            List<GenreRecord> movieGenreRecords = movies.stream()
                    .filter(movie -> movie.getGenres().contains(parent.getGenreDto().getName()))
                    .map(movie -> new GenreRecord(null, movie))
                    .toList();

            RecordUtils.populateChildren(tree, movieGenreRecords, parent);
        }

        return tree;
    }
}
