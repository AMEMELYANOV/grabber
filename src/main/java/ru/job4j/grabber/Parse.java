package ru.job4j.grabber;

import java.io.IOException;
import java.util.List;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.DateTimeParser;

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
     * @param url ссылка на ресурс для парсинга
     * @param dateTimeParser парсер даты и времени
     * @return список объявлений
     */
    List<Post> list(String url, DateTimeParser dateTimeParser) throws IOException;
}