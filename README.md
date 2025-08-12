# Book Management System

A Spring Boot application for sharing and borrowing books between users.

## Features

- **Book Management**: Add, view, and manage your book collection
- **Book Sharing**: Lend books to other users
- **Borrowing System**: Borrow books with return approval workflow
- **File Upload**: Upload book cover images
- **User Authentication**: Secure access with Spring Security

## Key Operations

- Create and manage books
- Borrow/return books
- Approve returns as book owner
- View borrowing history
- Upload book covers

## Tech Stack

- Spring Boot
- Spring Security
- JPA/Hibernate
- File Storage Service

## Business Rules

- Can't borrow your own books
- One book per user at a time
- Returns require owner approval
- Only owners can modify their books

## Getting Started

1. Configure database in `application.properties`
2. Set up file storage for covers
3. Run: `./mvnw spring-boot:run`