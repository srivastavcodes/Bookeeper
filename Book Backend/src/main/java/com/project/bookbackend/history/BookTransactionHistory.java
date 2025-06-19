package com.project.bookbackend.history;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.bookbackend.book.Book;
import com.project.bookbackend.common.BaseEntity;
import com.project.bookbackend.user.User;
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
public class BookTransactionHistory extends BaseEntity {

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	private boolean returned;
	private boolean returnApproved;
}
