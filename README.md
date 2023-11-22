# real-estate-db-access
## Table of Contents

1) [Description](#Description)
2) [Database](#Database)
3) [Postman collection](#Postman)
4) [Technology Stack](#Stack)
5) [Run the Application](#Run)
6) [API Documentation](#Documentation)

<a id="Description"></a>
## Description

This microservice is part of the broader "Casamylinda" Project.

As the name implies It's main goal is to establish a connection with the database that holds all the information related to renting contracts and its stakeholders.

<a id="Database"></a>
## Database

You can find the EER diagram inside the "db" directory in both .pdf and .mwb formats.
The main database runs on a SQLServer instance.
The test database runs on a embedded H2 instance and database changes are managed with Flyway.

<a id="Postman"></a>
## Postman Collection

Inside the "postman" directory you can find the file containing the postman collection of endpoints that you can import to your running postman instance.

<a id="Stack"></a>
## Technology Stack

Technologies used in this project are:
- Java 17
- SpringBoot 3.1
- Maven
- JPA
- Hibernate
- Lombok
- Mapstruct
- Junit
- Mockito
- AssertJ
- DBRider
- SQLServer
- H2
- Flyway

<a id="Run"></a>
## Run the Application

Before you do anything make sure you have **JDK 17**, **Maven** and **Docker** installed in your machine.
You can do this by running `java --version`, `mvn --version` and `docker --version` commands in your terminal.

To run the application download the repository contents and run the command `mvn clean install` in its root directory.
This command will create the .jar file.

Once you do the previous steps run `docker compose up -d`. This will build the database server and microservice images and run them both as docker containers in the same virtual network.
Wait **10 to 20 seconds**!

The microservice will be accessible in localhost address, port 8080 through any http client.

<a id="Documentation"></a>
## API Documentation

The Restful API has been documented following the Open API standard.
The Swagger library generated the documentation.

Please refer to the following links:<br>
[swagger-ui](http://localhost:8080/swagger-ui/index.html#/)<br>
[json format](http://localhost:8080/v3/api-docs)
