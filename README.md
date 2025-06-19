# Transaction Service API

Сервис для управления транзакциями, кошельками и платежными запросами с поддержкой шардинга.

## Требования

- Java 17
- PostgreSQL 14+
- Maven или Gradle

## Установка и запуск

1. Создание баз данных:
```bash
psql -U postgres -f src/main/resources/db/init/init_databases.sql
```

2. Настройка конфигурации:
- Проверьте настройки в `src/main/resources/application.yml`
- При необходимости измените параметры подключения к базам данных

3. Запуск приложения:
```bash
./gradlew bootRun
```

## API Endpoints

### Кошельки
- `GET /api/v1/wallets` - получить список кошельков пользователя
- `GET /api/v1/wallets/{uid}` - получить информацию о кошельке
- `POST /api/v1/wallets` - создать новый кошелек
- `PUT /api/v1/wallets/{uid}` - обновить кошелек

### Транзакции
- `GET /api/v1/transactions` - поиск транзакций
- `GET /api/v1/transactions/{uid}/status` - получить статус транзакции
- `POST /api/v1/transactions` - создать новую транзакцию

### Платежные запросы
- `GET /api/v1/payment-requests/{uid}` - получить информацию о запросе
- `GET /api/v1/payment-requests/user/{userUid}` - получить список запросов пользователя
- `POST /api/v1/payment-requests` - создать новый запрос
- `POST /api/v1/payment-requests/{uid}/accept` - принять запрос
- `POST /api/v1/payment-requests/{uid}/reject` - отклонить запрос

## Архитектура

### Шардинг
- Горизонтальное шардирование по user_uid
- Две базы данных для распределения нагрузки
- Автоматическое распределение данных

### Миграции
- Flyway для управления схемой базы данных
- Отдельные миграции для каждой базы данных
- Автоматическое применение миграций при запуске

### Безопасность
- Валидация входящих данных
- Транзакционность операций
- Логирование действий

## Разработка

### Структура проекта
```
src/main/java/maxim/module4_4/transaction_service_api/
├── config/          # Конфигурационные классы
├── controller/      # REST контроллеры
├── dto/            # Data Transfer Objects
├── entity/         # JPA сущности
├── mapper/         # Мапперы для DTO
├── repository/     # JPA репозитории
└── service/        # Бизнес-логика
```

### Тестирование
```bash
./gradlew test
``` 