# Gestor de Tareas con Autenticación JWT

Este proyecto es una **API REST** desarrollada con **Spring Boot**, que permite gestionar tareas asociadas a usuarios autenticados mediante **JSON Web Tokens (JWT)**.  
Incluye endpoints para **registro, login, refresco de token** y un sistema de **CRUD de tareas** protegido por roles.

---

## 🚀 Tecnologías utilizadas

- **Java 17+**  
- **Spring Boot 3+**  
- **Spring Security (JWT)**  
- **JPA / Hibernate**  
- **Validation API (Jakarta Validation)**  
- **Maven**  
- **Base de datos (MySQL)**  

---

## 📁 Estructura principal del proyecto

```
src/main/java/jsz/myapp/todoapp/
│
├── config/
│   ├── DataInitializer.java/
│   └── SwaggerConfig.java
├── controller/
│   ├── dto/
│   │   └── TaskDTO.java
│   ├── AuthController.java
│   └── TaskController.java
│
├── dto/
│   ├── AuthRequest.java
│   ├── AuthResponse.java
│   └── RegisterRequest.java 
│
├── model/
│   ├── UserEntity.java
│   └── TaskEntity.java
│
├── repository/
│   ├── UserRepository.java
│   └── TaskRepository.java
│  
├── service/
│   ├── AuthService.java
│   └── TaskService.java
│
└── security/
    └── Configuración de JWT y demas cosas
```

---

## 🔐 Autenticación y Autorización

El sistema utiliza **JWT (JSON Web Token)** para autenticar a los usuarios.

### Roles disponibles:
- `ROLE_USER`: Puede crear, editar y eliminar sus propias tareas.  
- `ROLE_ADMIN`: Tiene acceso a todas las tareas del sistema.

---

## 🧾 Endpoints principales

### **AuthController** (`/api/auth`)

| Método | Endpoint | Descripción | Acceso |
|--------|-----------|-------------|---------|
| POST | `/register` | Registra un nuevo usuario | Público |
| POST | `/login` | Inicia sesión y devuelve tokens JWT | Público |
| POST | `/refresh` | Refresca el token de acceso usando el refresh token | Público |

**Ejemplo de `AuthResponse`:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR...",
  "username": "juan123",
  "email": "juan@mail.com",
  "fullName": "Juan Pérez",
  "role": "USER",
  "tokenType": "Bearer"
}
```

---

### **TaskController** (`/api/tasks`)

| Método | Endpoint | Descripción | Acceso |
|--------|-----------|-------------|---------|
| GET | `/` | Obtiene todas las tareas | ADMIN |
| GET | `/me` | Obtiene las tareas del usuario autenticado | USER / ADMIN |
| GET | `/{id}` | Obtiene una tarea por ID | USER / ADMIN |
| POST | `/` | Crea una nueva tarea | USER / ADMIN |
| PUT | `/{id}` | Actualiza una tarea existente | USER / ADMIN |
| DELETE | `/{id}` | Elimina una tarea | USER / ADMIN |

**Ejemplo de `TaskDTO`:**
```json
{
  "id": 1,
  "title": "Estudiar Spring Boot",
  "description": "Repasar controladores y seguridad JWT",
  "completed": false
}
```

---

## 🧠 Lógica de seguridad

- Los endpoints de tareas requieren un **token JWT válido** en el encabezado:
  ```
  Authorization: Bearer <access_token>
  ```
- El token se genera al iniciar sesión y expira tras un tiempo configurable.
- El `refreshToken` permite obtener un nuevo `accessToken` sin volver a loguearse.

---

## 🧑‍💻 Cómo ejecutar el proyecto

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/simon-zubigaray/todo-app-java-spring
   ```

2. Configurar la base de datos en `application.properties` o `application.yml`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/gestor_tareas
   spring.datasource.username=root
   spring.datasource.password=tu_clave
   spring.jpa.hibernate.ddl-auto=update
   ```

3. Ejecutar el proyecto:
   ```bash
   mvn spring-boot:run
   ```

4. La API quedará disponible en:
   ```
   http://localhost:8080/api
   ```

---