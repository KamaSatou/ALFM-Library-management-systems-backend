package database.example.oracleapi.dto;

import database.example.oracleapi.entity.Book;

public class RecommendedBookResponse {

    private Book book;
    private int score;
    private String reason;

    public RecommendedBookResponse() {
    }

    public RecommendedBookResponse(Book book, int score, String reason) {
        this.book = book;
        this.score = score;
        this.reason = reason;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}