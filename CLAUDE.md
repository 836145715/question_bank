# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Quick Start

This is a **Spring Boot 3.2.0** question bank management application using Java 17, MyBatis Plus, and MySQL.

### Essential Setup Steps

1. **Database Setup**
   - Import `src/main/resources/init.sql` into MySQL (creates `question_db` database with seed data)
   - Default test users (password: `12345678`):
     - Admin: `admin`
     - Regular users: `user1`, `user2`, `user3`, `user4`

2. **Configuration**
   - Main config: `src/main/resources/application.yml`
   - Dev config: `src/main/resources/application-dev.yml`
   - Update database connection details in dev config if needed

3. **Build & Run**
   ```bash
   # Build project
   ./mvnw clean package -DskipTests

   # Run dev profile
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

   # Or run JAR directly
   java -jar target/springboot-init-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
   ```

### Development Commands

```bash
# Run tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=UserServiceTest

# Run with Hot Reload
./mvnw spring-boot:run

# Clean build
./mvnw clean

# Skip tests during build
./mvnw clean package -DskipTests
```

## Architecture Overview

### Project Structure

```
src/main/java/com/hu/wink/
├── config/          # Configuration classes (MyBatis Plus, COS, WeChat, OpenAPI, etc.)
├── controller/      # REST controllers (UserController, QuestionBankController, etc.)
├── service/         # Business logic layer (UserService, QuestionBankService, etc.)
├── mapper/          # MyBatis Plus data access layer
├── model/           # DTOs, VOs, and Entities
│   ├── dto/         # Request DTOs (UserAddRequest, UserLoginRequest, etc.)
│   ├── entity/      # Database entities (User, QuestionBank, Question)
│   ├── vo/          # Response View Objects (UserVO, QuestionBankVO)
│   └── enums/       # Enumerations (UserRoleEnum, ReviewStatusEnum)
├── aop/             # AOP interceptors (AuthInterceptor, LogInterceptor)
├── exception/       # Exception handling (BusinessException, GlobalExceptionHandler)
├── common/          # Shared utilities (ResultUtils, BaseResponse, ErrorCode)
├── annotation/      # Custom annotations (@AuthCheck)
├── constant/        # Constants (UserConstant, FileConstant)
├── utils/           # Utility classes (SqlUtils, NetUtils, SpringContextUtils)
└── MainApplication.java  # Spring Boot entry point
```

### Technology Stack

- **Core**: Spring Boot 3.2.0, Java 17, Maven
- **Database**: MyBatis Plus 3.5.5, MySQL 8.0
- **Cache/Session**: Redis (optional, currently disabled)
- **Search**: Elasticsearch (optional, currently disabled)
- **API Docs**: Knife4j (Swagger 3)
- **Storage**: Tencent COS for file uploads
- **Excel**: EasyExcel for import/export
- **WeChat**: wx-java-mp for mini-program integration
- **Template**: FreeMarker
- **Security**: Hibernate Validator, Custom AOP authentication

### Key Patterns & Conventions

**1. Response Format**
- All APIs return `BaseResponse<T>` with structure: `{"code": 0, "data": ..., "message": "ok"}`
- Success: `ResultUtils.success(data)`
- Error: `ResultUtils.error(ErrorCode.ERROR_CODE)`

**2. Layered Architecture**
```
Controller → Service → Mapper → Database
```
- Controllers handle HTTP, validation, and auth
- Services contain business logic
- Mappers use MyBatis Plus for CRUD

**3. Authentication**
- Use `@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)` for admin-only endpoints
- `AuthInterceptor` validates permissions via AOP
- User role enum: `user`, `admin`, `ban`

**4. Data Model Conventions**
- **Entities**: Database models (User, QuestionBank, Question)
- **DTOs**: Request objects (UserAddRequest, UserLoginRequest)
- **VOs**: Response objects with sensitive data filtered (UserVO, QuestionVO)
- **Enums**: Type-safe constants (UserRoleEnum, ReviewStatusEnum)

**5. Exception Handling**
- `GlobalExceptionHandler` catches all exceptions and returns standardized errors
- Custom `BusinessException` for business logic errors
- Uses `@Valid` for request validation (Jakarta Bean Validation)

**6. Database Patterns**
- Logical deletion with `@TableLogic` (isDelete field)
- Pagination via MyBatis Plus `Page<T>`
- Global config in `MyBatisPlusConfig` (pagination interceptor enabled)

### Core Entities

1. **User**
   - Fields: id, userAccount, userPassword, userRole, unionId, mpOpenId, vipExpireTime, etc.
   - Has VIP system and WeChat integration

