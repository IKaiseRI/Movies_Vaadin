package com.example.application.views.dashboard.tree;

import com.example.application.entity.dto.MovieDto;
import com.example.application.entity.record.LanguageRecord;
import com.example.application.service.MovieService;
import com.example.application.utils.RecordUtils;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;

import java.util.List;
import java.util.Set;

public class TreeGridLanguage extends VerticalLayout {
    private final MovieService movieService;

    public TreeGridLanguage(MovieService movieService) {
        this.movieService = movieService;

        add(configureTree());
    }

    private TreeGrid<LanguageRecord> configureTree() {
        TreeGrid<LanguageRecord> treeGrid = new TreeGrid<>();

        treeGrid.setTreeData(createTree());
        treeGrid.addHierarchyColumn(this::getLanguage)
                .setHeader("Language")
                .setFlexGrow(2);
        treeGrid.addColumn(this::getMovieTitle)
                .setHeader("Movie Title")
                .setFlexGrow(5);

        return treeGrid;
    }

    private String getLanguage(LanguageRecord languageRecord) {
        return languageRecord.getLanguage() != null ? languageRecord.getLanguage() : "";
    }

    private String getMovieTitle(LanguageRecord languageRecord) {
        return languageRecord.getMovieDto() != null ? languageRecord.getMovieDto().getTitle() : "";
    }

    private TreeData<LanguageRecord> createTree() {
        TreeData<LanguageRecord> tree = new TreeData<>();
        Set<String> languages = movieService.getSetOfLanguages();
        List<MovieDto> movies = movieService.findAll(null);

        for (String language : languages) {
            LanguageRecord parent = new LanguageRecord(language, null);
            tree.addItem(null, parent);

            List<LanguageRecord> movieLanguageRecords = movies.stream()
                    .filter(movie -> movie.getOriginalLanguage().equals(parent.getLanguage()))
                    .map(movie -> new LanguageRecord(null, movie))
                    .toList();

            RecordUtils.populateChildren(tree, movieLanguageRecords, parent);
        }

        return tree;
    }
}
