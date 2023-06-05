package ru.job4j.grabber.store;

import ru.job4j.grabber.model.Post;

import java.util.List;

/**
 * Хранилище объявлений
 *
 * @author Alexander Emelyanov
 * @version 1.0
 * @see ru.job4j.grabber.model.Post
 */
public interface Store {

    /**
     * Сохраняет объявление.
     *
     * @param post объявление
     */
    void save(Post post);

    /**
     * Возвращает список объявления.
     *
     * @return список объявления
     */
    List<Post> getAll();

    /**
     * Возвращает объявление по идентификатору.
     *
     * @param id идентификатор объявления
     * @return объявление
     */
    Post findById(int id);
}