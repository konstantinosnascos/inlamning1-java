package com.example;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderService {
    //Lista med alla produkter
    private List<Product> allProducts;
    //Lista med alla ordrar
    private List<Order> allOrders;
    //map med kundnamn som nyckel, data är kundens orderhistorik
    private Map<String, List<Order>> customerOrders;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService() {
        //konstruktor initierar listor och HashMap för produkter och ordrar
        this.allProducts = new ArrayList<>();
        this.allOrders = new ArrayList<>();
        this.customerOrders = new HashMap<>();
    }

    public List<Order> getAllOrders() {
        return allOrders;
    }

    public Map<String, List<Order>> getCustomerOrders() {
        return customerOrders;
    }

    public boolean addProduct(Product product)
    {
        //Kontrollera om produkten finns.
        boolean exists = allProducts.stream()
                        .anyMatch(p -> p.getId().equalsIgnoreCase(product.getId()));
        if (exists)
        {
            //om produktId finns, lägg inte till
            return false;
        }
        //finns ej, lägg till
        allProducts.add(product);
        return true;
    }

    public boolean removeProductById(String id)
    {
        //removeIf går igenom och tar bort alla produkter med id(dock ska bara finnas en)
        // (true = en togs bort/false = ingen togs bort)
        return allProducts.removeIf(p -> p.getId().equalsIgnoreCase(id));
    }

    public Product findProductById(String id)
    {

        //filter hittar produkter med matchande id
        return allProducts.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst()
                //hämtar första den hittar
                .orElse(null);
        //orElse(null) returnerar produkt om den hittas annars null
    }

    public void addOrder(Order order)
    {
        //lägger till i orderListan
        allOrders.add(order);
        customerOrders.computeIfAbsent(
                //computeIfAbsent hämtar lista om den finns, annars skapar den ny
                order.getCustomerName().toLowerCase(), k -> new ArrayList<>()).add(order);
    }

    public List<Product> getAllProducts()
    {
        return allProducts;
    }

    public List<Order> getOrdersByCustomer (String customerName) throws EmptyOrderException
    {
        List<Order> orders = customerOrders.get(customerName.toLowerCase());

        //om kunden inte har några ordrar throw exception
        if (orders == null || orders.isEmpty())
        {
            throw new EmptyOrderException("Kunden har inga ordrar registrerade: " + customerName);
        }
        //return kundens orderLista
        return orders;
    }

    public boolean removeOrderById (String orderId)
    {
        //tar bort order med orderID
        boolean removed = allOrders.removeIf(order -> order.getOrderId().equalsIgnoreCase(orderId));
        //removeIf tar bort ordern från listan om den hittas
        if (removed)
        {
            customerOrders.values().forEach(orderList ->
                    orderList.removeIf(order -> order.getOrderId().equalsIgnoreCase(orderId)));
        }
        return removed;
    }
    public double calculateOrderTotal(Order order)
    {
        //summera totalbelopp för en kund
        return order.getProducts()
                //hämtar listan med produkter
                .stream()
                .mapToDouble(Product::getPrice)
                //omvandlar produkt till bara pris
                .sum();
                //summera alla priser
    }
}
