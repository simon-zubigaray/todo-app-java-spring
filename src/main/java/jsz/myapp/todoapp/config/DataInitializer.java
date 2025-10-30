package jsz.myapp.todoapp.config;


import jsz.myapp.todoapp.model.UserEntity;
import jsz.myapp.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initDatabase() {
        return args -> {
            log.info("🚀 Iniciando precarga de datos...");

            // Verificar si ya existen usuarios
            if (userRepository.count() == 0) {
                // Crear usuario ADMIN
                UserEntity admin = new UserEntity();
                admin.setFull_name("Administrador del Sistema");
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                userRepository.save(admin);
                log.info("✅ Usuario ADMIN creado: username=admin, password=admin123");

                // Crear usuario normal
                UserEntity user = new UserEntity();
                user.setFull_name("Usuario Normal");
                user.setUsername("user");
                user.setEmail("user@example.com");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRole("USER");
                userRepository.save(user);
                log.info("✅ Usuario USER creado: username=user, password=user123");

                // Crear usuario adicional
                UserEntity john = new UserEntity();
                john.setFull_name("John Doe");
                john.setUsername("john");
                john.setEmail("john@example.com");
                john.setPassword(passwordEncoder.encode("john123"));
                john.setRole("USER");
                userRepository.save(john);
                log.info("✅ Usuario USER creado: username=john, password=john123");

                log.info("🎉 Precarga de datos completada exitosamente!");
            } else {
                log.info("ℹ️ La base de datos ya contiene usuarios, omitiendo precarga.");
            }
        };
    }
}