package util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public final class DBUtil {

    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/contactdb";
    private static final String DB_USER = "dbuser";
    private static final String DB_PASSWORD = "dbpassword";
    private static final int MAX_POOL_SIZE = 20;
    private static final String VALIDATION_QUERY = "SELECT 1";

    private static final HikariDataSource dataSource = createDataSource();

    private DBUtil() {
        // Utility class, no instances.
    }

    private static HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(MAX_POOL_SIZE);
        config.setConnectionTestQuery(VALIDATION_QUERY);
        config.setPoolName("ContactManagerPool");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return new HikariDataSource(config);
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
