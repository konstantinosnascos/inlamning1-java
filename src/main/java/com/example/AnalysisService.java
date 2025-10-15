package com.example;

import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalysisService {
    private final List<Product> allProducts;
    private final OrderService orderService;
    private static final Logger logger = LoggerFactory.getLogger(AnalysisService.class);

    public AnalysisService(OrderService orderService) {
        // Hämtar en kopia av produkterna från OrderService
        this.allProducts =  orderService.getAllProducts();
        //referens till OrderService för uppdatera
        this.orderService = orderService;
    }

    public Map<String, List<Product>> groupProductsByCategory()
    {
        return allProducts.stream()
                //groupingby, dela in produkter i kategorier
                .collect(Collectors.groupingBy(Product::getCategory));
    }

    public Map <String, List<Product>> groupProductByCategoryThenPrice()
    {
        //starta stream och returnera den
        return allProducts.stream()
                //gruppera efter kateogri
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.collectingAndThen(
                        //collecingandthen för att samla och sortera produkterna
                        Collectors.toList(), list -> list.stream()
                                //skapa ny sorterad lista(kategori)
                                .sorted(Comparator.comparingDouble(Product::getPrice))
                                .toList()
                )));
    }

    public double calculateCustomerTotalValue(String customerName, Map<String, List<Order>> customerOrders)
            throws EmptyOrderException {
        List<Order> orders = customerOrders.get(customerName);
        // om listan orders inte finns eller är tom
        if (orders == null || orders.isEmpty()) {
            throw new EmptyOrderException("Kunden '" + customerName + "' har inga ordrar.");
        }

        return orders.stream()
                //flatMap, för att slå ihop ordrar i en lista och slippa nästlade loopar.
                .flatMap(order -> order.getProducts().stream())
                // mapToDouble, för att få fram priset på varje produkt
                .mapToDouble(Product::getPrice)
                //summera pris
                .sum();
    }

    public Map<String, List<Order>> getCustomerOrders()
    {
        return orderService.getCustomerOrders();
    }

    public List<Map.Entry<String, Long>> findTopThreeProducts(Map<String, List<Order>> customerOrders) {
        return customerOrders.values().stream()
                //en stream med alla ordrar
                .flatMap(List::stream)
                //flatMap, för att slå ihop ordrar i en lista och slippa nästlade loopar.
                .flatMap(order -> order.getProducts().stream())
                //gruppera med produktnamn och räkna dom
                .collect(Collectors.groupingBy(Product::getName, Collectors.counting()))
                //ändra till en MapEntry lista (produktnamn, antal)
                .entrySet().stream()
                //Sortera efter antal (värdet) i fallande ordning
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder()))
                //Ta endast 3
                .limit(3)
                //Samla ihop i en lista
                .toList();
    }
}
