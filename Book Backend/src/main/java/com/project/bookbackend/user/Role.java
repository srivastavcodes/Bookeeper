package com.project.bookbackend.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@Table(name = "_role")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(unique = true)
	private String name;

	@ManyToMany(mappedBy = "roles")
	@JsonManagedReference
	private List<User> users;

	@Column(nullable = false, updatable = false)
	@CreatedDate
	private LocalDateTime createdDate;

	@Column(insertable = false)
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
}








