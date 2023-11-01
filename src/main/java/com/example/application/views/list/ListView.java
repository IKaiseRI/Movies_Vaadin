package com.example.application.views.list;

import com.example.application.entity.dto.MovieDto;
import com.example.application.entity.movie.characteristics.Status;
import com.example.application.service.GenreService;
import com.example.application.service.MovieService;
import com.example.application.utils.MovieFormEvents;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@PageTitle("Movie List | Vaadin")
@Route(value = "", layout = MainLayout.class)
public class ListView extends VerticalLayout {
    private final Grid<MovieDto> movieGrid = new Grid<>(MovieDto.class);
    private final TextField filterText = new TextField();
    private final MovieService movieService;
    private final GenreService genreService;
    private MovieForm movieForm;

    public ListView(MovieService movieService, GenreService genreService) {
        this.movieService = movieService;
        this.genreService = genreService;

        addClassName("Movie-view");
        setSizeFull();

        configureGridStyle();
        configureFormStyle();

        add(
                getToolBar(),
                getContent()
        );

        updateListOfMovies();
        closeMovieEditor();
    }

    private void updateListOfMovies() {
        movieGrid.setItems(movieService.findAll(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(movieGrid, movieForm);
        content.addClassName("content");
        content.setFlexGrow(1, movieGrid);
        content.setFlexGrow(1, movieForm);
        content.setSizeFull();

        return content;
    }

    private void configureFormStyle() {
        movieForm = new MovieForm(List.of(Status.values()), genreService.findAll());
        movieForm.setWidth("25em");
        movieForm.addListener(MovieFormEvents.SaveEvent.class, this::saveEvent);
        movieForm.addListener(MovieFormEvents.DeleteEvent.class, this::deleteEvent);
        movieForm.addListener(MovieFormEvents.CloseEvent.class, event -> closeMovieEditor());
    }

    private void deleteEvent(MovieFormEvents.DeleteEvent event) {
        movieService.deleteMovie(event.getMovie());
        updateListOfMovies();
        closeMovieEditor();
    }

    private void saveEvent(MovieFormEvents.SaveEvent event) {
        if (event.getMovie().getId() == 0) {
            movieService.createMovie(event.getMovie());
        } else {
            movieService.updateMovie(event.getMovie());
        }
        updateListOfMovies();
        closeMovieEditor();
    }

    private Component getToolBar() {
        filterText.setPlaceholder("Filter by title");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateListOfMovies());

        Button addMovieButton = new Button("Add movie");
        addMovieButton.addClickListener(element -> addNewMovie());

        HorizontalLayout toolBar = new HorizontalLayout(filterText, addMovieButton);
        toolBar.addClassName("toolbar");

        return toolBar;
    }

    private void addNewMovie() {
        movieGrid.asSingleSelect().clear();
        editMovie(new MovieDto());
    }

    private void configureGridStyle() {
        movieGrid.setClassName("Movie-grid");
        movieGrid.setSizeFull();
        movieGrid.setColumns("title", "originalLanguage", "releaseDate", "runtime", "status", "genres");
        movieGrid.getColumns().forEach(column -> column.setAutoWidth(true));
        movieGrid.asSingleSelect().addValueChangeListener(object -> editMovie(object.getValue()));
    }

    private void editMovie(MovieDto movieDto) {
        if (movieDto == null) {
            closeMovieEditor();
        } else {
            movieForm.setMovie(movieDto);
            movieForm.setVisible(true);
            addClassName("editing");
        }
    }

    public void closeMovieEditor() {
        movieForm.setMovie(null);
        movieForm.setVisible(false);
        removeClassName("editing");
    }
}
