package com.project.bookbackend.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {

	@Query("""
		select book from Book book where book.isArchived = false
		and book.isShareable = true and book.owner.id != :userId
		""")
	Page<Book> findAllDisplayableBooks(Pageable pageable, Integer userId);
}
