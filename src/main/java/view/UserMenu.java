package view;

import controllers.BookController;
import controllers.GenreController;
import models.*;
import repositories.*;
import utils.By;


import javax.inject.Inject;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class UserMenu {

    private Scanner scanner;

    private @Inject
    BookController bookController;
    private @Inject
    GenreController genreController;

    public UserMenu() {
        this.scanner = new Scanner(System.in);
        this.bookController = new BookController(new BookRepositoryImpl(), new LibraryCardRepositoryImpl(), new GenreRepositoryImpl(), new AuthorRepositoryImpl());
        this.genreController = new GenreController(new GenreRepositoryImpl());
    }

    public void showUserMenu() {
        System.out.print("Welcome to the library. What would you like to do? \n" +
                "1 - Search book(s) \n2 - Borrow book \n3 - Return book \n" +
                "4 - Donate book \n5 - Exit \nEnter your number:");
        System.out.println();
    }

    public boolean performAction(int number) throws NumberFormatException {
        switch (number) {
            case 1:
                return chooseSearchMethod();
            case 2:
                return typeInToBorrow();
            case 3:
                return returnBook();
            case 4:
                return donateBook();
            case 5:
                return showGoodbyeMsg();
            default:
                return invalidInput();
        }
    }

    private boolean showGoodbyeMsg() {
        System.out.println("Goodbye!");
        return true;
    }

    public boolean invalidInput() {
        System.out.println("Please check your input.");
        return false;
    }

    public void success() {
        System.out.println("Finished! Thank you for your visit.");
    }

    public void failure() {
        System.out.println("Sorry, we were unable to process your request.");
    }

    private boolean chooseSearchMethod() throws NumberFormatException {
        System.out.print("How would you like to search book(s)? \n" +
                "1 - By title \n2 - By author \n3 - By genre \n" +
                "4 - Exit \nEnter your number:");
        System.out.println();
        switch (Integer.parseInt(scanner.nextLine())) {
            case 1:
                return findBookBy(By.TITLE, null);
            case 2:
                return findBookBy(By.AUTHOR, null);
            case 3:
                return showGenres();
            case 4:
                return showGoodbyeMsg();
            default:
                return invalidInput();
        }
    }

    private boolean showGenres() {
        List<Genre> genres = genreController.findAllGenres();
        genres.forEach(genre -> {
            System.out.print(genre.getId() + " - " + genre.getName());
            System.out.println();
        });
        return findBookBy(By.GENRE, genres);
    }

    private boolean findBookBy(By criterion, List<Genre> genres) throws NumberFormatException {

        String template = "Type in the %s: ";
        String result = String.format(template, enumToStr(criterion));
        System.out.println(result);
        String input = scanner.nextLine();

        if (!isValid(input)) {
            return invalidInput();
        }

        List<Book> books = null;
        switch (criterion) {
            case TITLE:
                books = bookController.findBooks(input, By.TITLE);
                break;
            case AUTHOR:
                books = bookController.findBooks(input, By.AUTHOR);
                break;
            case GENRE:
                if(Integer.parseInt(input) > genres.size()) {
                    return invalidInput();
                }
                books = bookController.findBooks(filterGenre(genres, Integer.parseInt(input)), By.GENRE);
                break;
        }
        if (books == null || books.size() == 0) {
            System.out.println("No books found.");
            return false;
        } else {
            displayBooks(books);
            return selectToBorrow(books);
        }

    }

    private void displayBooks(List<Book> books) {
        int i = 0;
        for (Book book: books) {
            i++;
            book.setDisplayIndex(i);
            System.out.print(book.getDisplayIndex() + " - " + book.getTitle() + " | "
            + book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName());
            System.out.println();
        }
    }

    private boolean selectToBorrow(List<Book> books) throws NumberFormatException {
        System.out.print(books.size() + 1 + " - Exit \nType in the book's number to borrow: ");
        System.out.println();
        final int i = Integer.parseInt(scanner.nextLine());

        if (i <= books.size() && i >= 1) {
            Book selected = books.stream()
                    .filter(book -> i == book.getDisplayIndex())
                    .findAny()
                    .orElse(null);
            return borrowBook(selected.getTitle());
        } else if (i == books.size() + 1) {
            return showGoodbyeMsg();
        } else {
            return invalidInput();
        }
    }

    private boolean typeInToBorrow() {
        System.out.println("Type the name of the book: ");
        String input = scanner.nextLine();
        if(!isValid(input)) {
            return invalidInput();
        }
        return borrowBook(input);
    }

    private boolean borrowBook(String title) {
        LibraryCard lc = provideLibraryCard();
        if(lc == null) {
            return invalidInput();
        }
        return bookController.borrowBook(title, lc);
    }

    private boolean returnBook() {
        String title = provideBookTitle();
        if (title == null) {
            return invalidInput();
        }
        LibraryCard lc = provideLibraryCard();
        if(lc == null) {
            return invalidInput();
        }
        return bookController.returnBook(title, lc);
    }

    private boolean donateBook() throws NumberFormatException {

        Book book = new Book();
        Author author = new Author();
        Genre genre = new Genre();

        String title = provideBookTitle();
        if (title == null) {
            return invalidInput();
        }
        book.setTitle(title);

        System.out.println("Provide publication year: ");
        int pubYear = Integer.parseInt(scanner.nextLine());
        if (!String.valueOf(pubYear).matches("\\d(1-4)") && pubYear > LocalDate.now().getYear()) {
            return invalidInput();
        }
        book.setPublicationYear(pubYear);

        System.out.println("Provide author's first name: ");
        String firstName = scanner.nextLine();
        if (!isValid(firstName)) {
            return invalidInput();
        }
        author.setFirstName(firstName);

        System.out.println("Provide author's last name:");
        String lastName = scanner.nextLine();
        if (!isValid(lastName)) {
            return invalidInput();
        }
        author.setLastName(lastName);

        System.out.println("Provide genre: ");
        String genreType = scanner.nextLine();
        if (!isValid(genreType)) {
            return invalidInput();
        }
        genre.setName(genreType);

        return bookController.donateNewBook(book, author, genre);

    }

    private String provideBookTitle() {
        System.out.println("Provide book title: ");
        String title = scanner.nextLine();
        if (!isValid(title)) {
            return null;
        }
        return title;
    }

    private LibraryCard provideLibraryCard() {

        System.out.println("Enter your first name:");
        String firstName = scanner.nextLine();
        if (!isValid(firstName)) {
            return null;
        }
        System.out.println("Enter your last name:");
        String lastName = scanner.nextLine();
        if (!isValid(lastName)) {
            return null;
        }
        LibraryCard lc = new LibraryCard();
        lc.setFirstName(firstName);
        lc.setLastName(lastName);
        return lc;
    }

    private boolean isValid(String input) {
        return !input.equalsIgnoreCase("") && input.length() <= 50;
    }

    private String enumToStr(By criterion) {
        switch (criterion) {
            case TITLE:
                return "book title";
            case AUTHOR:
                return "author last name";
            case GENRE:
                return "genre number";
            default:
                return "";
        }
    }

    private String filterGenre(List<Genre> genres, int index) {
        for (Genre genre : genres) {
            if (genre.getId() == index) {
                return genre.getName();
            }
        }
        return null;
    }
}
