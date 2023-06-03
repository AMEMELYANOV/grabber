package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

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
     * Ссылка на сайт
     */
    private static final String SOURCE_LINK = "https://career.habr.com";

    /**
     * Ссылка на ресурс
     */
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);

    /**
     * Счетчик объявлений
     */
    private int id = 1;

    /**
     * Список объявлений
     */
    private final List<Post> posts = new ArrayList<>();

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
    @Override
    public List<Post> list(String url, DateTimeParser dateTimeParser) throws IOException {
        Connection connection = Jsoup.connect(url);
        Document document = connection.get();
        Elements rows = document.select(".vacancy-card__inner");
        rows.forEach(row -> {
            Element titleElement = row.select(".vacancy-card__title").first();
            Element linkElement = titleElement.child(0);
            String vacancyName = titleElement.text();
            String vacancyDate = Objects.requireNonNull(row.select(".vacancy-card__date").first())
                    .child(0).attr("datetime");
            String link = String.format("%s%s", SOURCE_LINK, linkElement.attr("href"));
            String description = retrieveDescription(link);
            posts.add(Post.builder()
                    .id(id++)
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

    public static void main(String[] args) throws IOException {
        DateTimeParser dateTimeParser = new HabrCareerDateTimeParser();
        Parse parser = new HabrCareerParse();
        int numPages = 3;
        for (int i = 1; i <= numPages; i++) {
            parser.list(PAGE_LINK + i, dateTimeParser);
        }
    }
}