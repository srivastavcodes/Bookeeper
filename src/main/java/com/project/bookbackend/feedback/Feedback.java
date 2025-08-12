package com.project.bookbackend.feedback;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.bookbackend.book.Book;
import com.project.bookbackend.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Feedback extends BaseEntity {

	private Double rating;
	private String comment;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;
}
