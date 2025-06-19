package com.project.bookbackend.security;

import com.project.bookbackend.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String token;

	private LocalDateTime createdAT;
	private LocalDateTime expiresAT;
	private LocalDateTime validatedAT;

	@ManyToOne
	@JoinColumn(name = "userId", nullable = false)
	private User user;
}
