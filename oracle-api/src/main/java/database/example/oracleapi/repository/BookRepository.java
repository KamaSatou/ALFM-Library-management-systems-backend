package database.example.oracleapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import database.example.oracleapi.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
}