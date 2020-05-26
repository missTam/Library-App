package utils;

import models.Author;
import models.Book;
import models.Genre;
import models.LibraryCard;
import org.apache.poi.xssf.usermodel.XSSFRow;
import reader.Reader;
import repositories.*;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;


public class DataInsertion {

    public @Inject GenreRepository genreRepository;
    public @Inject AuthorRepository authorRepository;
    public @Inject LibraryCardRepository libraryCardRepository;
    public @Inject BookRepository bookRepository;
    public @Inject Reader reader;

    public DataInsertion(GenreRepository genreRepository, AuthorRepository authorRepository,
                         LibraryCardRepository libraryCardRepository, BookRepository bookRepository,
                         Reader reader){
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.libraryCardRepository = libraryCardRepository;
        this.bookRepository = bookRepository;
        this.reader = reader;
    }

    // read genres from excel into db
    public void readGenresIntoDb() {
        try {
            // read data (rows) from xlsx file
            List<XSSFRow> data = reader.readData("genres");
            // map data to objects
            List<Genre> genres = XLSMapper.mapGenres(data);
            // fill db table with data
            for(Genre genre : genres){
                genreRepository.insert(genre);
            }
        } catch (SQLException e) {
            System.out.println("Couldn't read genres into db" + e.getMessage());
            e.printStackTrace();
        }
    }

    // read authors from excel into db
    public void readAuthorsIntoDb() {

        try {
            List<XSSFRow> data = reader.readData("authors");
            List<Author> authors = XLSMapper.mapAuthors(data);
            for(Author author : authors){
                authorRepository.insert(author);
            }
        } catch (SQLException e) {
            System.out.println("Couldn't read authors into db" + e.getMessage());
            e.printStackTrace();
        }
    }

    // read library cards from excel into db
    public void readLibraryCardsIntoDb() {
        try {
            List<XSSFRow> data = reader.readData("library_cards");
            List<LibraryCard> libraryCards = XLSMapper.mapLibraryCards(data);
            for(LibraryCard libraryCard : libraryCards){
                libraryCardRepository.insert(libraryCard);
            }
        } catch (SQLException e) {
            System.out.println("Couldn't insert library cards into db" + e.getMessage());
            e.printStackTrace();
        }
    }

    // read books from excel into db
    public void readBooksIntoDb() {
        try {
            List<XSSFRow> data = reader.readData("books");
            List<Book> books = XLSMapper.mapBooks(data);
            for(Book book : books){
                if(book.getLibraryCard().getId() == 0) {
                    bookRepository.insert(book);
                } else {
                    int key = bookRepository.insert(book);
                    book.setId(key);
                    bookRepository.update(book);
                }
            }
        } catch (SQLException e) {
            System.out.println("Couldn't insert books into db" + e.getMessage());
            e.printStackTrace();
        }
    }
}
