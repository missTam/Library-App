package utils;

import models.Author;
import models.Book;
import models.Genre;
import models.LibraryCard;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XLSMapper {

    // map genre data from excel to objects
    public static List<Genre> mapGenres(List<XSSFRow> data){
        List<Genre> genres = new ArrayList<>();
        for (XSSFRow row: data) {
            Iterator<Cell> cellIterator = row.cellIterator();
            Genre genre = new Genre();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                genre.setName(cell.getStringCellValue());
            }
            genres.add(genre);
        }
        return genres;
    }

    // map authors data from excel to objects
    public static List<Author> mapAuthors(List<XSSFRow> data){
        List<Author> authors = new ArrayList<>();
        for (XSSFRow row: data) {
            Iterator<Cell> cellIterator = row.cellIterator();
            Author author = new Author();
            int i = 0;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                i++;
                if (i == 1) {
                    author.setFirstName(cell.getStringCellValue());
                } else {
                    author.setLastName(cell.getStringCellValue());
                }
            }
            authors.add(author);
        }
        return authors;
    }

    // map library cards data from excel to objects
    public static List<LibraryCard> mapLibraryCards(List<XSSFRow> data){
        List<LibraryCard> libraryCards = new ArrayList<>();
        for (XSSFRow row: data) {
            Iterator<Cell> cellIterator = row.cellIterator();
            LibraryCard libraryCard = new LibraryCard();
            int i = 0;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                i++;
                switch(i) {
                    case 1:
                        libraryCard.setFirstName(cell.getStringCellValue());
                        break;
                    case 2:
                        libraryCard.setLastName(cell.getStringCellValue());
                        break;
                    case 3:
                        libraryCard.setValidUntil(cell.getStringCellValue());
                        break;
                }
            }
            libraryCards.add(libraryCard);
        }
        return libraryCards;
    }

    // map books data from excel to objects
    public static List<Book> mapBooks(List<XSSFRow> data){
        List<Book> books = new ArrayList<>();
        for (XSSFRow row: data) {
            Iterator<Cell> cellIterator = row.cellIterator();
            Book book = new Book();
            int i = 0;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                i++;
                switch(i) {
                    case 1:
                        book.setTitle(getCellDataAsString(cell));
                        break;
                    case 2:
                        book.setPublicationYear((int) cell.getNumericCellValue());
                        break;
                    case 3:
                        LibraryCard lc = new LibraryCard();
                        lc.setId((int) cell.getNumericCellValue());
                        book.setLibraryCard(lc);
                        break;
                    case 4:
                        Author a = new Author();
                        a.setId((int) cell.getNumericCellValue());
                        book.setAuthor(a);
                        break;
                }
            }
            books.add(book);
        }
        return books;
    }

    private static String getCellDataAsString(Cell cell){
        switch(cell.getCellType()){
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int)cell.getNumericCellValue());
            default:
                return "";
        }
    }
}
