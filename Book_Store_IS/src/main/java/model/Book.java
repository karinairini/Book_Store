package model;

import java.time.LocalDate;

//Java Bean
//constructor fara argumente, getters, setters, toate argumentele private, serializable
//POJO - Plain Old JAVA Object(clasa care nu stie sa faca nimic, nu implementeaza nici o interfata, nu are nici o adnotare)

public class Book {
    private Long id;
    private String author;
    private String title;
    private LocalDate publishedDate;
    private Double price;
    private Integer stock;
    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("Book ID: %d | Author: %s | Title: %s | Published Date: %s | Price: %f | Stock: %d", id, author, title, publishedDate, price, stock);
    }
}
