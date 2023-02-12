## User management system

## Prerequisites
- JDK 8
- Maven
- PostgreSQL
- Postman

## Installation
- Clone the repository
```bash
$ git clone https://github.com/Anton-Akinin/user-management-system.git
```

- Install dependencies
```bash
$ cd user-management-system
$ mvn install
```

- You need to manualy create DB in PostgreSQL with name:
```
userManagementDB
```

- Update the application.properties file with your data. You need to fill:
```
spring.datasource.username=
spring.datasource.password=
```
- Start Spring Boot Application



## Usage example as admin
I would recommend using postman to create API requests.  

By default, DB contains admin user with credentials:
```
admin@example.com
adminpass
```

You can log in with this endpoint:
```
POST localhost:8080/api/v1/auth/login
```
Also you need to peovide login data in body:
```
{
    "email": "admin@example.com",
    "password": "adminpass"
}
```
After that, you will receive a token in return.  
```
{
    "email": "admin@example.com",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBleGFtcGxlLmNvbSIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNjc2MjE1MjA4LCJleHAiOjE2NzYyMTg4MDh9.CzJ6whG92MdpiQcCRuLQ2bTE9KIur7HdCohwsWwo4Fo"
}
```
With this token, you can create a new user because you are logged in as an admin user.  
For this you need to call this endpoint:
```
POST localhost:8080/api/v1/admin/user
```
And paste token to here:![img.png](img.png)  
Also, you need provide json for creating new user. For example:
```
{
    "firstName": "Anton",
    "lastName": "Akinin",
    "email": "AkininAnton@outlook.com",
    "gender": "male"
}
```
But you need to change the email address to the one you have access to.  
After this you will receive email with credentials.  
If you try to log in with them, you will receive a message that you need to reset your password.  

For this you need to call this endpoint:
```
PUT localhost:8080/api/v1/auth/reset-password
```
With body like this:
```
{
    "email": "AkininAnton@outlook.com",
    "oldPassword": "pX1%Oy^m3n",
    "newPassword": "qwerty523",
    "confirmNewPassword": "qwerty523"
}
```
After this you will be able to log in.


## Usage example as user
First you can register. For this you need to call this endpoint:
```
POST localhost:8080/api/v1/auth/register
```
With body like this:
```
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "johndoe@example.com",
  "password": "password",
  "confirmPassword": "password",
  "birthDate": "1990-01-01",
  "gender": "male"
}
```
After this you will receive token. With them, you can call getCurrentUser method:
```
GET localhost:8080/api/v1/user
```
After this you will know your ID and with it, you can update information about current user:
```
PUT localhost:8080/api/v1/user/{id}
{
    "firstName": "Jonathan"
}
```



## Other usage examples

I did not give examples for all endpoints. For more information about endpoints, you can go here:
```
http://localhost:8080/swagger-ui.html
```