package database.example.oracleapi.service;

import java.util.List;

import org.springframework.stereotype.Service;

import database.example.oracleapi.entity.Book;
import database.example.oracleapi.repository.BookRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Book not found: " + id));
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book bookDetails) {

        Book book = getBookById(id);

        book.setTitle(bookDetails.getTitle());
        book.setIsbn(bookDetails.getIsbn());
        book.setTag(bookDetails.getTag());
        book.setSeries(bookDetails.getSeries());
        book.setCategoryId(bookDetails.getCategoryId());
        book.setAuthorId(bookDetails.getAuthorId());
        book.setQuantity(bookDetails.getQuantity());
        book.setAvailableQuantity(bookDetails.getAvailableQuantity());
        book.setPublishYear(bookDetails.getPublishYear());
        book.setImageUrl(bookDetails.getImageUrl());

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}