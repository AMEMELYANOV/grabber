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
     * ��������� ���������� � ����������
     */
    private static Properties config;

    /**
     * ������ ������� � ���������
     */
    private Store store;

    /**
     * ����������
     */
    private Post post;

    /**
     * ���������� � ����� ������
     */
    private static Connection connection;

    /**
     * ������� ����������� ��� ���������� ������ ������.
     * �������� ����������� ����� ����� �������.
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
     * ������� ����������� ��� ���������� ������ ����� �������.
     * �������� ����������� ����� ������ ������.
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
     * ������� ��������� ������ ����� ������� �����.
     */
    @AfterEach
    public void wipeStore() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("delete from items")) {
            statement.execute();
        }
    }

    /**
     * ��������� ������� ����� ������ ���� ������.
     */
    @AfterAll
    public static void closeConnection() throws SQLException {
        connection.close();
    }

}