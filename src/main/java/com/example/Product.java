package com.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Product {
    private String id;
    private String name;
    private String category;
    private double price;
    private static final Logger logger = LoggerFactory.getLogger(Product.class);


    public Product(String id, String name, String category, double price) throws InvalidPriceException {
        if (price <=0)
        {
            throw new InvalidPriceException("Priset måste vara högre än 0.");
        }
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    //<editor-fold desc="Getters and Setters">
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws InvalidPriceException
    {
        if (price <= 0) {
            throw new InvalidPriceException("Priset måste vara större än 0.");
        }
        this.price = price;
    }
    //</editor-fold>

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                '}';
    }
}
