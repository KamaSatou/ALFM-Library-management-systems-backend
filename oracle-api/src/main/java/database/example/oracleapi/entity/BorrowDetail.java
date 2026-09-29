package database.example.oracleapi.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "LIB_BORROW_DETAILS")
@IdClass(BorrowDetailId.class)
public class BorrowDetail {

    @Id
    @Column(name = "BORROWID", nullable = false)
    private Long borrowId;

    @Id
    @Column(name = "BOOKID", nullable = false)
    private Long bookId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "BORROWID",
            referencedColumnName = "BORROWID",
            insertable = false,
            updatable = false
    )
    private Borrow borrow;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "BOOKID",
            referencedColumnName = "BOOKID",
            insertable = false,
            updatable = false
    )
    private Book book;

    @Column(name = "RETURNDATE")
    private LocalDate returnDate;

    public BorrowDetail() {
    }

    public Long getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Long borrowId) {
        this.borrowId = borrowId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Borrow getBorrow() {
        return borrow;
    }

    public void setBorrow(Borrow borrow) {
        this.borrow = borrow;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}