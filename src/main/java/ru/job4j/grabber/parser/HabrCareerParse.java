package ru.job4j.grabber.parser;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.DateTimeParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Реализация парсера web страниц
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
public class HabrCareerParse implements Parse {

    /**
     * Список объявлений
     */
    private final List<Post> posts = new ArrayList<>();

    /**
     * Список объявлений
     */
    private final DateTimeParser dateTimeParser;

    /**
     * Конструктор. Принимает параметром парсер строки
     * даты и времени.
     * @param dateTimeParser парсер даты и времени
     */
    public HabrCareerParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

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
    @Override
    public List<Post> list(String sourceLink, String pageLink) {
        Connection connection = Jsoup.connect(sourceLink + pageLink);
        Document document = null;
        try {
            document = connection.get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Elements rows = document.select(".vacancy-card__inner");
        rows.forEach(row -> {
            Element titleElement = row.select(".vacancy-card__title").first();
            Element linkElement = titleElement.child(0);
            String vacancyName = titleElement.text();
            String vacancyDate = Objects.requireNonNull(row.select(".vacancy-card__date").first())
                    .child(0).attr("datetime");
            String link = String.format("%s%s", sourceLink, linkElement.attr("href"));
            String description = retrieveDescription(link);
            posts.add(Post.builder()
                    .title(vacancyName)
                    .description(description)
                    .link(link)
                    .created(dateTimeParser.parse(vacancyDate))
                    .build());
        });
        return posts;
    }

    /**
     * Выполняет парсинг страницы описания вакансии.
     * Возвращает описание вакансии.
     *
     * @param link ссылка страницу с описанием вакансии
     */
    private String retrieveDescription(String link) {
        String description;
        Connection connection = Jsoup.connect(link);
        try {
            Document document = connection.get();
            description = document.select(".vacancy-description__text").first().text();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return description;
    }
}