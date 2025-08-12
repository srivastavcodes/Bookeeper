package com.project.bookbackend.feedback;

import com.project.bookbackend.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Feedback")
@RestController
@RequestMapping("feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

	private final FeedbackService feedbackService;

	@PostMapping
	public ResponseEntity<Integer> saveFeedback(
		@Valid @RequestBody FeedbackRequest req, Authentication connectedUser
	) {
		return ResponseEntity.ok(feedbackService.saveFeedback(req, connectedUser));
	}

	@GetMapping("/book/{book-id}")
	public ResponseEntity<PageResponse<FeedbackResponse>> getAllFeedbacksByBook(
		@RequestParam(name = "page", defaultValue = "0", required = false) int page,
		@RequestParam(name = "size", defaultValue = "10", required = false) int size,
		@PathVariable("book-id") Integer bookId, Authentication connectedUser
	) {
		return ResponseEntity.ok(feedbackService.getAllFeedbacks(page, size, bookId, connectedUser));
	}
}








