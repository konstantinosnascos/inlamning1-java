package com.example;

import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalysisMenu {

    private final InputHelper input;
    private final AnalysisService analysisService;
    private static final Logger logger = LoggerFactory.getLogger(AnalysisMenu.class);

    public AnalysisMenu(Scanner scan, AnalysisService analysisService) {
        // Konstruktorn tar in Scanner och AnalysisService för input och analys
        this.input = new InputHelper(scan);
        //säker inmatning
        this.analysisService = analysisService;
        //analys
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = input.getInt("Välj ett alternativ: ");
            switch (choice) {
                case 1 -> displayProductsByCategoryThenPrice();// Grupp & sorteringsanalys
                case 2 -> displayCustomerTotalValue();//totalt kundvärde
                case 3 -> displayTopThreeProducts();// Topp 3 produkter
                case 4 -> running = false;// Tillbaka till huvudmeny
                default -> System.out.println("Felaktigt val, försök igen!");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- Analysmeny ---");
        System.out.println("1. Produkter per kategori (sorterade efter pris)");
        System.out.println("2. Total köpsumma för en kund");
        System.out.println("3. Topp 3 mest köpta produkter");
        System.out.println("4. Gå tillbaka till huvudmenyn");
    }

    // Visar produkter grupperade och sorterade efter pris
    private void displayProductsByCategoryThenPrice() {
        try {
            // Hämtar kategori-grupperade och pris-sorterade produkter
            Map<String, List<Product>> grouped = analysisService.groupProductByCategoryThenPrice();
            if (grouped.isEmpty()) {
                System.out.println("Inga produkter finns registrerade ännu.");
                return;
            }
            logger.info("Visade alla produkter, kategorier och priser");
            // Skriver ut kategori och produkter i stigande prisordning
            grouped.forEach((cat, prods) -> {
                System.out.println(cat + ":");
                prods.forEach(p -> System.out.println("  " + p));
            });
        } catch (Exception e) {
            // Fångar fel och loggar dem
            System.out.println("Ett fel uppstod vid hämtning av produkter: " + e.getMessage());
            logger.error("Fel i displayProductsByCategoryThenPrice", e);
        }
    }


    private void displayCustomerTotalValue() {
        // Visar total köpsumma för en specifik kund
        System.out.println("\n--- Total köpsumma för kund ---");
        String name = input.getString("Vad heter kunden? ");

        try {
            Map<String, List<Order>> customerOrders = analysisService.getCustomerOrders();
            logger.info("Visade all info för kund: {}", name);
            // Kontrollera att kunden finns
            if (!customerOrders.containsKey(name.toLowerCase())) {
                throw new CustomerNotFoundException("Kunden '" + name + "' har inga registrerade ordrar.");
            }

            // Beräkna totalt värde med stream
            double totalValue = analysisService.calculateCustomerTotalValue(name, customerOrders);
            System.out.println("Total köpsumma för " + name + ": " + totalValue + " kr");

        } catch (CustomerNotFoundException e) {
            System.out.println(e.getMessage());
            logger.warn("Kund saknas: {}", e.getMessage());
        } catch (EmptyOrderException e) {
            System.out.println("Kunden har en order utan produkter.");
            logger.warn("Tom order för kund: {}", name);
        } catch (Exception e) {
            System.out.println("Ett oväntat fel inträffade: " + e.getMessage());
            logger.error("Okänt fel i displayCustomerTotalValue", e);
        }

    }

    private void displayTopThreeProducts() {
        // Visar de 3 mest köpta produkterna
        System.out.println("\n--- Topp 3 mest köpta produkter ---");

        try {
            Map<String, List<Order>> customerOrders = analysisService.getCustomerOrders();

            if (customerOrders.isEmpty()) {
                //Kontrollera att det finns ordrar
                throw new EmptyOrderException("Det finns inga ordrar ännu.");
            }

            //Hämta topp 3 med stream  analysisService.findTopThreeProducts
            List<Map.Entry<String, Long>> topProducts = analysisService.findTopThreeProducts(customerOrders);

            if (topProducts.isEmpty()) {
                System.out.println("Inga produkter har köpts ännu.");
                return;
            }

            // Skriv ut topp 3
            int rank = 1;
            for (Map.Entry<String, Long> entry : topProducts) {
                System.out.println(rank + ". " + entry.getKey() + " – " + entry.getValue() + " st");
                rank++;
            }
            logger.info("Analys av topp 3-produkter gjord.");

        } catch (EmptyOrderException e) {
            System.out.println(e.getMessage());
            logger.warn("Inga ordrar fanns vid topp 3-analys.");
        } catch (Exception e) {
            System.out.println("Ett fel uppstod vid analys av topp 3-produkter: " + e.getMessage());
            logger.error("Fel i displayTopThreeProducts", e);
        }
    }
}