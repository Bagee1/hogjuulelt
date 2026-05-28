package mn.edu.room.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {
    private static final DatabaseConnection INSTANCE = new DatabaseConnection();

    private final Properties properties = new Properties();

    private DatabaseConnection() {
        loadProperties();
        registerDriver();
    }

    public static DatabaseConnection getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        String url = getConfig("DB_URL", "db.url", "jdbc:postgresql://localhost:5432/room_booking");
        String user = getConfig("DB_USER", "db.user", "admin");
        String password = getConfig("DB_PASSWORD", "db.password", "secretpassword");
        return DriverManager.getConnection(url, user, password);
    }

    private void loadProperties() {
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("database.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load database.properties", ex);
        }
    }

    private void registerDriver() {
        try {
            Class.forName(getConfig("DB_DRIVER", "db.driver", "org.postgresql.Driver"));
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException("PostgreSQL JDBC driver is missing", ex);
        }
    }

    private String getConfig(String envKey, String propertyKey, String defaultValue) {
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }
        String propertyValue = properties.getProperty(propertyKey);
        if (propertyValue != null && !propertyValue.isBlank()) {
            return propertyValue;
        }
        return defaultValue;
    }
}
