package controllers;

import models.*;
import repositories.*;
import utils.By;

import javax.inject.Inject;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class BookController {

    public @Inject
    BookRepository bookRepository;
    public @Inject
    LibraryCardRepository libraryCardRepository;
    public @Inject
    GenreRepository genreRepository;
    public @Inject
    AuthorRepository authorRepository;

    public BookController(BookRepository bookRepository, LibraryCardRepository libraryCardRepository, GenreRepository
            genreRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.libraryCardRepository = libraryCardRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
    }

    public List<Book> findBooks(String name, By criterion) {
        try {
           return bookRepository.findDistinctBy(name, criterion);
        } catch (SQLException e) {
            System.out.println("Couldn't find books" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean borrowBook(String title, LibraryCard libraryCard) {

        try {
            Book book = new Book();
            book.setTitle(title);
            // fetch book
            List<Book> books = bookRepository.findByName(book);
            if (books == null || books.size() == 0) {
                System.out.println("No book found under a given name.");
                return false;
            }
            Book chosenBook = findAvailableBook(books);
            if (chosenBook == null) {
                System.out.println("Book is unfortunately unavailable.");
                return false;
            }

            // fetch library card or create new if not existing, and update book
            List<LibraryCard> userLibraryCard = libraryCardRepository.findByName(libraryCard);
            if (userLibraryCard == null || userLibraryCard.size() == 0) {
                int key = libraryCardRepository.insert(libraryCard);
                if(key == -1) {
                    throw new SQLException("Couldn't insert library card");
                }
                libraryCard.setId(key);
                chosenBook.setLibraryCard(libraryCard);
            } else {
                if(!isStillValid(userLibraryCard.get(0).getValidUntil())) {
                    System.out.println("Your library card has expired. Please renew.");
                    return false;
                }
                chosenBook.setLibraryCard(userLibraryCard.get(0));
            }
            if(bookRepository.update(chosenBook) != 1) {
                throw new SQLException("Couldn't update book");
            }
        } catch (Exception e) {
            System.out.println("Borrow book exception: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    public boolean returnBook(String title, LibraryCard libraryCard) {
        try {

            List<LibraryCard> userLibraryCard = libraryCardRepository.findByName(libraryCard);
            if (userLibraryCard == null || userLibraryCard.size() == 0) {
                System.out.println("You are not a member of the library.");
                return false;
            }
            Book book = new Book();
            book.setTitle(title);
            book.setLibraryCard(userLibraryCard.get(0));
            List<Book> userBook = bookRepository.findByUser(book);
            if (userBook == null || userBook.size() == 0) {
                System.out.println("No such book under your account.");
                return false;
            }
            userBook.get(0).getLibraryCard().setId(0);
            if (bookRepository.update(userBook.get(0)) == 1) {
                return true;
            } else {
                throw new SQLException("Couldn't update book");
            }
        } catch (Exception e) {
            System.out.println("Return book exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean donateNewBook(Book book, Author author, Genre genre) {
        try {

            List<Genre> userGenre = genreRepository.findByName(genre);
            if (userGenre == null || userGenre.size() == 0) {
                int key = genreRepository.insert(genre);
                if(key == -1) {
                    throw new SQLException("Couldn't insert genre");
                }
                genre.setId(key);
            } else {
                genre.setId(userGenre.get(0).getId());
            }

            List<Author> userAuthor = authorRepository.findByName(author);
            if (userAuthor == null || userAuthor.size() == 0) {
                int key = authorRepository.insert(author);
                if(key == -1) {
                    throw new SQLException("Couldn't insert author");
                }
                author.setId(key);
            } else {
                author.setId(userAuthor.get(0).getId());
            }

            book.setAuthor(author);
            int key = bookRepository.insert(book);
            if(key == -1) {
                throw new SQLException("Couldn't insert book");
            }
            book.setId(key);

            if (genreRepository.postRelation(book, genre) != 1) {
                throw new SQLException("Couldn't insert relation");
            }

        } catch (Exception e) {
            System.out.println("Donate book exception: " + e.getMessage());
            e.printStackTrace();

        }
        return true;
    }

    private Book findAvailableBook(List<Book> books) {
        return books.stream()
                .filter(book -> book.getLibraryCard().getId() == 0)
                .findAny()
                .orElse(null);
    }

    private boolean isStillValid(String date) {
        return LocalDate.now().compareTo(LocalDate.parse(date)) < 0;
    }
}
