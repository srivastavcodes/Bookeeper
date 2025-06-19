package com.project.bookbackend.book;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookResponse {

	private Integer id;
	private String title;

	private String authorName;
	private String isbn;
	private String synopsis;

	private byte[] bookCover;
	private double rating;

	private String owner;

	private boolean isArchived;
	private boolean isShareable;
}
