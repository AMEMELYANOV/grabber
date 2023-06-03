package ru.job4j.grabber.parser;

import ru.job4j.grabber.model.Post;

import java.util.List;

/**
 * Парсер web страниц
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
public interface Parse {

    /**
     * Выполняет парсинг страницы, находит и извлекает краткое содержание
     * вакансии, дату и время размещения и ссылку на страницу с подробной
     * информацией о вакансии. Для парсинга используется библиотека Jsoup.
     * Данные сохраняются в список объявлений.
     *
     * @param sourceLink ссылка на сайт
     * @param pageLink ссылка на ресурс
     * @return список объявлений
     */
    List<Post> list(String sourceLink, String pageLink);
}