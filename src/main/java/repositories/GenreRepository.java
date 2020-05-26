package repositories;

import models.Book;
import models.Genre;

import java.sql.SQLException;

public interface GenreRepository extends CRUDRepository<Genre> {
    int postRelation(Book book, Genre genre) throws SQLException;
}
