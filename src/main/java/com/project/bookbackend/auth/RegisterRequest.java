package com.project.bookbackend.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RegisterRequest {

	@NotBlank(message = "Firstname is mandatory.")
	private String firstname;

	@NotBlank(message = "Lastname is mandatory.")
	private String lastname;

	private LocalDate dateOfBirth;

	@Email(message = "Invalid Email Format.")
	@Column(unique = true)
	@NotEmpty(message = "Email can't be Empty.")
	private String userEmail;

	@Size(min = 8, message = "Password can't be << 8 chars.")
	private String password;
}







