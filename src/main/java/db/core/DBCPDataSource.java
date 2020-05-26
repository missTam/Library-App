package db.core;

import org.apache.commons.dbcp2.BasicDataSource;

import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.SQLException;

public class DBCPDataSource {

    private static final String DB_NAME = "mylibrary.db";
    private static final String PATH = FileSystems.getDefault().getPath("resources", DB_NAME).toAbsolutePath().toString();
    private static final String CONN_STRING = "jdbc:sqlite:" + PATH;

    private static BasicDataSource ds = new BasicDataSource();

    static {
        ds.setUrl(CONN_STRING);
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private DBCPDataSource(){ }
}
