#JAX-RS with JPA on Jersey#

###Testing the service###

1. Opening an account

```
curl -i -X POST -F "picture=@profile.png" -F "name=Krishna" -F "phone=9731423166" http://localhost:8080/bank-jaxrs/savings/customer
```

2. Login

```
curl -i -X POST -u <name>:<password> http://localhost:8080/bank-jaxrs/savings/login
```

3. Depositing amount

```
curl -H "Authorization: Bearer <key>" -H "Content-Type:application/json" -d '{"amount":100, "type":"deposit"}' http://localhost:8080/bank-jaxrs/savings/account/1/transaction
```

4. Withdrawing amount

```
curl -H "Authorization: Bearer <key>" -H "Content-Type:application/json" -d '{"amount":20, "type":"withdraw"}' http://localhost:8080/bank-jaxrs/savings/account/1/transaction
```

5. Account Details

```
curl -H "Authorization: Bearer <key>" http://localhost:8080/bank-jaxrs/savings/account/1
```