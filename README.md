# StarAdvisor

Приложение для работы с банковскими продуктами компании и взаимодействия с клиентами 

## 🚀 Возможности
- Создание, хранение и выдача рекомендаций клиентам банка
- Поддержка информирования клиентов банка о новых продуктах через Telegram
- Отслеживание статистики самых популярных рекомендаций для клиента

## 🛠 Технологии
- **Языки:** <Java>
- **Фреймворки:** <Spring>
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
    
