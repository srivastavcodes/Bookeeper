package com.project.bookbackend.feedback;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

	@Query("""
		select feedbacks from Feedback feedbacks
		where feedbacks.book.id = :bookId
		""")
	Page<Feedback> findAllByBookId(Integer bookId, Pageable pageable);
}
