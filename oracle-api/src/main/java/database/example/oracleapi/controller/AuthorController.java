package database.example.oracleapi.controller;

import database.example.oracleapi.entity.Author;
import database.example.oracleapi.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@CrossOrigin(origins = "*")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok(
                authorService.getAllAuthors()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                authorService.getAuthorById(id)
        );
    }

    @PostMapping
    public ResponseEntity<Author> createAuthor(
            @RequestBody Author author) {

        Author created = authorService.createAuthor(author);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(
            @PathVariable Long id,
            @RequestBody Author author) {

        return ResponseEntity.ok(
                authorService.updateAuthor(id, author)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(
            @PathVariable Long id) {

        authorService.deleteAuthor(id);

        return ResponseEntity.noContent().build();
    }
}