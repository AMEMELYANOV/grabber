package ru.job4j.grabber.store;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.job4j.grabber.model.Post;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class PsqlStoreTest {

    /**
     * Настройки соединения с хранилищем
     */
    private static Properties config;

    /**
     * Объект доступа к хранилищу
     */
    private Store store;

    /**
     * Объявление
     */
    private Post post;

    /**
     * Соединение с базой данных
     */
    private static Connection connection;

    /**
     * Создает необходимый для выполнения тестов объект.
     * Создание выполняется перед всеми тестами.
     */
    @BeforeAll
    public static void initConnection() {
        try (InputStream in = PsqlStore.class.getClassLoader()
                .getResourceAsStream("test.properties")) {
            config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            connection = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try (PreparedStatement statement = connection.prepareStatement("CREATE DATABASE grabber")) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Создает необходимые для выполнения тестов общие объекты.
     * Создание выполняется перед каждым тестом.
     */
    @BeforeEach
    public void setUp() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("delete from items")) {
            statement.execute();
        }
        store = new PsqlStore(config);
        post = Post.builder()
                .title("title")
                .link("link")
                .description("description")
                .build();
    }

    /**
     * Очищает хранилище данных после каждого теста.
     */
    @AfterEach
    public void wipeStore() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("delete from items")) {
            statement.execute();
        }
    }

    /**
     * Закрывает ресурсы после работы всех тестов.
     */
    @AfterAll
    public static void closeConnection() throws SQLException {
        connection.close();
    }

}