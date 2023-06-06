# grabber

## <p id="contents">Оглавление</p>

<ul>
<li><a href="#01">Описание проекта</a></li>
<li><a href="#02">Стек технологий</a></li>
<li><a href="#03">Требования к окружению</a></li>
<li><a href="#04">Сборка и запуск проекта</a>
    <ol type="1">
        <li><a href="#0401">Сборка проекта</a></li>
        <li><a href="#0402">Запуск проекта</a></li>
    </ol>
</li>
<li><a href="#05">Взаимодействие с приложением</a>
    <ol  type="1">
        <li><a href="#0501">Создание заявки</a></li>
        <li><a href="#0502">Просмотр списка заявок</a></li>
        <li><a href="#0503">Редактирование заявки</a></li>
        <li><a href="#0504">Удаление заявки</a></li>
        <li><a href="#0505">Поиск заявки по id</a></li>
        <li><a href="#0506">Поиск заявки по наименованию</a></li>
        <li><a href="#0507">Выход из приложения</a></li>
    </ol>
</li>
<li><a href="#contacts">Контакты</a></li>
</ul>

## <p id="01">Описание проекта</p>

Консольное приложение выполняет парсинг сайта, с вакансиями.
* Работа по расписанию.
* Парсинг сайта с вакансиями (https://career.habr.com).
* Сохранение вакансий в базу данных.
* Предоставляет данные для просмотра через браузер.

## <p id="02">Стек технологий</p>
- Java 18
- PostgreSQL 14
- JUnit 5
- Gradle 8.1

  Инструменты:

- Javadoc, Checkstyle, Jacoco

## <p id="03">Требования к окружению</p>

Java 17, Maven 3.8, PostgreSQL 14

<p><a href="#contents">К оглавлению</a></p>

## <p id="04">Сборка и запуск проекта</p>

### <p id="0401">1. Сборка проекта</p>

Команда для сборки в jar:

`gradlew build shadowJar`

### <p id="0402">2. Запуск проекта</p>

Перед запуском проекта необходимо создать базу данных grabber
в PostgreSQL, команда для создания базы данных:

`create database tracker;`

Далее необходимо средствами PostgreSQL выполнить скрипт
`./db/scripts/001_ddl_create_posts_table.sql` для создания
таблицы, в которую будут записывать объявления.

Команда для запуска приложения:

`java -jar build/libs/grabber-1.0-SNAPSHOT-all.jar`

## <p id="contacts">Контакты</p>

[![alt-text](https://img.shields.io/badge/-telegram-grey?style=flat&logo=telegram&logoColor=white)](https://t.me/T_AlexME)&nbsp;&nbsp;
[![alt-text](https://img.shields.io/badge/@%20email-005FED?style=flat&logo=mail&logoColor=white)](mailto:amemelyanov@yandex.ru)&nbsp;&nbsp;

<p><a href="#contents">К оглавлению</a></p>