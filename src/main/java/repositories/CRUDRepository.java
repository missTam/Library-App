package repositories;
import java.sql.SQLException;
import java.util.List;

public interface CRUDRepository<T> {
    List<T> findAll() throws SQLException;
    T findById(int id) throws SQLException;
    List<T> findByName(T object) throws SQLException;
    int insert(T object) throws SQLException;
    int update(T object) throws SQLException;
    int delete(int id) throws SQLException;
}
