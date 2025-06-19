package com.project.bookbackend.book;

import com.project.bookbackend.file.FileUtils;
import com.project.bookbackend.history.BookTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

	public Book toBook(BookRequest req) {

		return Book.builder().id(req.id())
			.title(req.title())
			.authorName(req.authorName())
			.isbn(req.isbn())
			.synopsis(req.synopsis())
			.isArchived(false)
			.isShareable(req.shareable())
			.build();
	}

	public BookResponse toBookResponse(Book req) {

		return BookResponse.builder().id(req.getId())
			.title(req.getTitle())
			.authorName(req.getAuthorName())
			.isbn(req.getIsbn())
			.synopsis(req.getSynopsis())
			.rating(req.getRating())
			.owner(req.getOwner().getFullName())
			.isArchived(req.isArchived())
			.isShareable(req.isShareable())
			.bookCover(FileUtils.readCoverFromLocation(req.getBookCover()))
			.build();
	}

	public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory req) {
		final Book book = req.getBook();

		return BorrowedBookResponse.builder().id(book.getId())
			.title(book.getTitle())
			.authorName(book.getAuthorName())
			.isbn(book.getIsbn())
			.returned(req.isReturned())
			.returnApproved(req.isReturnApproved())
			.build();
	}
}








