package com.project.bookbackend.book;

import com.project.bookbackend.common.PageResponse;
import com.project.bookbackend.exception.OperationNotPermittedException;
import com.project.bookbackend.file.FileStorageService;
import com.project.bookbackend.history.BookTransactionHistory;
import com.project.bookbackend.history.TransactionHistoryRepository;
import com.project.bookbackend.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.project.bookbackend.book.BookSpecification.withOwnerId;

@Service
@RequiredArgsConstructor
public class BookService {

	private final FileStorageService fileStorageService;
	private final BookRepository bookRepository;
	private final BookMapper bookMapper;
	private final TransactionHistoryRepository transactionHistoryRepository;

	public Integer saveBook(BookRequest req, Authentication connectedUser) {
		User user = (User) connectedUser.getPrincipal();

		Book book = bookMapper.toBook(req);
		book.setOwner(user);
		return bookRepository.save(book).getId();
	}

	public BookResponse findBookById(final Integer bookId) {
		return bookRepository.findById(bookId)
			.map(bookMapper::toBookResponse)
			.orElseThrow(() -> new EntityNotFoundException("No Book found with Id::" + bookId));
	}

	public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUser) {
		User user = (User) connectedUser.getPrincipal();

		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAT").descending());
		Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());

		List<BookResponse> bookResponseList = books.stream()
			.map(bookMapper::toBookResponse)
			.toList();

		return new PageResponse<>(
			bookResponseList, books.getNumber(), books.getSize(),
			books.getTotalElements(), books.getTotalPages(),
			books.isFirst(), books.isLast()
		);
	}

	public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUser) {
		User user = (User) connectedUser.getPrincipal();

		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAT").descending());
		Page<Book> books = bookRepository.findAll(withOwnerId(user.getId()), pageable);

		List<BookResponse> bookResponseList = books.stream()
			.map(bookMapper::toBookResponse)
			.toList();

		return new PageResponse<>(bookResponseList, books.getNumber(), books.getSize(),
			books.getTotalElements(), books.getTotalPages(),
			books.isFirst(), books.isLast()
		);
	}

	public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUser) {
		User user = (User) connectedUser.getPrincipal();

		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAT").descending());
		Page<BookTransactionHistory> borrowedBooks = transactionHistoryRepository
			.findAllBorrowedBooks(pageable, user.getId());

		List<BorrowedBookResponse> bookResponseList = borrowedBooks.stream()
			.map(bookMapper::toBorrowedBookResponse)
			.toList();

		return new PageResponse<>(bookResponseList, borrowedBooks.getNumber(), borrowedBooks.getSize(),
			borrowedBooks.getTotalElements(), borrowedBooks.getTotalPages(),
			borrowedBooks.isFirst(), borrowedBooks.isLast()
		);
	}

	public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUser) {
		User user = (User) connectedUser.getPrincipal();

		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAT").descending());
		Page<BookTransactionHistory> returnedBooks = transactionHistoryRepository
			.findAllReturnedBooks(pageable, user.getId());

		List<BorrowedBookResponse> bookResponseList = returnedBooks.stream()
			.map(bookMapper::toBorrowedBookResponse)
			.toList();

		return new PageResponse<>(bookResponseList, returnedBooks.getNumber(), returnedBooks.getSize(),
			returnedBooks.getTotalElements(), returnedBooks.getTotalPages(),
			returnedBooks.isFirst(), returnedBooks.isLast()
		);
	}

	public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
		Book verifiedBook = findBookAndVerify(bookId, connectedUser);

		verifiedBook.setShareable(!verifiedBook.isShareable());
		return bookRepository.save(verifiedBook).getId();
	}

	public Integer updateArchivedStatus(Integer bookId, Authentication connectedUser) {
		Book verifiedBook = findBookAndVerify(bookId, connectedUser);

		verifiedBook.setArchived(!verifiedBook.isArchived());
		return bookRepository.save(verifiedBook).getId();
	}

	private Book findBookAndVerify(Integer bookId, Authentication connectedUser) {
		Book requestedBook = bookRepository.findById(bookId)
			.orElseThrow(() -> new EntityNotFoundException("No book found with id::" + bookId));

		User user = (User) connectedUser.getPrincipal();

		if (!Objects.equals(requestedBook.getOwner().getId(), user.getId())) {
			throw new OperationNotPermittedException("Action not allowed. Cause::Not the owner");
		}
		return requestedBook;
	}

	public Integer borrowBook(Integer bookId, Authentication connectedUser) {
		Book book = bookRepository.findById(bookId)
			.orElseThrow(() -> new EntityNotFoundException("No book found with id::" + bookId));

		User user = (User) connectedUser.getPrincipal();
		checkStatusAndVerify(book, user);

		BookTransactionHistory transactionHistory = BookTransactionHistory.builder().user(user)
			.book(book).returned(false).returnApproved(false)
			.build();

		return transactionHistoryRepository.save(transactionHistory).getId();
	}

	private void checkStatusAndVerify(Book book, User user) {

		if (Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationNotPermittedException("Dude...You can't borrow your own book!");
		}
		final boolean isAlreadyBorrowedByUser = transactionHistoryRepository
			.isAlreadyBorrowedByUser(book.getId(), user.getId());

		if (isAlreadyBorrowedByUser) {
			throw new OperationNotPermittedException("Already borrowed by you. Return before requesting.");
		}
		final boolean isAlreadyBorrowedByOtherUser = transactionHistoryRepository
			.isAlreadyBorrowedByOtherUser(book.getId(), user.getId());

		if (isAlreadyBorrowedByOtherUser) {
			throw new OperationNotPermittedException("Already borrowed. Try after sometime.");
		}
	}

	public Integer returnBorrowedBook(Integer bookId, Authentication connectedUser) {
		Book book = bookRepository.findById(bookId)
			.orElseThrow(() -> new EntityNotFoundException("No book found with id::" + bookId));

		if (book.isArchived() || !book.isShareable()) {
			throw new OperationNotPermittedException("Book is archived or is not shareable.");
		}
		User user = (User) connectedUser.getPrincipal();

		if (Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationNotPermittedException("You can't borrow your own book!");
		}

		BookTransactionHistory transactionHistory = transactionHistoryRepository
			.findByBookIdAndUserId(bookId, user.getId())
			.orElseThrow(() -> new OperationNotPermittedException("You did not borrow this book."));

		transactionHistory.setReturned(true);
		return transactionHistoryRepository.save(transactionHistory).getId();
	}

	public Integer approveBookReturn(Integer bookId, Authentication connectedUser) {
		Book book = bookRepository.findById(bookId)
			.orElseThrow(() -> new EntityNotFoundException("No book found with id::" + bookId));

		if (book.isArchived() || !book.isShareable()) {
			throw new OperationNotPermittedException("Book is archived or is not shareable.");
		}
		User user = (User) connectedUser.getPrincipal();

		if (!Objects.equals(book.getOwner().getId(), user.getId())) {
			throw new OperationNotPermittedException("You can't return book you don't own!");
		}
		BookTransactionHistory transactionHistory = transactionHistoryRepository
			.findByBookIdAndOwnerId(bookId, user.getId())
			.orElseThrow(() -> new OperationNotPermittedException("Cannot approve. The book is not returned yet."));

		transactionHistory.setReturnApproved(true);
		return transactionHistoryRepository.save(transactionHistory).getId();
	}

	public void uploadBookCover(Integer bookId, Authentication connectedUser, MultipartFile bookCover) {
		Book book = bookRepository.findById(bookId)
			.orElseThrow(() -> new EntityNotFoundException("No book found with id::" + bookId));
		User user = (User) connectedUser.getPrincipal();

		var savedBookCover = fileStorageService.saveBookCover(user.getId(), bookCover);
		book.setBookCover(savedBookCover);

		bookRepository.save(book);
	}
}







