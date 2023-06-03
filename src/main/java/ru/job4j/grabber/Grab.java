package ru.job4j.grabber;

import org.quartz.SchedulerException;

/**
 * Планировщик заданий
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
public interface Grab {

    /**
     * Инициализирует планировщик.
     *
     * @throws SchedulerException при ошибках в работе приложения
     * при работе планировщика
     */
    void init() throws SchedulerException;
}
