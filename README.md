# Банковское приложение на основе Spring Boot, Security

## О проекте

Проект представляет собой банковское приложение, разработанное с использованием Spring Boot.

## Задание: Реализация веб-приложения "Личный кабинет"

### Требования:
- Пользователь может зарегистрироваться, паспорт должен проходить валидацию на количество и тип символов.
- Пользователь может открыть депозитный счет.
- Пользователь может открыть дебетовую или кредитную карту, баланс карты совпадает с балансом счета.
- В личном кабинете отображаются счета, карты и их баланс.
- К одному дебетовому карточному счету может быть прикреплено неограниченное количество карт.
- К одному кредитному карточному счету может быть прикреплена только одна кредитная карта.
- Реализована логика закрытия карты, в личном кабинете отображаются закрытые карты.

### Описание решения

Приложение представляет собой веб-приложение (личный кабинет), позволяющее пользователям управлять своими финансовыми операциями, включая открытие счетов и управление картами. Для хранения данных используется база данных PostgreSQL, которая автоматически обновляется с помощью Liquibase для создания необходимых таблиц.

### Дополнительные возможности

- Для упрощения работы с запросами добавлен Swagger, доступный по адресу [Swagger UI](http://localhost:8080/api-doc/swagger-ui/index.html#/).
- Для развертывания приложения создан файл `docker-compose.yml`.
