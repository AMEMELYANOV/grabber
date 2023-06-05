package ru.job4j.grabber.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Утилита по конвертации формата даты
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
public interface DateTimeParser {

    /**
     * Возвращает объект LocalDateTime. Из переданной строки
     * формирует объект содержащий дату и время. Для преобразования
     * используется {@link LocalDateTime#parse(CharSequence, DateTimeFormatter)}
     *
     * @param parse строка с датой и временем
     * @return LocalDateTime объект
     */
    LocalDateTime parse(String parse);
}