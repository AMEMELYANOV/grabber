package ru.job4j.grabber;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Парсер web страниц
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
public class HabrCareerParse {

    /**
     * Ссылка на сайт
     */
    private static final String SOURCE_LINK = "https://career.habr.com";

    /**
     * Ссылка на ресурс
     */
    private static final String PAGE_LINK = String.format("%s/vacancies/java_developer?page=", SOURCE_LINK);

    /**
     * Выполняет парсинг страницы, находит и извлекает краткое содержание
     * вакансии, дату и время размещения и ссылку на страницу с подробной
     * информацией о вакансии. Для парсинга используется библиотека Jsoup.
     *
     * @param url ссылка на ресурс для парсинга
     */
    public static void parseUrl(String url) throws IOException {
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
            System.out.printf("%s %s %s%n", vacancyName, link, vacancyDate);
        });
    }

    public static void main(String[] args) throws IOException {
        int numPages = 5;
        for (int i = 1; i <= numPages; i++) {
            System.out.println();
            System.out.println("============================");
            parseUrl(PAGE_LINK + i);
        }
    }
}