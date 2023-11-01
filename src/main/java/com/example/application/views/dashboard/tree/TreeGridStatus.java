package com.example.application.views.dashboard.tree;

import com.example.application.entity.dto.MovieDto;
import com.example.application.entity.movie.characteristics.Status;
import com.example.application.entity.record.StatusRecord;
import com.example.application.service.MovieService;
import com.example.application.utils.RecordUtils;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.provider.hierarchy.TreeData;

import java.util.List;

public class TreeGridStatus extends VerticalLayout {
    private final MovieService movieService;

    public TreeGridStatus(MovieService movieService) {
        this.movieService = movieService;

        add(configureTree());
    }

    private TreeGrid<StatusRecord> configureTree() {
        TreeGrid<StatusRecord> treeGrid = new TreeGrid<>();

        treeGrid.setTreeData(createTree());
        treeGrid.addHierarchyColumn(this::getStatusValue)
                .setHeader("Status")
                .setFlexGrow(2);
        treeGrid.addColumn(this::getMovieTitle)
                .setHeader("Movie Title")
                .setFlexGrow(5);

        return treeGrid;
    }

    private String getStatusValue(StatusRecord statusRecord) {
        return statusRecord.getStatus() != null ? Status.getStringValue(statusRecord.getStatus()) : "";
    }

    private String getMovieTitle(StatusRecord statusRecord) {
        return statusRecord.getMovieDto() != null ? statusRecord.getMovieDto().getTitle() : "";
    }

    public TreeData<StatusRecord> createTree() {
        TreeData<StatusRecord> tree = new TreeData<>();
        List<Status> statusList = List.of(Status.values());
        List<MovieDto> movieList = movieService.findAll(null);

        for (Status status : statusList) {
            StatusRecord parent = new StatusRecord(status, null);
            tree.addItem(null, parent);

            List<StatusRecord> movieStatusRecords = movieList.stream()
                    .filter(movie -> movie.getStatus().equals(Status.getStringValue(parent.getStatus())))
                    .map(movie -> new StatusRecord(null, movie))
                    .toList();

            RecordUtils.populateChildren(tree, movieStatusRecords, parent);
        }

        return tree;
    }
}
