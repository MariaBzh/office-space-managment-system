# API

## Доступные методы

1. **POST** /bookings/read
   - посмотреть бронирование
   - просмотр бронирования
2. **POST** /bookings/create
   - забронировать переговорную
3. **POST** /bookings/update
   - изменить бронирование
4. **POST** /bookings/delete
   - отменить бронирование
5. **POST** /bookings/search
   - поиск бронирований

## Описание сущностей

### Booking (Бронирование):

| Поле         | Обязательность | Тип      | Описание                         |
|--------------|----------------|----------|----------------------------------|
| userUid      | +              | String   | Идентификатор пользователя       |
| branch       | +              | Branch   | Филиал                           |
| floor        | +              | Floor    | Этаж                             |
| Office       | +              | Office   | Комната                          |
| workspaceUid | +              | String   | Идентификатор места бронирования |
| description  | -              | String   | Описание                         |
| startTime    | +              | String   | Дата и время начала брони        |
| endTime      | +              | String   | Дата и время окончания брони     |

### Branch (Филиал):

| Поле        | Обязательность | Тип     | Описание              |
|-------------|----------------|---------|-----------------------|
| branchUid   | +              | String  | Идентификатор филиала |
| name        | +              | String  | Наименование          |
| location    | +              | String  | Адрес                 |
| description | -              | String  | Описание              |

### Floor (Этаж):

| Поле        | Обязательность | Тип     | Описание              |
|-------------|----------------|---------|-----------------------|
| floorUid    | +              | String  | Идентификатор этажа   |
| level       | +              | String  | Номер этажа           |
| description | -              | String  | Описание              |

### Office (Кабинет):

| Поле        | Обязательность | Тип     | Описание               |
|-------------|----------------|---------|------------------------|
| officeUid   | +              | String  | Идентификатор кабинета |
| name        | +              | String  | Наименование кабинета  |
| description | -              | String  | Описание               |