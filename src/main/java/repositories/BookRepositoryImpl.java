package repositories;

import db.core.Template;
import models.Author;
import models.Book;
import models.LibraryCard;
import utils.By;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;

import static utils.Constants.*;

public class BookRepositoryImpl implements BookRepository {

    private static final String INSERT = "INSERT INTO " + BOOKS +
            " (" + TITLE + ", " + PUB_YEAR + ", " + AUTHOR + ") VALUES(?, ?, ?)";

    private static final String UPDATE = "UPDATE " + BOOKS + " SET " + LIBRARY_CARD +
            "= ? " + "WHERE " + ID + "= ?";

    private static final String DISTINCT = "SELECT DISTINCT " + TITLE +
            ", " + FIRST_NAME + ", " + LAST_NAME + " FROM " + VIEW + " WHERE ";
    private static final String BY_TITLE = DISTINCT + TITLE + " LIKE ?";
    private static final String BY_AUTHOR = DISTINCT + LAST_NAME + " LIKE ?";
    private static final String BY_GENRE = DISTINCT + NAME + " LIKE ?";

    private static final String QUERY = "SELECT * FROM " + BOOKS + " WHERE ";
    private static final String BY_NAME = QUERY + TITLE + "= ?";
    private static final String BY_USER = QUERY + LIBRARY_CARD +
            "= ? " + " AND " + TITLE + "= ?";

    private Template<Book> template = new Template<>();

    @Override
    public List<Book> findDistinctBy(String name, By criterion) throws SQLException {
        String sql;
        switch (criterion) {
            case TITLE:
                sql = BY_TITLE;
                break;
            case AUTHOR:
                sql = BY_AUTHOR;
                break;
            case GENRE:
                sql = BY_GENRE;
                break;
            default:
                sql = "";
        }
        return template.query(sql, new Object[]{ "%" + name.toLowerCase() + "%" }, results -> {
            Book book = new Book();
            book.setTitle(results.getString("title"));
            Author author = new Author();
            author.setFirstName(results.getString("first_name"));
            author.setLastName(results.getString("last_name"));
            book.setAuthor(author);
            return book;
        });
    }

    @Override
    public List<Book> findByName(Book book) throws SQLException {
        return template.query(BY_NAME, new Object[] { book.getTitle().toLowerCase() },
            results -> map(results));
    }

    @Override
    public List<Book> findByUser(Book book) throws SQLException {
        return template.query(BY_USER, new Object[] { book.getLibraryCard().getId(),
        book.getTitle().toLowerCase() }, results -> map(results));
    }

    @Override
    public int insert(Book book) throws SQLException {
        return template.updateAndReturnKey(INSERT, new Object[]{
                book.getTitle().toLowerCase(), book.getPublicationYear(),
                book.getAuthor().getId()
        });
    }

    @Override
    public int update(Book book) throws SQLException {
        return template.update(UPDATE, new Object[]{
                book.getLibraryCard().getId(),
                book.getId()
        });
    }

    private Book map(ResultSet results) throws SQLException {
        Book b = new Book();
        b.setId(results.getInt("_id"));
        b.setTitle(results.getString("title"));
        b.setPublicationYear(results.getInt("publication_year"));
        LibraryCard lc = new LibraryCard();
        lc.setId(results.getInt("library_card"));
        b.setLibraryCard(lc);
        Author author = new Author();
        author.setId(results.getInt("author"));
        b.setAuthor(author);
        return b;
    }

    @Override
    public List<Book> findAll() throws SQLException {
        return null;
    }

    @Override
    public Book findById(int id) throws SQLException {
        return null;
    }

    @Override
    public int delete(int id) throws SQLException {
        return 0;
    }
}
