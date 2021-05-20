<h1 align="center">
  Spring Market
</h1>

## Table of Contents ##
1. [Problem](#Problem)
2. [Application](#Application)
3. [Technology](#Technology)
4. [Application Structure](#Application-Structure)
5. [Run Locally](#Running-the-server-locally)
6. [RabbitMQ](#RabbitMQ)
7. [API Documentation](#API-Documentation)


## Problem ##
A local supermarket needs a system with several modules to help its business. These are:
- Inventory and product handling;
- Payments with history, so that each payment updates the inventory;
- User handling (cashiers and administrators);
- Report generation.

In addition, the supermarket needs to implement a queuing system for reporting services, as well as alerting whenever an item in stock is close to running out.

## Application ##

The application consists in the implementation of an Admin portal which can be operated over browsers, and a series of REST APIs to interact with the system. The complete system has three important actors :

1. Admin
2. User
3. Cashier

The _Admin_ and can access this application on browser and can perform all the actions provided by the endpoints.

The _Cashier_ can also access the application and perform most of the actions provided by the endpoints

The _User_ can perform the following actions :

1. Signup (Create his account);
2. Login (and get a JWT token);
3. Create new orders, payments, orderDetails;
4. Read orders, payments and orderDetails that are related to his account.


Both Admin, User and Cashier use the JWT authentication for access.

Any changes that the admin or cashier users do on the web portal will impact the search results of the end users.

## Database Schema ##
The current schema can be accessed at: https://drive.google.com/file/d/11bl3ZCg3MyfdfIIMuZCuBPzwhF451jKM/view?usp=sharing

It looks as follows:

<p align="center">
    <a href="https://freeimage.host/i/BNzzLg"><img src="https://iili.io/BNzzLg.md.jpg" alt="BNzzLg.md.jpg" border="0"></a>
</p>

## Technology ##
Following libraries were used during the development of this application:

- **Spring Boot** - Server side framework
- **MySQL** - Database
- **Swagger** - API documentation
- **JWT** - Authentication mechanism for REST APIs
- **RabbitMQ** - Queues and queue management

## Application Structure ##
The project structure look as follows :

**_Models & DTOs_**

The various models of the application are organized under the **_model_** package, their DTOs(data transfer objects) are present under the **_dto_** package.

**_DAOs_**

The data access objects (DAOs) are present in the **_repository_** package. They help the service layer to persist and retrieve the data from MySQL. The service layer is defined under the **_service_** package.

**_Security_**

The security setting are present under the **_config_** package and the actual configurations are done under the class present in the **_security_** package.

**_Controllers_**

The controller layer is present in the **_controller_** package. The REST API controllers are located under the **_api_** package and the corresponding request classes are located under the **_request_** package.

The static resources are grouped under the **_resources_** directory.

## Response and Exception Handling ##
The application uses a sort of a mini framework for handling the entire application's exceptions using a few classes and the properties file. The **_exception_** package, it consists of  enums - EntityType and ExceptionType. The EntityType enum contains all the entity names that we are using in the system for persistence and those which can result in a run time exception. The ExceptionType enum consists of the different entity level exceptions such as the EntityNotFound and DuplicateEntity exceptions.

Another important part of this mini framework is the **_CustomizedResponseEntityExceptionHandler_** class. This class takes care of these RuntimeExceptions before sending a response to the API requests. Its a controller advice that checks if a service layer invocation resulted in a EntityNotFoundException or a DuplicateEntityException and sends an appropriate response to the caller.

Last, the API response are all being sent in a uniform manner using the **_Response_** class present in the dto/response package.

## Setting up your DB ##
Open the following files below and fill in the connection string with the information from your database instance. Remember that you should create two instances, one for your application and another one to be used for the unit tests.

```
market: src>main>resources>application.yml
```

```
market_test: src>test>resources>application.yml
```

Example:
```
url: jdbc:mysql://localhost:3306/YOUR_DATABASE
username: YOUR_USERNAME
password: YOUR_PASSWORD
```

**To facilitate the installation of the database image, you can use the docker-compose.yml that is in the project.**

## Running the server locally ##
Requirements:

- JDK corretto-1.8.0_292

To be able to run this Spring Boot app you will need to first build it. To build and package a Spring Boot app into a single executable Jar file with a Maven, use the command below. You will need to run it from the project folder which contains the pom.xml file.

```
mvn clean install
```

To run the Spring Boot app from a command line in a Terminal window you can use the java -jar command. This is provided your Spring Boot app was packaged as an executable jar file.

You can also use Maven plugin to run the app. Use the below example to run your Spring Boot app with Maven plugin :

```
mvn spring-boot:run
```

If you do not have a mysql instance running and still just want to create the JAR, then please use the following command:

```
mvn install -DskipTests
```

This will skip the test cases and won't check the availability of a mysql instance and allow you to create the JAR.

You can follow any/all the above commands, or simply use the run configuration provided by your favorite IDE and run/debug the app from there for development purposes.

Once the server is set up you should be able to access the admin interface via swagger at the following URL :


http://localhost:8080/swagger-ui.html#

There you can find and test the application endpoints.


## RabbitMQ ##
To be able to run RabbitMQ, make sure to run the command:

```
docker-compose up -d rabbit
```

To access the RabbitMQ interface, go to: http://localhost:15672/

Credentials for username and password are the following:

```
Username:guest
Password:guest
```

## API Documentation ##
IThe tool for API documentation used in this project is Swagger. You can open it inside a browser at the following url -

http://localhost:8080/swagger-ui.html#

<p align="center">

<a href="https://freeimage.host/i/BNzHXe"><img src="https://iili.io/BNzHXe.md.png" alt="BNzHXe.md.png" border="0"></a>

</p>

It will present you with a well-structured UI which contains all the end points of the application.

You can use the User spec to execute the login api for generating the Bearer token. The token then should be applied in the "Authorize" popup which will by default apply it to all secured apis (get and post both), thus giving you access to some of the endpoints if you are a regular user, or all endpoints if you are an admin.

**IMPORTANT NOTE: Don’t forget to insert “Bearer” before the token in the 'authorize' popup. Otherwise, the authentication won’t work!**
