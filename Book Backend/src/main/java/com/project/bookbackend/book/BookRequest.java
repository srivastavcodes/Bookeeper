package com.project.bookbackend.book;

import jakarta.validation.constraints.NotBlank;

public record BookRequest(

	Integer id,

	@NotBlank(message = "Title cannot be blank")
	String title,

	@NotBlank(message = "Author name is mandatory")
	String authorName,

	@NotBlank(message = "ISBN is mandatory")
	String isbn,

	@NotBlank(message = "Synopsis cannot be blank")
	String synopsis,

	boolean shareable
) {
}
