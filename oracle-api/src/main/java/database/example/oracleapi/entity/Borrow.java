package database.example.oracleapi.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "LIB_BORROWS")
public class Borrow {

    @Id
    @Column(name = "BORROWID", nullable = false)
    private Long borrowId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MEMBERID", nullable = false)
    private Member member;

    @Column(name = "BORROWDATE", nullable = false)
    private LocalDate borrowDate;

    @Column(name = "DUEDATE", nullable = false)
    private LocalDate dueDate;

    @Column(name = "STATUS", nullable = false, length = 20)
    private String status;

    public Borrow() {
    }

    public Long getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Long borrowId) {
        this.borrowId = borrowId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}