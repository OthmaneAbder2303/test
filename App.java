package ma.ensa.lis;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class App extends Application {
    private static final Logger logger = LogManager.getLogger(App.class);
    private static Connection connection;

    public static void main(String[] args) {
        logger.info("Starting Laboratory Information System...");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Initializing JavaFX application...");

        // Load application properties
        Properties properties = loadProperties();
        if (properties == null) {
            logger.error("Failed to load application properties. Exiting...");
            return;
        }

        // Initialize Database Connection
        try {
            initDatabaseConnection(properties);
            logger.info("Database connection established successfully.");
        } catch (SQLException e) {
            logger.error("Error establishing database connection.", e);
            return;
        }

        // Load the main JavaFX view
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
            primaryStage.setTitle("Laboratory Information System");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            logger.error("Error loading main view.", e);
        }

        // Close database connection on application exit
        primaryStage.setOnCloseRequest(event -> closeDatabaseConnection());
    }

    private Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                logger.error("Unable to find application.properties.");
                return null;
            }
            properties.load(input);
            logger.info("Application properties loaded successfully.");
        } catch (IOException e) {
            logger.error("Error loading application properties.", e);
        }
        return properties;
    }

    private void initDatabaseConnection(Properties properties) throws SQLException {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");
        String driver = properties.getProperty("db.driver");

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            logger.error("Database driver not found.", e);
            throw new SQLException("Database driver not found.", e);
        }
    }

    private void closeDatabaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed.");
            }
        } catch (SQLException e) {
            logger.error("Error closing database connection.", e);
        }
    }

}
