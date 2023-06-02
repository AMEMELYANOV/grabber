package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Реализация утилитного класса по конвертации формата даты
 *
 * @author Alexander Emelyanov
 * @version 1.0
 * @see ru.job4j.grabber.utils.DateTimeParser
 */
public class HabrCareerDateTimeParser implements DateTimeParser {

    /**
     * Возвращает объект LocalDateTime. Из переданной в метод строки
     * формирует объект содержащий дату и время. Для преобразования
     * используется {@link LocalDateTime#parse(CharSequence, DateTimeFormatter)}
     *
     * @param parse строка с датой и временем
     * @return LocalDateTime объект
     */
    @Override
    public LocalDateTime parse(String parse) {
        return LocalDateTime.parse(parse, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
