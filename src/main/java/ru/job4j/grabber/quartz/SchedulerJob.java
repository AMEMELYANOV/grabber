package ru.job4j.grabber.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Планировщик заданий
 *
 * @author Alexander Emelyanov
 * @version 1.0
 */
public class SchedulerJob {
    public static void main(String[] args) {
        Properties properties = loadProperties();
        int jobInterval = Integer.parseInt(properties.getProperty("scheduler.job.interval"));
        try (Connection cn = DriverManager.getConnection(
                properties.getProperty("url"),
                properties.getProperty("username"),
                properties.getProperty("password")
        )) {
            Class.forName(properties.getProperty("driver-class-name"));
            List<Long> store = new ArrayList<>();
            JobDataMap data = new JobDataMap();
            data.put("connection", cn);
            data.put("store", store);

            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(jobInterval)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);

            Thread.sleep(10000);
            scheduler.shutdown();
            System.out.println(store);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream in = SchedulerJob.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            System.out.println("Rabbit runs here ...");
            Connection cn = (Connection) context.getJobDetail().getJobDataMap().get("connection");
            try (
                 PreparedStatement ps = cn.prepareStatement("INSERT INTO "
                         + "data(created_date) VALUES (?)")) {
                ps.setLong(1, System.currentTimeMillis());
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            List<Long> store = (List<Long>) context.getJobDetail().getJobDataMap().get("store");
            store.add(System.currentTimeMillis());
        }
    }
}