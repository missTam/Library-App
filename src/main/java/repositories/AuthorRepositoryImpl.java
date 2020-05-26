package repositories;

import db.core.Template;
import models.Author;

import java.sql.*;
import java.util.List;

import static utils.Constants.*;
import static utils.Constants.FIRST_NAME;
import static utils.Constants.LAST_NAME;

public class AuthorRepositoryImpl implements AuthorRepository {

    private static final String INSERT = "INSERT INTO " + AUTHORS + " (" +
            FIRST_NAME + ", " + LAST_NAME + ") VALUES(?, ?)";

    private static final String SELECT = "SELECT * FROM " + AUTHORS +
            " WHERE " + FIRST_NAME + "= ?" + " AND " + LAST_NAME + "= ?";

    private Template<Author> template = new Template<>();

    @Override
    public List<Author> findByName(Author author) throws SQLException {
        return template.query(SELECT, new Object[]{author.getFirstName().toLowerCase(),
                author.getLastName().toLowerCase()}, results -> {
            Author auth = new Author();
            auth.setId(results.getInt("_id"));
            auth.setFirstName(results.getString("first_name"));
            auth.setLastName(results.getString("last_name"));
            return auth;
        });
    }

    @Override
    public int insert(Author author) throws SQLException {
        return template.updateAndReturnKey(INSERT, new Object[]{
                author.getFirstName().toLowerCase(), author.getLastName().toLowerCase()
        });
    }

    @Override
    public List<Author> findAll() throws SQLException {
        return null;
    }

    @Override
    public Author findById(int id) throws SQLException {
        return null;
    }

    @Override
    public int update(Author object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(int id) throws SQLException {
        return 0;
    }


}
