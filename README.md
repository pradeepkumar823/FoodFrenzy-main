# 🍱 FoodFrenzy - Professional Food Management System

FoodFrenzy is a world-class digital brand and food management platform built with a robust Spring Boot backend. It provides a seamless, high-performance experience for both gourmet customers and administrative staff.

---

## 🚀 Backend Architecture & Technical Stack

The core of FoodFrenzy is engineered for scalability, security, and type-safety.

### 🛠️ Core Technology Stack

- **Backend Framework**: Spring Boot 3.x
- **Language**: Java 17+
- **Persistence Layer**: Spring Data JPA (Hibernate)
- **Database**: MySQL 8.x
- **Security**: Spring Security 6.x + OAuth2
- **Templating Engine**: Thymeleaf (Server-Side Rendering)
- **Tooling**: Maven, JUnit 5

### 🏗️ Architectural Patterns

- **MVC (Model-View-Controller)**: Strict separation of concerns between business logic, data persistence, and UI rendering.
- **Service Layer Pattern**: Encapsulates complex business workflows (e.g., Order Processing, Email Verification).
- **DTO Pattern (Data Transfer Object)**: Decouples the internal database schema from the API layer, ensuring secure data exchange and preventing accidental exposure of sensitive fields.
- **Repository Pattern**: Utilized Spring Data JPA for abstraction over database operations.

---

## 🔐 Security & User Management

FoodFrenzy implements a "Security-First" approach to protect user data and administrative actions.

### 🛡️ Authentication Flow

- **Direct Login**: Secure credential-based login with hashed password storage.
- **OAuth2 Integration**: Full support for "Sign in with Google," providing a frictionless experience for modern users.
- **Email Verification**: A robust verification system triggers upon registration to ensure account authenticity.

### 🚦 Role-Based Access Control (RBAC)

- **USER Role**: Access to menu browsing, profile management, and order history.
- **ADMIN Role**: Exclusive access to the Global Management Dashboard, inventory control, and system-wide order auditing.
- **Session Protection**: Explicit role-based session validation prevents cross-account unauthorized access (e.g., stopping a standard user from accessing Admin controllers).

---

## 📊 Database Design & Entity Relationships

The relational schema is optimized for data integrity and efficient retrieval.

- **User & Admin Entities**: Distinct tables for different personas, linked via unique email identifiers.
- **Product Entity**: Stores menu items with categorizations (Biryani, Chinese, etc.).
- **Order System**:
  - **Orders Entity**: Persistent storage of transaction details, timestamps, and customer relations.
  - **One-to-Many Relationship**: Users are mapped to multiple orders, enabling a rich "Order History" feature.
- **Externalized Configuration**: Database credentials are managed via environment variables (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`), ensuring the codebase is production-ready and secure.

---

## 🎨 Professional Brand UI/UX

While the backend drives the logic, the frontend utilizes modern design principles:

- **Masonry Gallery**: A high-density grid showcasing 30+ food items with staggered CSS columns.
- **Glassmorphism**: Sleek overlay effects for premium readability.
- **Scroll Reveal**: Cinematic entry animations for all major landing page sections.
- **Fluid Typography**: Responsive scaling across all devices using CSS `clamp()`.

---

## 🛠️ Setup & Local Deployment

### 1. Database Initialization

Create a MySQL database named `foodfrenzy_db`.

### 2. Environment Configuration

Configure your credentials in a `.env` file or export them:

```bash
export DB_URL=jdbc:mysql://localhost:3306/foodfrenzy_db
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
```

### 3. Build & Run

```bash
./mvnw clean install
./mvnw spring-boot:run
```

Access the dashboard at `http://localhost:8080`

---

## 📂 Project Structure

```text
src/main/java/com/example/demo/
├── config/       # Security, OAuth2, and Bean configurations
├── controllers/  # REST & MVC Controllers (Admin, User, Registration)
├── dto/          # Data Transfer Objects for secure communication
├── entities/     # JPA Hibernate Database Models
├── repository/   # Spring Data JPA interfaces
└── services/     # Business logic and Core Services
```
