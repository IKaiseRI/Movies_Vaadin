package com.example.application.views.dashboard.tree;

import com.example.application.entity.dto.MovieDto;
import com.example.application.entity.record.DateRecord;
import com.example.application.service.MovieService;
import com.example.application.utils.RecordUtils;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;

import java.time.Month;
import java.time.Year;
import java.util.List;

public class TreeGridDate extends VerticalLayout {
    private final MovieService movieService;

    public TreeGridDate(MovieService movieService) {
        this.movieService = movieService;

        add(configureTree());
    }

    private TreeGrid<DateRecord> configureTree() {
        TreeGrid<DateRecord> treeGrid = new TreeGrid<>();

        treeGrid.setTreeData(createTree());
        treeGrid.addHierarchyColumn(this::getYear)
                .setHeader("Year")
                .setWidth("100px")
                .setFlexGrow(0);
        treeGrid.addColumn(this::getMonth)
                .setHeader("Month")
                .setWidth("100px")
                .setFlexGrow(0);
        treeGrid.addColumn(this::getMovie)
                .setHeader("Movie Title")
                .setWidth("100px")
                .setFlexGrow(3);


        return treeGrid;
    }

    private String getYear(DateRecord dateRecord) {
        return dateRecord.getYear() != null ? dateRecord.getYear().toString() : "";
    }

    private String getMonth(DateRecord dateRecord) {
        return dateRecord.getMonth() != null ? dateRecord.getMonth().name() : "";
    }

    private String getMovie(DateRecord dateRecord) {
        return dateRecord.getMovieDto() != null ? dateRecord.getMovieDto().getTitle() : "";
    }

    private TreeData<DateRecord> createTree() {
        TreeData<DateRecord> tree = new TreeData<>();
        List<MovieDto> movieDtoList = movieService.findAll(null);
        List<Year> yearList = movieService.findAll(null).stream()
                .map(movieDto -> Year.of(movieDto.getReleaseDate().getYear()))
                .distinct()
                .toList();

        for (Year year : yearList) {
            DateRecord firstParent = new DateRecord(year, null, null);
            tree.addItem(null, firstParent);

            List<DateRecord> monthChildList = movieDtoList.stream()
                    .filter(movieDto -> Year.of(movieDto.getReleaseDate().getYear()).equals(firstParent.getYear()))
                    .map(movieDto -> new DateRecord(null, movieDto.getReleaseDate().getMonth(), null))
                    .toList();

            for (DateRecord dateRecord : monthChildList) {
                Month month = dateRecord.getMonth();
                DateRecord secondParent = new DateRecord(null, month, null);
                tree.addItem(null, secondParent);
                tree.setParent(secondParent, firstParent);

                List<DateRecord> movieChildList = movieDtoList.stream()
                        .filter(movieDto -> movieDto.getReleaseDate().getMonth().equals(secondParent.getMonth()))
                        .map(movieDto -> new DateRecord(null, null, movieDto))
                        .toList();

                RecordUtils.populateChildren(tree, movieChildList, secondParent);
            }
        }

        return tree;
    }
}
