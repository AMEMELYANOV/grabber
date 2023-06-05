package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HabrCareerDateTimeParserTest {

    /**
     * Тест проверяет парсинг строки, когда строка представлена
     * в ожидаемом корректном виде.
     */
    @Test
    void parseShouldReturnLocalDateTimeWhenSuccess() {
        HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
        String dateTime = "2023-06-01T19:38:05+03:00";
        LocalDateTime expected = LocalDateTime.of(2023, 6, 1, 19, 38, 5);

        LocalDateTime result = parser.parse(dateTime);

        assertThat(result).isEqualTo(expected);
    }

    /**
     * Тест проверяет парсинг строки, когда строка представлена
     * в некорректном виде.
     */
    @Test
    void parseShouldThrowsExceptionWhenFail() {
        HabrCareerDateTimeParser parser = new HabrCareerDateTimeParser();
        String dateTime = "2023-06-0119:38:05+03:00";

        assertThrows(DateTimeParseException.class, () -> parser.parse(dateTime));
    }
}