2. **QuestionBank**
   - Contains questions with review workflow
   - Fields: title, description, picture, reviewStatus, priority, viewNum

3. **Question**
   - Questions with tags and statistics
   - Fields: title, content, tags (JSON), answer, viewNum, thumbNum, favourNum, needVip

4. **QuestionBankQuestion**
   - Junction table (hard delete)
   - Fields: questionBankId, questionId, questionOrder

### API Endpoints

**Base URL**: `http://localhost:8101`

- **User APIs**: `/user/*`
  - Register: `POST /user/register`
  - Login: `POST /user/login`
  - WeChat Login: `GET /user/login/wx_open`
  - Admin CRUD: `/user/add`, `/user/delete`, `/user/update`, `/user/list/page`

- **Question Bank APIs**: `/questionBank/*`
  - CRUD operations with admin privileges
  - Public queries for approved content

- **Question APIs**: `/question/*`
  - CRUD operations with admin privileges
  - Public queries for approved content

- **File Upload**: `/file/*`
  - Uses COS (Tencent Cloud Object Storage)

- **API Documentation**: `http://localhost:8101/doc.html` (Knife4j/Swagger UI)

### Configuration Highlights

**Spring Profiles**:
- `dev`: Default development profile
- `prod`: Production configuration

**Key Settings** (`application.yml`):
- Server port: `8101`
- Session timeout: 30 days
- File upload max: 10MB
- MyBatis Plus: SQL logging enabled, logic delete configured
- Knife4j docs enabled at `/doc.html`

**Todo Items** (search for `// todo` comments):
- Enable Redis: Remove from `exclude` in `MainApplication` and update config
- Enable Elasticsearch: Update `application.yml` config
- Update WeChat/COS credentials in production

### Common Development Tasks

**Adding a New Entity**:
1. Create entity in `model/entity/`
2. Create mapper interface in `mapper/`
3. Create service interface in `service/`
4. Implement service in `service/impl/`
5. Create controller in `controller/`
6. Add XML mapper file in `src/main/resources/mapper/`
7. Run database migration for new table

**Adding Validation**:
- Add validation annotations to DTOs (`@Valid`, `@Min`, etc.)
- `GlobalExceptionHandler` already handles validation errors

**Modifying Auth**:
- Check `AuthInterceptor` for permission logic
- Update `UserRoleEnum` for new roles
- Use `@AuthCheck` annotation on endpoints

### Testing

Test files in `src/test/java/`:
- `MainApplicationTests`: Spring context test
- `UserServiceTest`: User service unit tests
- `EasyExcelTest`: Excel utility tests
- `CosManagerTest`: COS integration tests

Run specific tests:
```bash
./mvnw test -Dtest=UserServiceTest
./mvnw test -Dtest=*Test
```

### Dependencies (from pom.xml)

Key libraries with versions:
- Spring Boot: 3.2.0
- MyBatis Plus: 3.5.5
- MySQL Connector: 8.x
- Knife4j: 4.4.0
- EasyExcel: 3.3.4
- Hutool: 5.8.26
- wx-java-mp: 4.6.0

See `pom.xml:19-124` for complete dependency list.

### Important Notes

1. **WeChat Integration**: Currently uses Memory storage type, needs proper Redis for production
2. **Redis**: Disabled by default, need to configure and enable in `MainApplication`
3. **Elasticsearch**: Optional dependency, needs configuration if used
4. **SQL Logging**: Enabled in dev profile (see `application.yml:52`)
5. **Port**: Default is 8101
6. **Session**: Cookie-based, 30-day expiry
7. **Password Encryption**: MD5 with SALT (see `UserServiceImpl`)
8. **Review System**: Questions/Banks have review workflow (0-pending, 1-approved, 2-rejected)

### Files of Interest

- `src/main/resources/init.sql`: Database schema and seed data
- `src/main/java/com/hu/wink/MainApplication.java`: Entry point, note Redis exclusion
- `src/main/java/com/hu/wink/config/MyBatisPlusConfig.java`: DB pagination setup
- `src/main/java/com/hu/wink/config/OpenApiConfig.java`: Knife4j/OpenAPI configuration
- `src/main/java/com/hu/wink/exception/GlobalExceptionHandler.java`: Centralized error handling
- `src/main/java/com/hu/wink/aop/AuthInterceptor.java`: Permission validation logic
- `src/main/java/com/hu/wink/controller/UserController.java`: Complete example of REST patterns
- `src/main/java/com/hu/wink/service/UserService.java`: Service interface patterns
- `src/main/java/com/hu/wink/common/ResultUtils.java`: Standardized API responses
