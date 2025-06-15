# StarAdvisor

Приложение для работы с банковскими продуктами компании и взаимодействия с клиентами 

[Полная инструкция по развёртыванию проекта](https://github.com/Alla-java/StarAdvisor/wiki/%D0%98%D0%BD%D1%81%D1%82%D1%80%D1%83%D0%BA%D1%86%D0%B8%D1%8F-%D0%BF%D0%BE-%D1%80%D0%B0%D0%B7%D0%B2%D1%91%D1%80%D1%82%D1%8B%D0%B2%D0%B0%D0%BD%D0%B8%D1%8E-%D0%BF%D1%80%D0%BE%D0%B5%D0%BA%D1%82%D0%B0)

## 🚀 Возможности
- Создание, хранение и выдача рекомендаций клиентам банка
- Поддержка информирования клиентов банка о новых продуктах через Telegram
- Отслеживание статистики самых популярных рекомендаций для клиента

## 🛠 Технологии
- **Языки:** < Java >
- **Фреймворки:** < Spring >
- **Базы данных:** <PostgreSQL/H2>
- **Инструменты:** <Git/Maven/Swagger>

## 📦 Установка
1. Клонировать репозиторий:
   ```bash
   git clone https://github.com/Alla-java/StarAdvisor.git
    ```
2. Установить зависимости:
   Убедитесь, что установлены:
   - JDK 17+ 
   - Apache Maven 4.0+
     
  Все зависимости указаны в pom.xml и автоматически устанавливаются при:
   ```bash
   mvn clean install
   ```
3. Базы данных
   
   Приложение работает с двумя СУБД:
   
### 1. Встроенная H2 (для хранения данных клиентов)
- **Файл БД**: `jdbc:h2:file:./transaction`  ссылка на файл - [transaction.db](./transaction.mv.db)
- **Доступ**: используется только для чтения
### 2. PostgreSQL (для взаимодействия с продуктами банка)
- **Управление**: Liquibase
- **Миграции**: `/src/main/resources/liquebase/db.changelog-master/`
  > 💡 Все изменения БД должны проводиться через миграции Liquibase!
- **Настройка**:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/database_name
spring.datasource.username=user_db_name
spring.datasource.password=user_db_password
```

4. Telegram bot
   
 Создайте бота в [@BotFather](https://t.me/BotFather)
 
 Получите и сохраните **API-токен**
 
### Конфигурация в приложении
Добавьте в `application.properties`:
```properties
# Telegram Bot
telegram.bot.token=${BOT_TOKEN}
```

## 🚀 Запуск приложения

Для запуска приложения в стандартном профиле (профиль по умолчанию) 
```properties
# Terminal
mvn spring-boot:run
```
    
