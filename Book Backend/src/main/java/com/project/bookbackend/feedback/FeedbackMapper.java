package com.project.bookbackend.feedback;

import com.project.bookbackend.book.Book;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {

	public Feedback toFeedback(FeedbackRequest req) {
		return Feedback.builder().rating(req.rating()).comment(req.comment())
			.book(Book.builder().id(req.bookId()).isShareable(false)
				.isArchived(false).build())
			.build();
	}

	public FeedbackResponse toFeedbackResponse(Feedback req, Integer userId) {
		return FeedbackResponse.builder().rating(req.getRating())
			.comment(req.getComment())
			.ownFeedback(Objects.equals(req.getCreatedBy(), userId))
			.build();
	}
}
