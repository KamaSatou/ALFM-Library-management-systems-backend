package database.example.oracleapi.entity;

import java.io.Serializable;
import java.util.Objects;

public class BorrowDetailId implements Serializable {

    private Long borrowId;
    private Long bookId;

    public BorrowDetailId() {
    }

    public BorrowDetailId(Long borrowId, Long bookId) {
        this.borrowId = borrowId;
        this.bookId = bookId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BorrowDetailId)) return false;

        BorrowDetailId that = (BorrowDetailId) o;

        return Objects.equals(borrowId, that.borrowId)
                && Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(borrowId, bookId);
    }
}