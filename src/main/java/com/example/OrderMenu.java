package com.example;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderMenu {

    private final InputHelper input;
    private final OrderService orderService;
    private static final Logger logger = LoggerFactory.getLogger(OrderMenu.class);

    public OrderMenu(Scanner scan, OrderService orderService) {
        //Konstruktorn tar in scanner och OrderService
        this.input = new InputHelper(scan);//input
        this.orderService = orderService;//hantera ordrar och produkter
    }

    public void run() {
        //loop för ordermenyn
        boolean running = true;
        while (running) {
            try {
                printMenu();
                int choice = input.getInt("Välj ett alternativ: ");
                switch (choice) {
                    case 1 -> createOrder();//skapa ny order
                    case 2 -> displayAllOrders();//visa alla ordrar
                    case 3 -> displayCustomerOrders();//visa en kunds ordrar
                    case 4 -> removeOrder();//ta bort en tidigare order
                    case 5 -> running = false;
                    default -> System.out.println("Felaktigt val, försök igen!");
                }
            } catch (Exception e) {
                System.out.println("Ett oväntat fel uppstod i ordermenyn: " + e.getMessage());
                logger.error("Fel i OrderMenu.run()", e);
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- Ordermeny ---");
        System.out.println("1. Skapa en ny order");
        System.out.println("2. Visa alla ordrar");
        System.out.println("3. Visa ordrar för specifik kund");
        System.out.println("4. Ta bort en order");
        System.out.println("5. Gå tillbaka till huvudmenyn");
    }

    private void createOrder() {
        //skapa ny order
        System.out.println("\n--- Skapa en ny order ---");
        String orderId = "order" + (orderService.getAllOrders().size() + 1);

        //generera ny orderID, kanske ska göra samma för produkt id om jag hinner
        String customerName = input.getString("Vad heter kunden? ");
        List<Product> products = new ArrayList<>();

        boolean adding = true;
        while (adding) {
            printAddProductMenu();//liten switch
            int choice = input.getInt("Välj ett alternativ: ");
            switch (choice) {
                case 1 -> {//lägg till produkt i ordern
                    String productId = input.getString("Skriv produkt-ID: ");
                    try {
                        Product product = orderService.findProductById(productId);
                        if (product == null) throw new ProductNotFoundException("Produkt-ID hittades inte.");
                        products.add(product);
                        System.out.println("Produkten är tillagd i listan.");
                        logger.info("Produkten med ID {} är tillagd i listan.", productId);
                    } catch (ProductNotFoundException e) {
                        System.out.println(e.getMessage());
                        logger.warn("Produkten {} saknas vid orderläggning ", productId);
                    }
                }
                case 2 -> {//inga fler produkter, skapa order
                    adding = false;
                    if (products.isEmpty()) {
                        System.out.println("Ingen produkt tillagd. Order avbröts.");
                        logger.info("Ingen produkt tillagd, avbryter");
                        return;
                    }
                    Order order = new Order(orderId, products, customerName);
                    orderService.addOrder(order);
                    System.out.println("Order skapad med ID: " + orderId);
                    logger.info("Ny produkt tillagd i ordern med ID: {} skapad", orderId);
                }
                case 99 -> {
                    //om jag vill avbryta mitt i
                    System.out.println("Ordern avbröts.");
                    logger.info("Ordern avbröts");
                    return;
                }
                default -> System.out.println("Felaktigt val, försök igen!");
            }
        }
    }

    private void printAddProductMenu() {
        System.out.println("1. Lägg till produkt med produkt-ID");
        System.out.println("2. Inga fler produkter ska läggas till");
        System.out.println("99. Avbryt hela ordern");
    }

    private void displayAllOrders() {
        //skriv ut alla ordrar
        System.out.println("\n--- Alla ordrar ---");
        List<Order> orders = orderService.getAllOrders();
        if (orders.isEmpty()) {
            System.out.println("Inga ordrar finns.");
            logger.info("Inga ordrar finns");
            return;
        }
        for (Order o : orders) {
            System.out.println("Order-ID: " + o.getOrderId());
            System.out.println("Kund: " + o.getCustomerName());
            o.getProducts().forEach(p -> System.out.println("  " + p));
            System.out.println("Totalt: " + orderService.calculateOrderTotal(o) + " kr\n");
        }
        logger.info("Listade alla kundordrar.");
    }
    private void removeOrder() {
        // ta bort en order med orderID
        System.out.println("\n--- Ta bort en order ---");
        List<Order> orders = orderService.getAllOrders();

        if (orders.isEmpty()) {
            System.out.println("Inga ordrar att ta bort.");
            logger.info("Inga ordrar att ta bort – orderlistan är tom.");
            return;
        }

        // visar alla ordrar med orderID och kundnamn
        orders.forEach(o -> System.out.println("Order ID: " + o.getOrderId() +
                ": för kunden " + o.getCustomerName()));

        String id = input.getString("Skriv order-ID för att ta bort: ");
        boolean removed = orderService.removeOrderById(id);

        // skriv ut relevant info baserat på resultat
        if (removed) {
            System.out.println("Ordern borttagen.");
            logger.info("Order med ID '{}' togs bort.", id);
        } else {
            System.out.println("Order-ID hittades inte.");
            logger.warn("Försökte ta bort order med ID '{}' men den hittades inte.", id);
        }
    }

    private void displayCustomerOrders() {
        System.out.println("\n--- Visa ordrar för specifik kund ---");
        String customer = input.getString("Ange kundens namn: ");

        try {
            //hämtar och printar kundens ordrar
            List<Order> orders = orderService.getOrdersByCustomer(customer);
            System.out.println("\nOrdrar för kund: " + customer);
            for (Order o : orders)//loopar igenom ordrar för kunden
            {
                System.out.println("Order ID: " + o.getOrderId());
                o.getProducts().forEach(p -> System.out.println(
                        " " + p.getName() + " (" + p.getPrice() + " kr)"));
                double total = orderService.calculateOrderTotal(o);
                //summera totalkostnaden och skriver ut den
                System.out.println("Totalt för denna order: " + total + "kr\n");
            }
            logger.info("Visade ordrar för {}", customer);
        } catch (EmptyOrderException e)
        {
            System.out.println(e.getMessage());
            logger.warn("Ingen order hittades för {}", customer);
        }
    }
}