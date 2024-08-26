# Задание 3 открытой школы T1

## Базовое веб-приложение с использованием Spring Security и JWT для аутентификации и авторизации пользователей.


### Описание проекта
Проект выполнен на базе первого задания с добавлением аутентификации с использованием JWT токенов и 
авторизации по ролям пользователей. 
Документация по API при помощи [Swagger](http://localhost:8080/swagger-ui/index.html)

### Реализация аутентификации и авторизации
 Первичная аутентификация происходит по паре username:password, при успешной аутентификации выдается пара токенов 
 (accessToken и refreshToken). Далее все запросы в закрытую часть API должны производится с использованием accessToken.
При истечении срока действия accessToken требуется запросить новый с использованием refreshToken.

### Настройка проекта
В application.yml требуется указать 4 обязательных параметра:
- accessExpirationMs - время жизни access токена
- refreshExpirationMs - время жизни refresh токена
- hs_256_access - ключ подписи access токена
- hs_256_refresh - ключ подписи refresh токена

```yaml
app:
  security:
    jwt:
      accessExpirationMs: ${JWT_ACCESS_EXPIRATION_MS:900000}  #15 min
      refreshExpirationMs: ${JWT_REFRESH_EXPIRATION_MS:1296000000} #15 days
      secret:
        hs_256_access: ${JWT_SECRET_ACCESS:qs5T4iesLQ9D+0VTiXrGqUDEgsVvrP3o/c1aam0vX0E=}
        hs_256_refresh: ${JWT_SECRET_REFRESH:R14CmRynq7dpgNbBJSABwFThq0Sa/SAG0BlUOdn3XKc=}
```

### Инструкции по запуску
Проект выполнен на базе Docker. Так же подготовлен docker-compose.yml файл для упрощенного разворачивания.
Для запуска в корневом каталоге проекта выполнить команду **docker compose up -d**

### Тестирование
- В тестах проекта проверяется доступ по ролям.
- В корневом каталоге есть запросы для Postman