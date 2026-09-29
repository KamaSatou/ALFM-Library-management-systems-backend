package database.example.oracleapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "LIB_BOOKS")
public class Book {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "book_seq"
    )
    @SequenceGenerator(
            name = "book_seq",
            sequenceName = "SEQ_LIB_BOOKS",
            allocationSize = 1
    )
    @Column(name = "BOOKID", nullable = false)
    private Long bookId;

    @Column(name = "TITLE", nullable = false, length = 500)
    private String title;

    @Column(name = "ISBN", length = 30)
    private String isbn;

    @Column(name = "TAG", length = 100)
    private String tag;

    @Column(name = "SERIES", length = 300)
    private String series;

    @Column(name = "CATEGORYID")
    private Long categoryId;

    @Column(name = "AUTHORID")
    private Long authorId;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @Column(name = "AVAILABLEQUANTITY", nullable = false)
    private Integer availableQuantity;

    @Column(name = "PUBLISHYEAR")
    private Integer publishYear;

    @Column(name = "IMAGEURL", length = 1000)
    private String imageUrl;


    public Book() {
    }


    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }


    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }


    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }


    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }


    public Integer getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(Integer publishYear) {
        this.publishYear = publishYear;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}