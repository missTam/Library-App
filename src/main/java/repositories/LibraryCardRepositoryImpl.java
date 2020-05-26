package repositories;

import db.core.Template;

import models.LibraryCard;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static utils.Constants.*;
import static utils.Constants.LAST_NAME;

public class LibraryCardRepositoryImpl implements LibraryCardRepository {

    private static final String INSERT = "INSERT INTO " + LIBRARY_CARDS + " (" +
            FIRST_NAME + ", " + LAST_NAME + ", " + VALID_UNTIL + ") VALUES(?, ?, ?)";

    private static final String QUERY = "SELECT * FROM " + LIBRARY_CARDS + " WHERE " +
            FIRST_NAME + "= ?" + " AND " + LAST_NAME + "= ?";

    private Template<LibraryCard> template = new Template<>();

    @Override
    public List<LibraryCard> findByName(LibraryCard libraryCard) throws SQLException {
        return template.query(QUERY, new Object[] { libraryCard.getFirstName().toLowerCase(),
        libraryCard.getLastName().toLowerCase()}, results -> {
            LibraryCard lc = new LibraryCard();
            lc.setId(results.getInt("_id"));
            lc.setFirstName(results.getString("first_name"));
            lc.setLastName(results.getString("last_name"));
            lc.setValidUntil(results.getString("valid_until"));
            return lc;
        });
    }

    @Override
    public int insert(LibraryCard libraryCard) throws SQLException {
        return template.updateAndReturnKey(INSERT, new Object[] {
                libraryCard.getFirstName().toLowerCase(),
                libraryCard.getLastName().toLowerCase(),
                LocalDate.now().plusYears(1).toString()
        });
    }

    @Override
    public LibraryCard findById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<LibraryCard> findAll() throws SQLException {
        return null;
    }

    @Override
    public int update(LibraryCard object) throws SQLException {
        return 0;
    }

    @Override
    public int delete(int id) throws SQLException {
        return 0;
    }
}
