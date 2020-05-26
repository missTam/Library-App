package controllers;

import models.Genre;
import repositories.GenreRepository;
import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class GenreController {

    public @Inject
    GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> findAllGenres() {
        try {
            List<Genre> genres = genreRepository.findAll();
            if (genres == null || genres.size() == 0) {
                throw new SQLException();
            }
            return genres;
        } catch (SQLException e) {
            System.out.println("Couldn't find genres" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
