# CMS Microservices - Spring Boot Architecture

Dự án CMS được xây dựng theo kiến trúc **Microservices** sử dụng Spring Boot và Spring Cloud.

## 🏗️ Kiến trúc Microservices

```
┌─────────────────┐
│   API Gateway   │ (Port 8080)
│  (Spring Cloud  │
│     Gateway)    │
└────────┬────────┘
         │
         ├─────────────────┐
         │                 │
┌────────▼────────┐  ┌─────▼──────────┐
│ Service Discovery│  │  User Service   │
│  (Eureka Server)│  │  (Port 8081)   │
│   (Port 8761)    │  └────────────────┘
└──────────────────┘
         │
         │
┌────────▼────────┐
│  MySQL Database │
│   (Port 3306)   │
└─────────────────┘
```

## 📦 Cấu trúc Project

```
cms-backend-java/
├── common-lib/              # Shared libraries
│   ├── src/main/java/com/base/cms/common/
│   │   ├── dto/             # Common DTOs (ApiResponse)
│   │   ├── entities/        # All entities (BaseAuditEntity, User, ...)
│   │   └── exception/        # Common exceptions & handlers
│   └── pom.xml
│
├── service-discovery/       # Eureka Server
│   ├── src/main/java/com/base/cms/discovery/
│   │   └── ServiceDiscoveryApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
├── api-gateway/             # Spring Cloud Gateway
│   ├── src/main/java/com/base/cms/gateway/
│   │   └── ApiGatewayApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
├── user-service/            # User Management Service
│   ├── src/main/java/com/base/cms/user/
│   │   ├── controller/      # REST Controllers
│   │   ├── service/         # Business Logic
│   │   ├── repository/       # Data Access
│   │   └── dto/             # Data Transfer Objects
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
├── pom.xml                   # Parent POM
├── docker-compose.yml        # Docker Compose configuration
└── README.md
```

## 🚀 Công nghệ sử dụng

- **Java 17**
- **Spring Boot 4.0.2**
- **Spring Cloud 2024.0.0**
- **Spring Cloud Gateway** - API Gateway
- **Netflix Eureka** - Service Discovery
- **Spring Data JPA** - ORM và database access
- **MySQL 8.0** - Database
- **Lombok** - Giảm boilerplate code
- **SpringDoc OpenAPI (Swagger)** - API documentation
- **Docker & Docker Compose** - Containerization
- **Maven** - Dependency management

## ⚙️ Cấu hình Services

### Service Discovery (Eureka)
- **Port**: 8761
- **URL**: http://localhost:8761
- **Chức năng**: Quản lý và phát hiện các microservices

### API Gateway
- **Port**: 8080
- **URL**: http://localhost:8080
- **Chức năng**: 
  - Routing requests đến các services
  - Load balancing
  - CORS handling
  - API aggregation

### User Service
- **Port**: 8081
- **Database**: `user_db`
- **Chức năng**: Quản lý users (CRUD operations)

## 🏃 Chạy ứng dụng

### Yêu cầu
- Java 17 hoặc cao hơn
- Maven 3.6+
- MySQL 8.0+ (hoặc sử dụng Docker)
- Docker & Docker Compose (nếu chạy bằng Docker)

### Cách 1: Chạy bằng Docker Compose (Khuyến nghị)

1. **Build và chạy tất cả services:**
```bash
docker-compose up --build
```

2. **Chạy ở background:**
```bash
docker-compose up -d
```

3. **Xem logs:**
```bash
docker-compose logs -f
```

4. **Dừng tất cả services:**
```bash
docker-compose down
```

### Cách 2: Chạy thủ công từng service

**Bước 1: Khởi động MySQL**
```bash
# Sử dụng Docker
docker run -d \
  --name cms-mysql \
  -e MYSQL_ROOT_PASSWORD=123456 \
  -e MYSQL_DATABASE=user_db \
  -p 3306:3306 \
  mysql:8.0
```

**Bước 2: Khởi động Service Discovery**
```bash
cd service-discovery
../mvnw spring-boot:run
```

**Bước 3: Khởi động User Service**
```bash
cd user-service
../mvnw spring-boot:run
```

**Bước 4: Khởi động API Gateway**
```bash
cd api-gateway
../mvnw spring-boot:run
```

### Thứ tự khởi động (quan trọng)
1. MySQL Database
2. Service Discovery (Eureka)
3. User Service
4. API Gateway

## 📡 API Endpoints

### Qua API Gateway (Port 8080)

- **User Service APIs:**
  - `GET http://localhost:8080/api/users` - Lấy danh sách users
  - `GET http://localhost:8080/api/users/{id}` - Lấy user theo ID
  - `POST http://localhost:8080/api/users` - Tạo user mới
  - `PUT http://localhost:8080/api/users/{id}` - Cập nhật user
  - `DELETE http://localhost:8080/api/users/{id}` - Xóa user

### Trực tiếp từ User Service (Port 8081)

- `GET http://localhost:8081/api/users` - Lấy danh sách users
- `GET http://localhost:8081/api/users/{id}` - Lấy user theo ID
- `POST http://localhost:8081/api/users` - Tạo user mới
- `PUT http://localhost:8081/api/users/{id}` - Cập nhật user
- `DELETE http://localhost:8081/api/users/{id}` - Xóa user

### Service Discovery Dashboard

- **Eureka Dashboard**: http://localhost:8761
  - Xem danh sách các services đã đăng ký
  - Kiểm tra trạng thái health của services

### Swagger Documentation

- **User Service Swagger**: http://localhost:8081/swagger-ui.html
- **API Gateway Swagger**: http://localhost:8080/swagger-ui.html (nếu có)

## 🔧 Cấu hình Database

