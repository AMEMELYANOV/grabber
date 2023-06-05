package ru.job4j.grabber.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Модель данных объявление
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
@Builder
@Setter
@Getter
@Data
public class Post {

    /**
     * ID объявления
     */
    private int id;

    /**
     * Заголовок объявления
     */
    private String title;

    /**
     * Ссылка объявления
     */
    private String link;

    /**
     * Описание объявления
     */
    private String description;

    /**
     * Дата и время создания объявления
     */
    private LocalDateTime created;
}
