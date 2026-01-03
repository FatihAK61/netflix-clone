# 🎬 Netflix Clone - Full Stack Application

A complete Netflix clone built from scratch using **Spring Boot**, **Angular**, and **PostgreSQL**. This project
demonstrates modern full-stack development practices with a focus on scalable architecture and clean code.

## 🚀 Features

### Core Functionality

- 🔐 User authentication and authorization
- 🎥 Browse movies and TV shows
- 🔍 Search and filter content
- 📺 Video streaming capability
- ⭐ Rating and review system
- 📱 Responsive design for all devices
- 👤 User profile management
- 📊 Admin dashboard for content management

### Technical Features

- RESTful API architecture
- JWT-based authentication
- PostgreSQL database with optimized queries
- File upload and storage for media content
- Error handling and validation
- CORS configuration
- Pagination and sorting

## 🛠️ Tech Stack

### Backend

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Database operations
- **PostgreSQL** - Primary database
- **Maven** - Dependency management
- **Lombok** - Reduce boilerplate code
- **JWT** - Token-based authentication

### Frontend

- **Angular 17+**
- **TypeScript**
- **RxJS** - Reactive programming
- **Angular Material** - UI components
- **Angular Router** - Navigation
- **HttpClient** - API communication

## 📋 Prerequisites

Before running this project, make sure you have:

- Java JDK 17 or higher
- Node.js 18+ and npm
- PostgreSQL 15.0+
- Maven 3.6+
- Angular CLI
- Git

## 🔧 Installation & Setup

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/netflix-clone.git
cd netflix-clone
```

### 2. Backend Setup

#### Configure Database

```bash
# Create PostgreSQL database
CREATE DATABASE netflix_clone;
```

#### Update application.properties

```properties
# src/main/resources/application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/netflix
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

#### Run Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend server will start at `http://localhost:6161`

### 3. Frontend Setup

```bash
cd frontend
npm install
ng serve
```

The frontend application will start at `http://localhost:4200`

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👨‍💻 Author

**Fatih AK**

- GitHub: [@FatihAK61](https://github.com/FatihAK61)
- LinkedIn: [Fatih AK](https://www.linkedin.com/in/fatihak)

## 🙏 Acknowledgments

- Inspired by Netflix's design and functionality
- Built as a learning project for full-stack development
- Thanks to the Spring Boot and Angular communities

## 📞 Support

For support, email fatihak61@gmail.com or create an issue in the repository.

---

⭐ If you found this project helpful, please give it a star!
