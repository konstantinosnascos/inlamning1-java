package com.example;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Order {
    private String orderId;
    private List<Product> products;
    private String customerName;
    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    public Order(String orderId, List<Product> products, String customerName) {
        this.orderId = orderId;
        this.products = products;
        this.customerName = customerName;
    }

    //<editor-fold desc="Getters and Setters">
    public String getOrderId() {
        return orderId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getCustomerName() {
        return customerName;
    }
    //</editor-fold>

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", products=" + products +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}
