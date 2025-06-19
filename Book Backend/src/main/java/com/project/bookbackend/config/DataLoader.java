package com.project.bookbackend.config;

import com.project.bookbackend.user.Role;
import com.project.bookbackend.user.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

	private final RoleRepository roleRepository;

	public DataLoader(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Bean
	public CommandLineRunner loadUserRole() {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(Role.builder().name("USER").build());
			}
		};
	}
}
