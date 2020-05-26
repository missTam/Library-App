package db.core;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Template<T> {

    // select item(s)
    public List<T> query(String sql, Object[] params, RowMapper<T> mapper) throws SQLException {
        List<T> rows = new ArrayList<>();
        // getting a pooled connection:
        Connection conn = DBCPDataSource.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql);
        if(params != null && params.length > 0){
            setParameters(statement, params);
        }
        ResultSet results = statement.executeQuery();
        while (results.next()) {
            rows.add(mapper.mapRow(results));
        }
        statement.close();
        conn.close();
        return rows;
    }

    // insert and updateAndReturnKey item
    public int updateAndReturnKey(String sql, Object[] params) throws SQLException {
        Connection conn = DBCPDataSource.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql);
        setParameters(statement, params);
        statement.executeUpdate();
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        }
        statement.close();
        conn.close();
        return -1;
    }

    // delete item (or insert/ update where key not a requirement)
    public int update(String sql, Object[] params) throws SQLException {
        Connection conn = DBCPDataSource.getConnection();
        PreparedStatement statement = conn.prepareStatement(sql);
        setParameters(statement, params);
        int affectedRows = statement.executeUpdate();
        statement.close();
        conn.close();
        return affectedRows;
    }

    private void setParameters(PreparedStatement statement, Object[] params) throws SQLException {
        for(int i = 1; i <= params.length; i++) {
            if(String.valueOf(params[i-1]).equals("0")) {
                statement.setNull(i, Types.INTEGER);
            } else {
                statement.setObject(i, params[i-1]);
            }
        }
    }
}
