package database.example.oracleapi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "LIB_CATEGORIES")
public class Category {

    @Id
    @Column(name = "CATEGORYID", nullable = false)
    private Long categoryId;

    @Column(name = "CATEGORYNAME", nullable = false, length = 200)
    private String categoryName;

    public Category() {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}