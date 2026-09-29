package database.example.oracleapi.repository;

import database.example.oracleapi.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}