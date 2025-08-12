package com.project.bookbackend.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Authentication")
@RestController
@RequestMapping("auth")
public class AuthenticationController {

	private final AuthenticationService authService;

	@Autowired
	public AuthenticationController(AuthenticationService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(
		@RequestBody @Valid RegisterRequest req) throws MessagingException {

		authService.registerUser(req);
		return ResponseEntity.created(URI.create("/activate-account")).build();
	}

	@PostMapping("/verify")
	public ResponseEntity<AuthResponse> verifyUser(@RequestBody @Valid AuthRequest req) {
		return ResponseEntity.ok(authService.verifyUser(req));
	}

	@GetMapping("/activate-account")
	public void activateAccount(@RequestParam String token) throws MessagingException {
		authService.activateAccount(token);
	}
}







