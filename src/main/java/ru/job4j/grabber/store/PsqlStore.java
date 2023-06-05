package ru.job4j.grabber.store;

import ru.job4j.grabber.model.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Реализация хранилища объявлений
 *
 * @author Alexander Emelyanov
 * @version 1.0
 * @see ru.job4j.grabber.store.Store
 */
public class PsqlStore implements Store, AutoCloseable {

    /**
     * Соединение с базой данных
     */
    private final Connection cnn;

    /**
     * Конструктор. Получает параметром объект
     * Properties, на основании которого инициализирует
     * объект Connection.
     *
     * @param cfg объект конфигурации настройки соединения с БД
     */
    public PsqlStore(Properties cfg) throws SQLException {
        try {
            Class.forName(cfg.getProperty("driver-class-name"));
            cnn = DriverManager.getConnection(
                    cfg.getProperty("url"),
                    cfg.getProperty("username"),
                    cfg.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Сохраняет объявление.
     *
     * @param post объявление
     */
    @Override
    public void save(Post post) {
        try (PreparedStatement statement =
                     cnn.prepareStatement("INSERT INTO posts(title, link, description, created) "
                             + "VALUES (?, ?, ?, ?) ON CONFLICT (link) DO NOTHING")) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getLink());
            statement.setString(3, post.getDescription());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает список объявления.
     *
     * @return список объявления
     */
    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement ps = cnn.prepareStatement("SELECT * FROM posts")) {
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(
                            Post.builder()
                            .id(resultSet.getInt("id"))
                            .title(resultSet.getString("title"))
                            .description(resultSet.getString("description"))
                            .link(resultSet.getString("link"))
                            .created(resultSet.getTimestamp("created").toLocalDateTime())
                            .build());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    /**
     * Возвращает объявление по идентификатору.
     *
     * @param id идентификатор объявления
     * @return объявление
     */
    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement ps = cnn.prepareStatement("SELECT * FROM posts WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    post = Post.builder()
                            .id(resultSet.getInt("id"))
                            .title(resultSet.getString("title"))
                            .description(resultSet.getString("description"))
                            .link(resultSet.getString("link"))
                            .created(resultSet.getTimestamp("created").toLocalDateTime())
                            .build();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    /**
     * Закрывает соединение с базой данных.
     */
    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }
}
