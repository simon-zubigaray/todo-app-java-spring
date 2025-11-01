package jsz.myapp.todoapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de ToDoApp",
                version = "1.0.0",
                description = "Documentación de la API para autenticación y gestión de tareas",
                contact = @Contact(
                        name = "Juan Simón Zubigaray",
                        email = "example@todoapp.com",
                        url = "https://github.com/simon-zubigaray"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        )
)
public class SwaggerConfig {
}