# Statement Generator API

## Description
The **Statement Generator API** is a Spring Boot-based service that generates account statements asynchronously by integrating with a mocked Core Banking system. This design enhances user experience and system reliability by allowing users to request their account statements without blocking the main thread. Once the statement is generated, users will be notified with a download link.

## Features
- **Asynchronous Statement Generation:** Utilizes `CompletableFuture` for non-blocking operations.
- **Mocked Core Banking Integration:** Simulates fetching statement data from a banking system.
- **In-Memory H2 Database:** Used for data persistence.
- **Notification System:** Simulates customer notifications upon statement completion.

## Technologies Used
- **Spring Boot:** For building the RESTful API.
- **JPA (Java Persistence API):** For data persistence using an in-memory H2 database.
- **JUnit:** For unit testing.

## API Endpoints

### 1. Request Statement
- **Endpoint:** `/api/v1/statements`
- **Method:** `POST`
- **Request Body:**
    ```json
    {
        "id": 1,  // Long account ID
        "status": "NEW"  // Initial status of the statement
    }
    ```
- **Responses:**
  - **202 Accepted**
    - Body: `Request accepted with ID: <requestId>`
  - **500 Internal Server Error**
    - Body: `Error: <errorMessage>`

### 2. Download Statement
- **Endpoint:** `/api/v1/statements/download/{id}`
- **Method:** `GET`
- **Path Variable:**
    - `id`: The ID of the statement to download.
- **Responses:**
  - **200 OK**
    - Body: A PDF file of the statement.

## Setup Instructions

To run the Statement Generator API locally, follow these steps:

1. **Clone the Repository:**
    ```bash
    git clone https://github.com/yourusername/statement-generator-api.git
    cd statement-generator-api
    ```

2. **Build the Project:**  
   Ensure you have Maven installed. Run the following command to build the project:
    ```bash
    ./mvnw clean package
    ```

3. **Run the Application:**  
   Use the following command to start the Spring Boot application:
    ```bash
    ./mvnw spring-boot:run
    ```

4. **Access the API:**  
   The API will be available at `http://localhost:8080`. You can use tools like Postman or cURL to interact with the endpoints.

## Testing Instructions

The project includes unit tests for both the service and controller layers. To run the tests, execute the following command:
```bash
./mvnw test
