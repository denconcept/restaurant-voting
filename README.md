Restaurant Voting REST API
===============================

### Task
Design and implement a REST API using Hibernate/Spring/SpringMVC (Spring-Boot preferred!) without frontend.<br>

The task is:<br>
Build a voting system for deciding where to have lunch.<br>
2 types of users: admin and regular users.<br>
Admin can input a restaurant, and it's lunch menu of the day (2-5 items usually, just a dish name and price).<br>
Menu changes each day (admins do the updates).<br>
Users can vote for a restaurant they want to have lunch at today.<br>
Only one vote counted per user.<br>
If user votes again the same day:<br>
If it is before 11:00 we assume that he changed his mind.<br>
If it is after 11:00 then it is too late, vote can't be changed.<br>
Each restaurant provides a new menu each day.<br>

As a result, provide a link to github repository.<br>
It should contain the code, README.md with API documentation and couple curl commands to test it (better - link to Swagger).<br>
P.S.: Make sure everything works with latest version that is on github :)<br>
P.P.S.: Assume that your API will be used by a frontend developer to build frontend on top of that.

### Stack
- Java 21
- Spring Boot 4
- Spring Security
- Spring Data JPA (Hibernate)
- H2 Database
- Maven
- Lombok
- Spring Cache
- Swagger / OpenAPI 3

### Credentials
```
User:  user@gmail.com / user
Admin: admin@gmail.com / admin
```

### Run in root directory
`mvn spring-boot:run`

### API Documentation
[Swagger UI](http://localhost:8080/swagger-ui/index.html)