Cập nhật thông tin database trong `user-service/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/user_db
spring.datasource.username=root
spring.datasource.password=123456
```

## 📦 Entities (Common Entities)

Tất cả các **Entities** được đặt trong `common-lib` để các service có thể chia sẻ và tái sử dụng. Dự án bao gồm:

### Base Entity (Abstract Class)

### BaseAuditEntity
Entity đơn giản chỉ với audit cơ bản:
- `id`: Primary key (Long)
- `createdAt`: Thời gian tạo (LocalDateTime)
- `updatedAt`: Thời gian cập nhật (LocalDateTime)

**Sử dụng khi:**
- Chỉ cần audit cơ bản (createdAt, updatedAt)
- Không cần createdBy, updatedBy, isDeleted

**Ví dụ:**
```java
@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class User extends BaseAuditEntity {
    private String email;
    private String name;
}
```

### Business Entities

Các entities cụ thể như `User`, `Product`, `Order`, ... cũng được đặt trong `common-lib/src/main/java/com/base/cms/common/entities/`:

**Ví dụ: User Entity**
```java
package com.base.cms.common.entities;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class User extends BaseAuditEntity {
    private String email;
    private String name;
}
```

### Lưu ý khi sử dụng Entities:

1. **Tất cả entities đều nằm trong `common-lib`:**
   - Base entity: `BaseAuditEntity`
   - Business entities: `User`, `Product`, `Order`, ...

2. **Import từ common-lib:**
```java
import com.base.cms.common.entities.User;
import com.base.cms.common.entities.BaseAuditEntity;
```

3. **Sử dụng Lombok annotations:**
- `@EqualsAndHashCode(callSuper = true)`: Để include các trường từ base class
- `@SuperBuilder`: Để hỗ trợ builder pattern với inheritance

4. **Database columns:**
Các trường từ base entity sẽ tự động được map vào database:
- `id` → `id` (BIGINT, AUTO_INCREMENT)
- `createdAt` → `created_at` (DATETIME)
- `updatedAt` → `updated_at` (DATETIME)

5. **Repository và Service:**
- Repository và Service vẫn nằm trong từng service riêng
- Chỉ entities được chia sẻ trong `common-lib`

## 🏗️ Thêm Service mới

Để thêm một microservice mới (ví dụ: Product Service):

1. **Tạo module mới trong parent POM:**
```xml
<modules>
    ...
    <module>product-service</module>
</modules>
```

2. **Tạo cấu trúc service:**
```bash
mkdir -p product-service/src/main/java/com/base/cms/product
mkdir -p product-service/src/main/resources
```

3. **Tạo pom.xml cho service:**
```xml
<parent>
    <groupId>com.base</groupId>
    <artifactId>cms-microservices</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</parent>
<artifactId>product-service</artifactId>
```

4. **Thêm dependency vào service:**
```xml
<dependency>
    <groupId>com.base</groupId>
    <artifactId>common-lib</artifactId>
</dependency>
```

5. **Tạo Entity trong common-lib:**
```java
// File: common-lib/src/main/java/com/base/cms/common/entities/Product.java
package com.base.cms.common.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "products")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Product extends BaseAuditEntity {
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private BigDecimal price;
}
```

**Lưu ý:** Tất cả entities phải được tạo trong `common-lib`, không tạo trong các service riêng.

6. **Cấu hình trong API Gateway:**
```properties
spring.cloud.gateway.routes[1].id=product-service
spring.cloud.gateway.routes[1].uri=lb://product-service
spring.cloud.gateway.routes[1].predicates[0]=Path=/api/products/**
```

7. **Thêm vào docker-compose.yml:**
```yaml
product-service:
  build:
    context: .
    dockerfile: product-service/Dockerfile
  ...
```

## 🐳 Docker Commands

```bash
# Build tất cả images
docker-compose build

# Chạy tất cả services
docker-compose up

# Chạy ở background
docker-compose up -d

# Xem logs
docker-compose logs -f [service-name]

# Dừng services
docker-compose stop

# Xóa containers và volumes
docker-compose down -v

# Rebuild và restart
docker-compose up --build -d
```

## 🧪 Testing

### Test User Service qua API Gateway

```bash
# Tạo user mới
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "name": "John Doe"
  }'

# Lấy danh sách users
curl http://localhost:8080/api/users

# Lấy user theo ID
curl http://localhost:8080/api/users/1
```

## 📝 Best Practices

1. **Service Independence**: Mỗi service có database riêng
2. **API Gateway**: Tất cả requests đi qua API Gateway
3. **Service Discovery**: Tự động phát hiện và load balancing
4. **Common Library**: Shared code trong `common-lib`
5. **Docker**: Containerization cho dễ deploy
6. **Configuration**: Mỗi service có `application.properties` riêng

## 🔍 Monitoring & Health Checks

- **Eureka Dashboard**: http://localhost:8761
- **Service Health**: Mỗi service có health check endpoint
- **Docker Health Checks**: Được cấu hình trong docker-compose.yml

## 🚨 Troubleshooting

### Service không đăng ký với Eureka
- Kiểm tra Eureka đã chạy chưa
- Kiểm tra `eureka.client.service-url.defaultZone` trong application.properties
- Kiểm tra network connectivity

### API Gateway không route được
- Kiểm tra service đã đăng ký với Eureka chưa
- Kiểm tra route configuration trong API Gateway
- Kiểm tra service name phải match với Eureka service name

### Database connection issues
- Kiểm tra MySQL đã chạy chưa
- Kiểm tra database credentials
- Kiểm tra network trong Docker (nếu dùng Docker)

## 📚 Tài liệu tham khảo

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [Netflix Eureka](https://github.com/Netflix/eureka)
- [Docker Compose](https://docs.docker.com/compose/)

## 📄 License

This project is licensed under the Apache 2.0 License.
