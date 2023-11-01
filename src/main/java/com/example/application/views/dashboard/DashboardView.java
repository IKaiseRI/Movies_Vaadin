package com.example.application.views.dashboard;

import com.example.application.service.GenreService;
import com.example.application.service.MovieService;
import com.example.application.views.MainLayout;
import com.example.application.views.dashboard.tree.TreeGridDate;
import com.example.application.views.dashboard.tree.TreeGridGenres;
import com.example.application.views.dashboard.tree.TreeGridLanguage;
import com.example.application.views.dashboard.tree.TreeGridStatus;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Dashboard | Movie List")
@Route(value = "dashboard", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {
    private final MovieService movieService;
    private final GenreService genreService;
    private TreeGridGenres treeGridGenres;
    private TreeGridStatus treeGridStatus;
    private TreeGridLanguage treeGridLanguage;
    private TreeGridDate treeGridDate;
    public DashboardView(MovieService movieService, GenreService genreService) {
        this.movieService = movieService;
        this.genreService = genreService;

        initiateTrees();

        add(configureTabs());
    }

    private void initiateTrees() {
        treeGridGenres = new TreeGridGenres(movieService, genreService);
        treeGridStatus = new TreeGridStatus(movieService);
        treeGridLanguage = new TreeGridLanguage(movieService);
        treeGridDate = new TreeGridDate(movieService);
    }

    private TabSheet configureTabs() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.add("Genre board", new Div(treeGridGenres));
        tabSheet.add("Status board", new Div(treeGridStatus));
        tabSheet.add("Language board", new Div(treeGridLanguage));
        tabSheet.add("Date board", new Div(treeGridDate));
        tabSheet.setWidth("600px");

        return tabSheet;
    }
}
