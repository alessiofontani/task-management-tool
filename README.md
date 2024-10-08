# Task Management Tool

## Overview
**Task Management Tool** is a web application built using **Vue 3** with **Vite** as a build tool for the frontend and **Spring Boot** for the backend. The application is containerized using **Docker** and orchestrated with **Docker Compose** for easy deployment and development.

The main goal of this project is to showcase my skills.

## Features
- **Frontend**: Developed with Vue 3 and Vite.
- **Backend**: Built using Spring Boot.
- **Docker**: Containerized for easy deployment and scalability.
- **Docker Compose**: Simplifies running multiple services.

## Technologies Used
- **Frontend**: Vue 3, Vite, TypeScript
- **Backend**: Spring Boot, Java 21
- **Database**: ...
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
   - **Frontend**: Open your browser and navigate to [http://localhost:3001](http://localhost:3001).
   - **Backend**: Access the backend at [http://localhost:8083](http://localhost:8083).