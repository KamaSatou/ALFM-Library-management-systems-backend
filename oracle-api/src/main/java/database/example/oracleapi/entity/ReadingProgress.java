package database.example.oracleapi.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "LIB_READING_PROGRESS",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UQ_MEMBER_BOOK_PROGRESS",
                        columnNames = {"MEMBERID", "BOOKID"}
                )
        }
)
public class ReadingProgress {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reading_progress_seq"
    )
    @SequenceGenerator(
            name = "reading_progress_seq",
            sequenceName = "SEQ_READING_PROGRESS",
            allocationSize = 1
    )
    @Column(name = "PROGRESSID")
    private Long progressId;

    @Column(name = "MEMBERID", nullable = false)
    private Long memberId;

    @Column(name = "BOOKID", nullable = false)
    private Long bookId;

    @Column(name = "PROGRESS", nullable = false)
    private Integer progress = 0;

    @Column(name = "STATUS", nullable = false, length = 20)
    private String status = "UNREAD";

    public ReadingProgress() {
    }

    public Long getProgressId() {
        return progressId;
    }

    public void setProgressId(Long progressId) {
        this.progressId = progressId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}