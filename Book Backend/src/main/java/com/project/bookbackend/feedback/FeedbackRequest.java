package com.project.bookbackend.feedback;

import jakarta.validation.constraints.*;

public record FeedbackRequest(

	@Min(value = 0, message = "201")
	@Max(value = 5, message = "202")
	@Positive(message = "200")
	Double rating,

	@NotEmpty(message = "203")
	String comment,

	@NotNull(message = "204")
	Integer bookId
) {
}
