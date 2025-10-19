# Skill / Project Tracker — Spring Boot + PostgreSQL (MVP)

This project is a full-stack application that allows users to register, log in, and manage their skills and projects. It provides a dashboard summarizing user activity and includes a simple AI-based suggestion system that offers recommendations based on the data.

---

## Overview

The Skill / Project Tracker is built with Spring Boot (backend) and PostgreSQL (database), with a lightweight frontend written in HTML, CSS, and JavaScript. It follows a three-tier architecture: Presentation, Business, and Data layers.

---

## Features

- Authentication: User registration and login
- Skill Management: Add, list, and view skills with details such as level and notes
- Project Management: Add, list, and view projects with title, status, description, and tech stack
- Dashboard: Displays summary metrics for total skills and projects
- AI Suggestions: Provides contextual recommendations based on the user’s activity
- Simple User Interface: Clean, responsive, and easy-to-navigate HTML/CSS frontend

---

## Tech Stack

| Component   | Technology                  |
|------------|------------------------------|
| Backend    | Spring Boot 3 (Java 23)      |
| Database   | PostgreSQL 16                |
| ORM        | Hibernate / Spring Data JPA  |
| Build Tool | Maven                        |
| Frontend   | HTML, CSS, JavaScript        |
| Deployment | Localhost or Docker          |

---

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/<YOUR_USERNAME>/skill-project-tracker.git
cd skill-project-tracker
```

### 2. Start PostgreSQL using Docker

```bash
docker run -d --name skilltracker-db   -e POSTGRES_USER=skilluser   -e POSTGRES_PASSWORD=skillpass   -e POSTGRES_DB=skilltracker   -p 5432:5432   postgres:16
```

Verify the container is running:

```bash
docker ps
```

### 3. Configure Spring Boot

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/skilltracker
spring.datasource.username=skilluser
spring.datasource.password=skillpass
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 4. Run the Application

```bash
./mvnw spring-boot:run
```

Then open your browser and go to: http://localhost:8080

---

## Project Structure

```text
src/main/java/com/skilltracker/skill_project_tracker
├── config
│   ├── SecurityConfig.java
│   └── WebCorsConfig.java
├── controller
│   ├── AuthController.java
│   ├── HomeController.java
│   ├── ProjectController.java
│   └── SkillController.java
├── dto
│   ├── LoginRequest.java
│   ├── RegisterRequest.java
│   ├── SkillCreateRequest.java
│   ├── SkillResponse.java
│   ├── ProjectCreateRequest.java
│   └── ProjectResponse.java
├── model
│   ├── User.java
│   ├── Skill.java
│   └── Project.java
├── repo
│   ├── UserRepository.java
│   ├── SkillRepository.java
│   └── ProjectRepository.java
└── service
    ├── AuthService.java
    ├── SkillService.java
    ├── ProjectService.java
    └── SuggestionService.java
```

UI File: `src/main/resources/templates/app.html`

---

## API Endpoints

### Authentication

#### POST /api/auth/register
Registers a new user.

```json
{
  "username": "john",
  "email": "john@example.com",
  "password": "Pass1234"
}
```

#### POST /api/auth/login
Authenticates an existing user.

```json
{
  "username": "john",
  "password": "Pass1234"
}
```

### Skills

#### POST /api/skills
Adds a new skill.

```json
{
  "userId": 1,
  "name": "Java",
  "level": "Intermediate",
  "notes": "Learning Spring Boot"
}
```

#### GET /api/skills?userId=1
Fetches all skills for a user.

### Projects

#### POST /api/projects
Adds a new project.

```json
{
  "userId": 1,
  "title": "Skill Tracker MVP",
  "description": "Spring Boot + PostgreSQL",
  "techStack": "Java, Spring Boot, SQL",
  "status": "In Progress"
}
```

#### GET /api/projects?userId=1
Fetches all projects for a user.

### AI Suggestions

#### GET /api/suggestions?userId=1
Returns contextual tips based on user data.

```json
[
  {
    "type": "resource",
    "title": "Practice: 3 quick exercises in SQL",
    "reason": "Short reps will move you toward Intermediate."
  }
]
```

---

## Example cURL Commands

```bash
# Register
curl -i -X POST http://localhost:8080/api/auth/register   -H "Content-Type: application/json"   -d '{"username":"demo","password":"Pass1234","email":"demo@example.com"}'

# Login
curl -i -X POST http://localhost:8080/api/auth/login   -H "Content-Type: application/json"   -d '{"username":"demo","password":"Pass1234"}'

# Add Skill
curl -i -X POST http://localhost:8080/api/skills   -H "Content-Type: application/json"   -d '{"userId":1,"name":"Spring Boot","level":"Intermediate"}'

# List Skills
curl -i "http://localhost:8080/api/skills?userId=1"

# Get Suggestions
curl -i "http://localhost:8080/api/suggestions?userId=1"
```

---

## Security Notes

- `/api/auth/register` and `/api/auth/login` are publicly accessible.
- Other endpoints require valid user context (`userId` passed in the payload for now).
- In production, this should be replaced by JWT-based authentication or session management.

---

## Roadmap

- Replace manual userId passing with authenticated sessions or JWT tokens.
- Add edit and delete options for skills and projects.
- Enhance AI suggestions using external APIs such as OpenAI.
- Add pagination, filters, and search capabilities.
- Improve backend validation and exception handling.
- Write automated unit and integration tests.

---

## License

This project is distributed under the MIT License.
