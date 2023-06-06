package com.electronic;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.electronic.entites.Role;
import com.electronic.repository.RoleRepository;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepo;

	@Value("${admin_id}")
	private String admin_id;

	@Value("${user_id}")
	private String user_id;

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// System.out.println(new BCryptPasswordEncoder().encode("1234"));
		Role role_admin = Role.builder().roleId(admin_id).roleName("ROLE_ADMIN").build();

		Role role_user = Role.builder().roleId(user_id).roleName("ROLE_USER").build();

		roleRepo.save(role_admin);
		roleRepo.save(role_user);

	}

}
