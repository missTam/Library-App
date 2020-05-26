package repositories;

import java.sql.SQLException;

public interface ViewRepository {
    int createView() throws SQLException;
}
