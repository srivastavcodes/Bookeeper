package com.project.bookbackend.book;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowedBookResponse {

	private Integer id;
	private String title;

	private String authorName;
	private String isbn;

	private double rating;

	private boolean returned;
	private boolean returnApproved;
}
