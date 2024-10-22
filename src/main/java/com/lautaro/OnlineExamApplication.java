package com.lautaro;

import com.lautaro.entity.rol.RoleRepository;
import com.lautaro.entity.rol.Role;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.Instant;
import java.util.TimeZone;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
@SpringBootApplication
public class OnlineExamApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineExamApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			String[] roles = {"USER", "ADMIN", "MODERATOR"};
			for (String role : roles) {
				if (roleRepository.findByName(role).isEmpty()) {
					roleRepository.save(Role.builder().name(role).build());
				}
			}
		};
	}


}
