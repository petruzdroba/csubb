package org.example.domain;

public class MenuItem {
    private Long id;
    private String category;
    private String item;
    private float price;
    private String currency;

    public MenuItem(Long id, String category, String item, float price, String currency) {
        this.id = id;
        this.category = category;
        this.item = item;
        this.price = price;
        this.currency = currency;
    }

    public MenuItem() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return item+"\n";
    }
}
