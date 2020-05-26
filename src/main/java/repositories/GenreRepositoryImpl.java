package repositories;

import db.core.Template;
import models.Book;
import models.Genre;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static utils.Constants.*;

public class GenreRepositoryImpl implements GenreRepository {

    private static final String SELECT = "SELECT * FROM " + GENRES;
    private static final String INSERT = "INSERT INTO " + GENRES + " (" + NAME + ") VALUES(?)";
    private static final String SELECT_WHERE = SELECT + " WHERE " + NAME + "= ?";
    private static final String REL = "INSERT INTO " + RELATION + " (" + BOOK + ", " + GENRE +
            ") VALUES(?, ?)";

    private Template<Genre> template = new Template<>();

    @Override
    public List<Genre> findAll() throws SQLException {
        return template.query(SELECT, null, results -> map(results));
    }

    @Override
    public List<Genre> findByName(Genre genre) throws SQLException {
        return template.query(SELECT_WHERE, new Object[] { genre.getName().toLowerCase() },
                results -> map(results));
    }

    @Override
    public int postRelation(Book book, Genre genre) throws SQLException {
        return template.update(REL, new Object[] { book.getId(), genre.getId()});
    }

    @Override
    public int insert(Genre genre) throws SQLException {
        return template.updateAndReturnKey(INSERT, new Object[] { genre.getName().toLowerCase() });
    }

    private Genre map(ResultSet results) throws SQLException {
        Genre genre = new Genre();
        genre.setId(results.getInt("_id"));
        genre.setName(results.getString("name"));
        return genre;
    }

    @Override
    public Genre findById(int id) throws SQLException {
        return null;
    }

    @Override
    public int update(Genre object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(int id) throws SQLException {
        return 0;
    }
}
