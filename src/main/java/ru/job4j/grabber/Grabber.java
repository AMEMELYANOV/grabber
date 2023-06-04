package ru.job4j.grabber;

import lombok.AllArgsConstructor;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.grabber.Store.PsqlStore;
import ru.job4j.grabber.Store.Store;
import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.parser.HabrCareerParse;
import ru.job4j.grabber.parser.Parse;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Реализация планировщика заданий
 *
 * @author Alexander Emelyanov
 * @version 1.0
 * @see ru.job4j.grabber.Grab
 */
@AllArgsConstructor
public class Grabber implements Grab {

    /**
     * Ссылка на сайт
     */
    private static final String SOURCE_LINK = "https://career.habr.com";

    /**
     * Ссылка на ресурс
     */
    private static final String PAGE_LINK = "/vacancies/java_developer?page=";

    /**
     * Конфигурация
     */
    private static final Properties cfg = new Properties();

    /**
     * Парсер
     */
    private final Parse parse;

    /**
     * Хранилище данных
     */
    private final Store store;

    /**
     * Планировщик
     */
    private final Scheduler scheduler;

    /**
     * Периодичность запуска задач
     */
    private final int time;

    /**
     * Количество сканируемых страниц
     */
    private final int numPages;

    /**
     * Инициализирует планировщик.
     *
     * @throws SchedulerException при ошибках в работе приложения
     *                            при работе планировщика
     */
    @Override
    public void init() throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        data.put("numPages", numPages);
        JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(time)
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    /**
     * Запускает сервер, отдающий данные по web запросу
     * на localhost:[порт], где порт указан в application.properties.
     *
     * @param store хранилище данных
     */
    public void web(Store store) {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(Integer.parseInt(cfg.getProperty("port")))) {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    try (OutputStream out = socket.getOutputStream()) {
                        out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                        for (Post post : store.getAll()) {
                            out.write(post.toString().getBytes());
                            out.write(System.lineSeparator().getBytes(Charset.forName("Windows-1251")));
                        }
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Выполняет переданную задачу.
     *
     * @author Alexander Emelyanov
     * @version 1.0
     */
    public static class GrabJob implements Job {

        /**
         * Метод выполнения задачи планировщика.
         * Выполняет сканирование страниц и сохранение
         * результата в базу данных.
         *
         * @param context контекст планировщика
         */
        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Start Job");
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            int numPages = (Integer) map.get("numPages");
            List<Post> posts = new ArrayList<>();
            for (int i = 1; i <= numPages; i++) {
                System.out.printf("Start parse %d page%n", i);
                posts.addAll(parse.list(SOURCE_LINK, PAGE_LINK + i));
            }
            for (Post post : posts) {
                System.out.println("Save post");
                store.save(post);
            }
        }
    }

    /**
     * Читает параметры из application.properties.
     * Выполняет запуск приложения. Создание основных объектов.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) throws Exception {
        try (InputStream in = Grabber.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            cfg.load(in);
        }
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        var parse = new HabrCareerParse(new HabrCareerDateTimeParser());
        var store = new PsqlStore(cfg);
        var time = Integer.parseInt(cfg.getProperty("time"));
        var numPages = Integer.parseInt(cfg.getProperty("num.pages"));
        Grabber grab = new Grabber(parse, store, scheduler, time, numPages);
        grab.init();
        grab.web(store);
    }
}