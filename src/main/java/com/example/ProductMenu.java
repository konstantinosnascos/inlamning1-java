package com.example;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductMenu {

    private final InputHelper input;
    private final OrderService orderService;
    private final AnalysisService analysisService;
    private static final Logger logger = LoggerFactory.getLogger(ProductMenu.class);

    public ProductMenu(Scanner input, OrderService orderService, AnalysisService analysisService) {
        // Konstruktor tar in scanner och annat
        this.input = new InputHelper(input);//input för val och skapa produkter
        this.orderService = orderService;//hantera produkter och ordrar
        this.analysisService = analysisService;//för tillgång till AnalysisService
    }

    public void run() {
        //loop för produktmenyn
        boolean running = true;
        while (running) {
            try {
                printMenu();//visa menyalternativ
                int choice = input.getInt("Välj ett alternativ: ");
                switch (choice) {
                    case 1 -> addProduct();//lägg till produkt
                    case 2 -> displayProductsByCategory();//visa produkter efter kategori
                    case 3 -> removeProduct();//ta bort produkt
                    case 4 -> running = false;//till huvudmeny
                    default -> System.out.println("Felaktigt val, försök igen!");
                }
            } catch (Exception e) {
                System.out.println("Ett fel uppstod i produktmenyn: " + e.getMessage());
                logger.error("Något är fel i ProductMenu.run()", e);
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- Produktmeny ---");
        System.out.println("1. Lägg till en produkt");
        System.out.println("2. Visa alla produkter");
        System.out.println("3. Ta bort en produkt");
        System.out.println("4. Gå tillbaka till huvudmenyn");
    }

    private void addProduct() {
        //skapa och spara ny produkt
        String id = input.getString("Skriv nytt produkt-ID: ");
        String name = input.getString("Skriv produktnamn: ");
        String category = input.getString("Skriv kategori: ");
        double price = input.getDouble("Skriv pris: ");

        try {
            // Skapa ny produkt
            Product newProduct = new Product(id, name, category, price);
            // Försök lägga till produkten i listan (returnerar false om ID redan finns)
            boolean added = orderService.addProduct(newProduct);

            if (added) {
                System.out.println("Produkten '" + name + "' är tillagd i '"
                        + category + "' och kostar " + price + " kr.");
                logger.info("Ny produkt tillagd i {}", category);
            } else {
                System.out.println("Fel: En produkt med produkt-ID " + id + " finns redan.");
                logger.warn("Försökte lägga till produkt ID {} som redan finns", id);
            }
        } catch (InvalidPriceException e) {
            System.out.println("Fel: " + e.getMessage());
            logger.warn("Misslyckades skapa produkt: {}", e.getMessage());
        }
    }

    private void displayProductsByCategory() {
        // Skriver ut produkter grupperade efter kategori
        System.out.println("\n--- Produkter per kategori ---");
        Map<String, List<Product>> grouped = analysisService.groupProductsByCategory();
        //Hämtar en Map (nyckeln är kategori och värdet produkterna i kategorin)
        grouped.forEach((category, productList) -> {
            //forEach ittera genom varje kategori och produktlista
            System.out.println(category + ":");
            productList.forEach(p -> System.out.println("   " + p));
            System.out.println();
        });
        logger.info("Visade alla produkter.");
    }

    private void removeProduct() {
        //ta bort en produkt med produktID
        System.out.println("\n--- Ta bort en produkt ---");
        //visar produkter och kategorier så det blir lättare att ta bort
        Map<String, List<Product>> grouped = analysisService.groupProductsByCategory();
        grouped.forEach((category, products) -> {
            System.out.println(category + ":");
            products.forEach(p -> System.out.println(p.getId() + " - " + p.getName()));
        });

        String productId = input.getString("\nSkriv produkt-ID för att ta bort produkten: ");
        boolean removed = orderService.removeProductById(productId);
        //försöker ta bort produkten
        if (removed)
        {
            System.out.println("Produkten har tagits bort.");
            logger.info("Produkten {} togs bort.", productId);
        }
        else
        {
            System.out.println("Produkten hittades inte.");
            logger.warn("Försökte att ta bort produkten {} som inte finns", productId);
        }
    }
}