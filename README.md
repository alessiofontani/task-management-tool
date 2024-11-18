# Task Management Tool

## Overview
**Task Management Tool** is a web application built using **Vue 3** with **Vite** as a build tool for the frontend and **Spring Boot** for the backend. The application is containerized using **Docker** and orchestrated with **Docker Compose** for easy deployment and development.

The main goals of this project are to showcase my skills in full-stack development and have the opportunity to practice test-driven development, Typescript, Docker and Github Actions.

Every feedback is very appreciated.

## Technologies Used
- **Frontend**: Vue 3, Vite, TypeScript
- **Backend**: Spring Boot, Java 21
- **Database**: Sqlite
- **Containerization**: Docker, Docker Compose

## Project Structure
```
TaskManagementTool/
├── backend/                # Spring Boot application
│   ├── src/                # Source code for the backend
│   ├── pom.xml             # Maven configuration file
│   └── Dockerfile          # Dockerfile for backend application
└── frontend/               # Vue.js application
│   ├── src/                # Source code for the frontend
│   ├── package.json        # NPM dependencies and scripts
│   └── Dockerfile          # Dockerfile for frontend application
├── docker-compose.yml      # Docker Compose configuration file
└── README.md               # Project documentation
```

## Getting Started

### Prerequisites
- [Docker](https://www.docker.com/get-started) installed
- [Docker Compose](https://docs.docker.com/compose/install/) installed

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/alessiofontani/task-management-tool.git
   cd TaskManagementTool

2. Build and run the application using Docker Compose:
    ```bash
    docker-compose up --build

3. Access the application:
   - **Frontend**: Open your browser and navigate to [http://localhost:5005](http://localhost:3001).
   - **Backend**: Access the backend at [http://localhost:8083](http://localhost:8083).

## Security

Security in **Task Management Tool** is handled using Spring Security.

The app currently supports **Basic Authentication** for the initial login, which validates username and password and issues a **JWT** for session management in case of successful login.

### JWT

The JWT is signed using **HMAC-SHA** secret key for authentication and integrity verification.

HMAC-SHA256, HMAC-SHA384 or HMAC-SHA512 will be chosen depending on the length of the jwt.secret.key in the application.properties file, since the library (io.jsonwebtoken) selects the appropriate algorithm based on the key size.

The claims in the JWT consist of the username, roles, email, issued at, and expiration values.

The JWT is set to expire 24 hours after it is issued.

### Authentication

Authentication is JWT based.

The first login is handled through **Basic Authentication**. If the login is successful a JWT is issued to the user. The JWT will be used for next interactions with the application.

Once issued, the JWT must be included in the **Authorization header** for every request except for those under /api/auth/** path, which handles registration and login.

Requests made without the JWT will be rejected with a **401 Unauthorized** response.

User passwords are securely stored using **BCrypt** hashing algorithm.

### CORS

CORS (Cross-Origin Resource Sharing) rules are defined in the application.properties

```bash
cors.allowed-origins= ${CORS_ALLOWED_ORIGINS} 
cors.allowed-methods= ${CORS_ALLOWED_METHODS} 
cors.allowed-headers= ${CORS_ALLOWED_HEADERS}
```

The values are defined into the docker-compose.yml. This configuration currently allow only the frontend to communicate with the backend.

