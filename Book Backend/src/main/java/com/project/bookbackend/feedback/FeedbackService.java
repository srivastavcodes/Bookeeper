package com.project.bookbackend.feedback;

import com.project.bookbackend.book.Book;
import com.project.bookbackend.book.BookRepository;
import com.project.bookbackend.common.PageResponse;
import com.project.bookbackend.exception.OperationNotPermittedException;
import com.project.bookbackend.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

	private final FeedbackRepository feedbackRepository;
	private final BookRepository bookRepository;
	private final FeedbackMapper feedbackMapper;

	public Integer saveFeedback(FeedbackRequest req, Authentication connectedUser) {
		getUserAndVerify(req.bookId(), connectedUser);

		Feedback feedback = feedbackMapper.toFeedback(req);
		return feedbackRepository.save(feedback).getId();
	}

	private void getUserAndVerify(Integer bookId, Authentication connectedUser) {
		Book book = bookRepository.findById(bookId)
			.orElseThrow(() -> new EntityNotFoundException("No book found with id::" + bookId));

		if (book.isArchived() || !book.isShareable()) {
			throw new OperationNotPermittedException("Book is archived or is not shareable.");
		}
		User user = (User) connectedUser.getPrincipal();

		if (Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationNotPermittedException("Dude...You can't review your own book!");
		}
	}

	public PageResponse<FeedbackResponse> getAllFeedbacks(int page, int size, Integer bookId, Authentication connectedUser) {
		User user = (User) connectedUser.getPrincipal();

		Pageable pageable = PageRequest.of(page, size);
		Page<Feedback> feedbacks = feedbackRepository.findAllByBookId(bookId, pageable);

		List<FeedbackResponse> feedbackResponses = feedbacks.stream()
			.map(feedback -> feedbackMapper.toFeedbackResponse(feedback, user.getId()))
			.toList();

		return new PageResponse<>(
			feedbackResponses, feedbacks.getNumber(), feedbacks.getSize(),
			feedbacks.getTotalElements(), feedbacks.getTotalPages(),
			feedbacks.isFirst(), feedbacks.isLast()
		);
	}
}








