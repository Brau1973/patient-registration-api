# Patient Registration API

This project is a complete CRUD (Create, Read, Update, Delete) application for managing patient data. It is implemented using Java 21 and Spring Boot 3.2.5.

## Features

- **Data Validation**: The application uses `spring-boot-starter-validation` for validating the input data. This ensures that the data stored in the database is accurate and reliable.

- **Database Persistence**: The application uses Java Persistence API (JPA) for managing the database operations. The database used for this project is MySQL.

- **Asynchronous Email Confirmation**: After a patient is registered, an asynchronous confirmation email is sent to the patient's email address from "gest-edu".

- **Docker with Volume**: The application is containerized using Docker, with a volume for persistent storage.

- **Document Photos Storage**: The photos of the documents are stored in a specific directory in the file system. The path to this directory is set in the variable `UPLOAD_DIR`, in this case is set to /uploads.

- **API Documentation**: The application uses Swagger for API documentation. This provides a user-friendly interface for exploring and testing the API endpoints. You can access the Swagger UI by navigating to the `/swagger-ui.html` endpoint on your application's base URL (for example, `http://localhost:8080/swagger-ui.html`).

Please refer to the API documentation for more details on how to use the API endpoints.