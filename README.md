# Transaction Service

## Требования

- Java 17
- Maven
- Docker и Docker Compose

## Запуск сервиса

### 1. Клонируйте репозиторий и перейдите к папке

```bash
git clone https://github.com/Pur1e/solvaTest.git

cd solvaTest
```

### 2. Соберите проект
```bash 
mvn clean package
```


### 3. Запустите Docker Compose
```bash
docker compose up -d
```

## Endpoints

### Запись транзакции

POST http://localhost:8080/api/v1/client/transactions
Запрос:
```json body
{
  "accountFrom": "(Long, 10 characters)",
  "accountTo": "(Long, 10 characters)",
  "amount": "decimal(15,2)",
  "currencyShortname": "RUB/KZT",
  "expenseCategory": "product/service"
}
```

### Установка лимита

POST http://localhost:8080/api/v1/client/limits
Запрос:
```json body
{
  "account": "(Long, 10 characters)",
  "limitSum": "decimal(15,2)",
  "category": "product/service"
}
```

### Получение отчета о превышении лимита

GET http://localhost:8080/api/v1/client/transactions/limit-exceeded?userId=<userId>
Ответ:
HTTP 200 OK
```json 
{
  "transactionId": "Long",
  "dateTime": "OffsetDateTime",
  "transactionAmount": "decimal(15,2)",
  "limitSum": "decimal(15,2)",
  "currency": "USD"
}
```
