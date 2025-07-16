# Freelance Marketplace

A full-stack web application for connecting clients and freelancers. This project features a Spring Boot backend and an Angular frontend, providing a platform for job posting, user management, and profile handling.

## Features
- User authentication and role-based access (Admin, Client, Freelancer)
- Job posting and management
- Freelancer and client profile management
- Admin dashboard for user and job oversight
- Responsive Angular frontend

## Tech Stack
- **Backend:** Java, Spring Boot, Spring Security, JWT, Gradle
- **Frontend:** Angular, TypeScript, SCSS
- **Database:** (Configure in `application.properties`)

## Directory Structure
```
Backend/                  # Spring Boot backend
  └── src/main/java/com/freelance/marketplace/
frontend/                 # Angular frontend
  └── src/app/
```

## Getting Started

### Prerequisites
- Java 17+
- Node.js 16+
- npm 8+
- Angular CLI (recommended)

### Backend Setup
1. Navigate to the backend directory:
   ```sh
   cd Backend
   ```
2. Build and run the Spring Boot application:
   ```sh
   ./gradlew bootRun
   ```
   Or on Windows:
   ```sh
   gradlew.bat bootRun
   ```
3. Configure your database in `src/main/resources/application.properties` as needed.

### Frontend Setup
1. Navigate to the frontend directory:
   ```sh
   cd frontend
   ```
2. Install dependencies:
   ```sh
   npm install
   ```
3. Start the Angular development server:
   ```sh
   ng serve
   ```
4. The app will be available at `http://localhost:4200`.

### API Proxy
The Angular app uses `proxy.conf.json` to forward API requests to the backend during development.

## Usage
- Register as a client or freelancer
- Clients can post jobs and manage their postings
- Freelancers can create profiles and apply for jobs
- Admins can manage users and job posts

## Contributing
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Open a pull request

## License
This project is licensed under the MIT License. 