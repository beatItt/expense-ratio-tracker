package com.learn.expensetracker.domain;

import java.math.BigDecimal;

public class Category {

    private Integer categoryId;
    private Integer userId;
    private String title;
    private String description;
    private BigDecimal totalExpensePerCategory;

    public Category(Integer categoryId, Integer userId, String title, String description, BigDecimal totalExpensePerCategory) {
        this.categoryId = categoryId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.totalExpensePerCategory = totalExpensePerCategory;
    }

    public Category() {
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTotalExpensePerCategory() {
        return totalExpensePerCategory;
    }

    public void setTotalExpensePerCategory(BigDecimal totalExpensePerCategory) {
        this.totalExpensePerCategory = totalExpensePerCategory;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", totalExpensePerCategory=" + totalExpensePerCategory +
                '}';
    }
}
