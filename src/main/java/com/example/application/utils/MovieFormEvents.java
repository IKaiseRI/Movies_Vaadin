package com.example.application.utils;

import com.example.application.entity.dto.MovieDto;
import com.example.application.views.list.MovieForm;
import com.vaadin.flow.component.ComponentEvent;

public class MovieFormEvents {
    public static abstract class MovieFormEvent extends ComponentEvent<MovieForm> {
        private final MovieDto movieDto;

        protected MovieFormEvent(MovieForm source, MovieDto movieDto) {
            super(source, false);
            this.movieDto = movieDto;
        }

        public MovieDto getMovie() {
            return movieDto;
        }
    }

    public static class SaveEvent extends MovieFormEvent {
        public SaveEvent(MovieForm source, MovieDto movieDto) {
            super(source, movieDto);
        }
    }

    public static class DeleteEvent extends MovieFormEvent {
        public DeleteEvent(MovieForm source, MovieDto movieDto) {
            super(source, movieDto);
        }
    }

    public static class CloseEvent extends MovieFormEvent {
        public CloseEvent(MovieForm source) {
            super(source, null);
        }
    }
}
