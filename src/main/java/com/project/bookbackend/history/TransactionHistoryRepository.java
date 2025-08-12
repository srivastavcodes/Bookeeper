package com.project.bookbackend.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Integer> {

	@Query("""
		select transactionHistory from BookTransactionHistory transactionHistory
		where transactionHistory.user.id = :userId
		""")
	Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);

	@Query("""
		select transactionHistory from BookTransactionHistory transactionHistory
		where transactionHistory.book.createdBy = :userId
		""")
	Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Integer userId);

	@Query("""
		select (count (*) > 0) as isBorrowed from BookTransactionHistory transactionHistory
		where transactionHistory.user.id = :userId and transactionHistory.book.id = :bookId
		and transactionHistory.returnApproved = false
		""")
	boolean isAlreadyBorrowedByUser(Integer bookId, Integer userId);

	@Query("""
		select (count (*) > 0) as isBorrowed from BookTransactionHistory transactionHistory
		where transactionHistory.user.id != :userId and transactionHistory.book.id = :bookId
		and transactionHistory.returnApproved = false
		""")
	boolean isAlreadyBorrowedByOtherUser(Integer bookId, Integer userId);

	@Query("""
		select transactionHistory from BookTransactionHistory transactionHistory where
		transactionHistory.book.id = :bookId and transactionHistory.user.id = :userId
		and transactionHistory.returned = false and transactionHistory.returnApproved = false
		""")
	Optional<BookTransactionHistory> findByBookIdAndUserId(Integer bookId, Integer userId);

	@Query("""
		select transactionHistory from BookTransactionHistory transactionHistory where
		transactionHistory.book.id = :bookId and transactionHistory.book.owner.id = :userId
		and transactionHistory.returned = true and transactionHistory.returnApproved = false
		""")
	Optional<BookTransactionHistory> findByBookIdAndOwnerId(Integer bookId, Integer userId);
}







