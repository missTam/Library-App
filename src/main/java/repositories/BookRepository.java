package repositories;

import models.Book;
import utils.By;

import java.sql.SQLException;
import java.util.List;

public interface BookRepository extends CRUDRepository<Book> {
    List<Book> findDistinctBy(String name, By criterion) throws SQLException;
    List<Book> findByUser(Book book) throws SQLException;
}
