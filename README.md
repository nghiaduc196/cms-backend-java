# CMS Backend Java - Spring Boot REST API

Backend cho dự án CMS bằng Java Spring Boot với REST API.

## 🚀 Công nghệ sử dụng

- **Java 17**
- **Spring Boot 4.0.2**
- **Spring Data JPA** - ORM và database access
- **MySQL** - Database
- **Lombok** - Giảm boilerplate code
- **Spring Validation** - Validate request data
- **SpringDoc OpenAPI (Swagger)** - API documentation
- **Maven** - Dependency management

## 📁 Cấu trúc Project

```
src/main/java/com/base/cms/
├── common/                    # Common utilities và base classes
│   ├── config/               # Configuration classes
│   │   ├── CorsConfig.java   # CORS configuration
│   │   └── OpenApiConfig.java # Swagger/OpenAPI configuration
│   ├── controller/           # Common controllers
│   │   └── HealthController.java
│   ├── dto/                  # Common DTOs
│   │   └── ApiResponse.java  # Standard API response wrapper
│   └── exception/            # Exception handling
│       ├── BadRequestException.java
│       ├── ResourceNotFoundException.java
│       └── GlobalExceptionHandler.java
├── example/                   # Example module (có thể xóa sau)
│   ├── controller/
│   │   └── UserController.java
│   ├── dto/
│   │   ├── UserRequest.java
│   │   └── UserResponse.java
│   ├── entity/
│   │   └── User.java
│   ├── repository/
│   │   └── UserRepository.java
│   └── service/
│       └── UserService.java
└── CmsApplication.java        # Main application class
```

## ⚙️ Cấu hình

### Database Configuration

Cập nhật thông tin database trong `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cms_db
spring.datasource.username=root
spring.datasource.password=your_password
```

### Port Configuration

Mặc định server chạy trên port `8080`. Có thể thay đổi trong `application.properties`:

```properties
server.port=8080
```

## 🏃 Chạy ứng dụng

### Yêu cầu
- Java 17 hoặc cao hơn
- Maven 3.6+
- MySQL 8.0+

### Các bước chạy

1. **Clone và vào thư mục project:**
```bash
cd cms-backend-java
```

2. **Cấu hình database:**
   - Tạo database MySQL: `cms_db`
   - Cập nhật username/password trong `application.properties`

3. **Build project:**
```bash
./mvnw clean install
```

4. **Chạy ứng dụng:**
```bash
./mvnw spring-boot:run
```

Hoặc chạy trực tiếp:
```bash
java -jar target/cms-0.0.1-SNAPSHOT.jar
```

5. **Kiểm tra ứng dụng:**
   - Health check: http://localhost:8080/api/health
   - API Base URL: http://localhost:8080/api
   - **Swagger UI**: http://localhost:8080/swagger-ui.html
   - **API Docs (JSON)**: http://localhost:8080/api-docs

## 📡 API Endpoints

### Health Check
- `GET /api/health` - Kiểm tra trạng thái ứng dụng

### User API (Example)
- `POST /api/users` - Tạo user mới
- `GET /api/users` - Lấy danh sách tất cả users
- `GET /api/users/{id}` - Lấy thông tin user theo ID
- `PUT /api/users/{id}` - Cập nhật user
- `DELETE /api/users/{id}` - Xóa user

### Example Request/Response

**Tạo User:**
```bash
POST /api/users
Content-Type: application/json

{
  "email": "user@example.com",
  "name": "John Doe"
}
```

**Response:**
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": 1,
    "email": "user@example.com",
    "name": "John Doe",
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T10:00:00"
}
```

## 📚 Swagger/OpenAPI Documentation

Project đã được tích hợp **SpringDoc OpenAPI (Swagger)** để tự động tạo API documentation.

### Truy cập Swagger UI

Sau khi chạy ứng dụng, truy cập:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

### Sử dụng Swagger Annotations

Để thêm documentation cho API mới, sử dụng các annotations:

**Trong Controller:**
```java
@Tag(name = "Product Management", description = "APIs for managing products")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @Operation(summary = "Create product", description = "Create a new product")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request) {
        // ...
    }
}
```

**Trong DTO:**
```java
@Schema(description = "Request DTO for creating a product")
public class ProductRequest {
    @Schema(description = "Product name", example = "Laptop", required = true)
    @NotBlank
    private String name;
}
```

### Cấu hình Swagger

Cấu hình Swagger được định nghĩa trong `OpenApiConfig.java`. Bạn có thể tùy chỉnh:
- API title và description
- Contact information
- Server URLs
- License information

## 🏗️ Tạo Module mới

Để tạo một module mới (ví dụ: Product), làm theo các bước:

1. **Tạo Entity:**
```java
@Entity
@Table(name = "products")
public class Product {
    // fields
}
```

2. **Tạo Repository:**
```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
```

3. **Tạo DTOs:**
```java
public class ProductRequest { }
public class ProductResponse { }
```

4. **Tạo Service:**
```java
@Service
public class ProductService {
    // business logic
}
```

5. **Tạo Controller:**
```java
@RestController
@RequestMapping("/api/products")
public class ProductController {
    // REST endpoints
}
```

## 🛠️ Best Practices

1. **Sử dụng `ApiResponse<T>`** để wrap tất cả API responses
2. **Sử dụng `@Valid`** để validate request DTOs
3. **Sử dụng custom exceptions** (`ResourceNotFoundException`, `BadRequestException`)
4. **Sử dụng `@Transactional`** cho các operations thay đổi database
5. **Sử dụng Lombok** để giảm boilerplate code

## 📝 Notes

- Module `example` chỉ để demo cấu trúc, có thể xóa sau khi hiểu rõ
- Tất cả exceptions sẽ được xử lý bởi `GlobalExceptionHandler`
- CORS đã được cấu hình cho localhost:3000 và localhost:8080

## 📚 Tài liệu tham khảo

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Lombok](https://projectlombok.org/)
