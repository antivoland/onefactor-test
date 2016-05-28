Тестовое задание
================

# Условие

Необходимо разработать сервис, обрабатывающий запросы о географических метках пользователей.

Сервис работает с двумя таблицами:

- Таблица 1 - "метки пользователей":
  - Колонки:
    - `user_id` - идентификатор пользователя, целое число, первичный ключ
    - `lon` и `lat` - долгота и широта местоположения метки в градусах, вещественные числа
  - Размер – от 1 до 10 млн. записей

- Таблица 2 - "географическая сетка":
  - Колонки:
    - `tile_x` и `tile_y` - координаты ячейки сетки, целые числа, образуют первичный ключ
    - `distance_error` - погрешность определения расстояний внутри ячейки в метрах, вещественное число. Данное расстояние определяет точность, с которой заданы координаты меток, находящихся в ячейке. В контексте данного задания это число задано извне, его не нужно вычислять.
  - Размер - порядка 10 тыс. записей

Сервис должен обрабатывать следующие типы запросов:

1. Проверка местоположения для заданного пользователя по его заданным текущим координатам - "рядом с меткой", если расстояние до метки не превышает погрешности в ячейке, где находится метка, или "вдали от метки".

2. Добавление нового пользователя с меткой, изменение или удаление метки существующего пользователя

3. Статистика по ячейке сетки - по заданным координатам произвольной точки внутри ячейки определить количество пользователей с меткой в данной ячейке

В простейшем варианте координаты ячейки сетки для заданных географических координат предлагается вычислять как:

- `tile_x` - целочисленная часть от `lon`
- `tile_y` - целочисленная часть от `lat`

# Решение

Для добавления или изменения пользовательской метки используются методы `PUT /api/dev/users/{id}` и `POST /api/dev/users/{id}` соответственно с телом запроса вида `{"lat": double, "lon": double}`. Удалить метку пользователя можно посредством метода `DELETE /api/dev/users/{id}/check-out`.

Например, мы хотим добавить метку первого пользователя, находящегося в районе Эйфелевой башни:

```http
PUT /api/dev/users/1 HTTP/1.1
Host: 127.0.0.1:8081
Content-Type: application/json

{"lat": 48.85889, "lon": 2.29583}
```

После чего её удалим:

```http
DELETE /api/dev/users/1 HTTP/1.1
Host: 127.0.0.1:8081
```

Проверить расстояние от пользователя до точки можно в методе `GET /api/dev/users/{id}/distance-to/{lat}:{lon}`.

Наконец, метод для подсчёта пользовательских меток: `GET /api/dev/points/{lat}:{lon}/neighbourhood`.
