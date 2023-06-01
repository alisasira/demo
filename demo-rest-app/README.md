### API Description

#### Create transaction

POST /api/transaction

Request-Body:

```
{
    "fromAccountNumber": "11111111",
    "toAccountNumber": "22222222",
    "amount": 250,
    "categoryName": "salary"
}
```

#### Export account transactions into .csv file
GET /api/export/{accountNumber}

### Predefined Account Numbers
11111111<br>
22222222<br>
33333333<br>
44444444<br>
55555555


### Predefined Categories
- salary
- grocery
- moneyTransfer
- onlineShopping

### Run Application
<p> chmod 777 run.sh </p>
<p> ./run.sh </p>

Application will be executed on <b>localhost:8080</b>

