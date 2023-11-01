package com.example.application.views.list;

import com.example.application.entity.dto.GenreDto;
import com.example.application.entity.dto.MovieDto;
import com.example.application.entity.movie.characteristics.Status;
import com.example.application.utils.EntityUtils;
import com.example.application.utils.MovieFormEvents;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import lombok.SneakyThrows;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.application.constant.OutputConstants.CANCEL;
import static com.example.application.constant.OutputConstants.DELETE;
import static com.example.application.constant.OutputConstants.GENRES;
import static com.example.application.constant.OutputConstants.LANGUAGE;
import static com.example.application.constant.OutputConstants.MOVIE_GENRES;
import static com.example.application.constant.OutputConstants.RELEASE_DATE;
import static com.example.application.constant.OutputConstants.RUNTIME;
import static com.example.application.constant.OutputConstants.SAVE;
import static com.example.application.constant.OutputConstants.STATUS;
import static com.example.application.constant.OutputConstants.TITLE;

public class MovieForm extends FormLayout {

    private final BeanValidationBinder<MovieDto> binder = new BeanValidationBinder<>(MovieDto.class);
    private final IntegerField id = new IntegerField();
    private final TextField title = new TextField(TITLE);
    private final DatePicker datePicker = new DatePicker(RELEASE_DATE);
    private final TextField language = new TextField(LANGUAGE);
    private final IntegerField runtime = new IntegerField(RUNTIME);
    private final ComboBox<Status> statusComboBox = new ComboBox<>(STATUS);
    private final TextArea selectedGenres = new TextArea(MOVIE_GENRES);
    private final MultiSelectComboBox<GenreDto> genreComboBox = new MultiSelectComboBox<>(GENRES);
    private MovieDto movieDto;

    public MovieForm(List<Status> statuses, List<GenreDto> genres) {
        addClassName("Movie-form");
        setupFields(statuses, genres);
        createButtonsLayout();
    }

    private void setupFields(List<Status> statuses, List<GenreDto> genres) {
        HorizontalLayout genresForm = new HorizontalLayout(selectedGenres, configGenreComboBox(genres));
        genresForm.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);

        statusComboBox.setItems(statuses);
        statusComboBox.setItemLabelGenerator(Status::getStringValue);

        selectedGenres.setReadOnly(true);
        id.setVisible(false);

        binder.bind(title, MovieDto::getTitle, MovieDto::setTitle);
        binder.bind(language, MovieDto::getOriginalLanguage, MovieDto::setOriginalLanguage);
        binder.bind(runtime, MovieDto::getRuntime, MovieDto::setRuntime);
        binder.bind(datePicker, MovieDto::getReleaseDate, MovieDto::setReleaseDate);
        binder.bind(statusComboBox, movieDto -> Status.fromString(movieDto.getStatus()),
                (movieDto, status) -> movieDto.setStatus(Status.getStringValue(status)));
        binder.bind(id, movieDto -> EntityUtils.convertLongToInteger(movieDto.getId()),
                (movieDto, idDto) -> movieDto.setId(EntityUtils.convertIntegerToLong(idDto)));
        binder.bind(selectedGenres, MovieDto::getGenres, MovieDto::setGenres);

        add(
                id,
                title,
                language,
                datePicker,
                runtime,
                statusComboBox,
                genresForm
        );
    }

    private MultiSelectComboBox<GenreDto> configGenreComboBox(List<GenreDto> genres) {
        genreComboBox.setItems(genres);
        genreComboBox.setItemLabelGenerator(GenreDto::getName);
        genreComboBox.addValueChangeListener(
                genreDto -> {
                    String selected = genreDto.getValue().stream().map(GenreDto::getName)
                            .collect(Collectors.joining(", "));
                    selectedGenres.setValue(selected);
                }
        );

        return genreComboBox;
    }

    private void createButtonsLayout() {
        Button save = new Button(SAVE);
        Button delete = new Button(DELETE);
        Button cancel = new Button(CANCEL);

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new MovieFormEvents.DeleteEvent(this, movieDto)));
        cancel.addClickListener(event -> fireEvent(new MovieFormEvents.CloseEvent(this)));

        save.addClickShortcut(Key.END);
        cancel.addClickShortcut(Key.ESCAPE);

        add(new HorizontalLayout(save, delete, cancel));
    }

    @SneakyThrows
    private void validateAndSave() {
        binder.writeBean(movieDto);
        fireEvent(new MovieFormEvents.SaveEvent(this, movieDto));
    }

    public void setMovie(MovieDto movieDto) {
        this.movieDto = movieDto;
        binder.readBean(movieDto);
    }

    protected <EVENT extends ComponentEvent<?>> Registration addListener(
            Class<EVENT> eventType,
            ComponentEventListener<EVENT> listener
    ) {
        return getEventBus().addListener(eventType, listener);
    }
}
