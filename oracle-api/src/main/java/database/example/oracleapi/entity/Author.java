package database.example.oracleapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "LIB_AUTHORS")
public class Author {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "author_seq"
    )
    @SequenceGenerator(
            name = "author_seq",
            sequenceName = "SEQ_LIB_AUTHORS",
            allocationSize = 1
    )
    @Column(name = "AUTHORID", nullable = false)
    private Long authorId;

    @Column(name = "AUTHORNAME", nullable = false, length = 300)
    private String authorName;


    public Author() {
    }


    public Long getAuthorId() {
        return authorId;
    }


    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }


    public String getAuthorName() {
        return authorName;
    }


    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}