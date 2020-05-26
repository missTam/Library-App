package repositories;

import db.core.Template;
import org.apache.poi.ss.formula.functions.T;

import java.sql.SQLException;

import static utils.Constants.*;

public class ViewRepositoryImpl implements ViewRepository {

    private static final String CREATE_BOOKS_LIST_VIEW = "CREATE VIEW IF NOT EXISTS " +
            VIEW + " AS SELECT " + BOOKS + "." + ID + ", " + BOOKS + "." + TITLE +
            ", " + AUTHORS + "." + FIRST_NAME + ", " + AUTHORS + "." + LAST_NAME + ", " +
            GENRES + "." + NAME + ", " + BOOKS + "." + LIBRARY_CARD + " FROM " + BOOKS +
            " INNER JOIN " + AUTHORS + " ON " + BOOKS + "." + AUTHOR + " = " +
            AUTHORS + "." + ID + " INNER JOIN " + GENRES + " ON " + RELATION + "." +
            GENRE + " = " + GENRES + "." + ID + " INNER JOIN " + RELATION + " ON " +
            RELATION + "." + BOOK + " = " + BOOKS + "." + ID + " ORDER BY " +
            BOOKS + "." + TITLE + ", " + GENRES + "." + NAME;

    private Template<T> template = new Template<>();

    @Override
    public int createView() throws SQLException {
        return template.update(CREATE_BOOKS_LIST_VIEW, new Object[] {});
    }
}
