package com.project.bookbackend.common;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAT;

	@LastModifiedDate
	@Column(insertable = false)
	private LocalDateTime lastModifiedAT;

	@CreatedBy
	@Column(nullable = false, updatable = false)
	private Integer createdBy;

	@LastModifiedBy
	@Column(insertable = false)
	private Integer lastModifiedBy;
}
