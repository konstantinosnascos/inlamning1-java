package com.example;

import java.util.List;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
    private final Scanner scan = new Scanner(System.in);
    private final OrderService orderService = new OrderService();
    private final AnalysisService analysisService;
    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private final MenuHandler menuHandler;
    public Application() {
        try {
            // Försök skapa testdata (produkter)
            orderService.addProduct(new Product("p1", "Kaffe", "Dryck", 49));
            orderService.addProduct(new Product("p2", "Mjölk", "Dryck", 29));
            orderService.addProduct(new Product("p3", "Bröd", "Mat", 39));
            orderService.addProduct(new Product("p4", "Smör", "Mat", 45));
            orderService.addProduct(new Product("p5", "Ost", "Mejeri", 59));
            orderService.addProduct(new Product("p6", "Apelsinjuice", "Dryck", 35));
            orderService.addProduct(new Product("p7", "Yoghurt", "Mejeri", 25));
            orderService.addProduct(new Product("p8", "Ägg", "Mat", 22));

            // Försök skapa ordrar med produkter
            List<Product> annasProducts = List.of(
                    orderService.findProductById("p1"),
                    orderService.findProductById("p3"),
                    orderService.findProductById("p2"));
            orderService.addOrder(new Order("order1", annasProducts, "Anna"));

            List<Product> bobProducts = List.of(
                    orderService.findProductById("p3"),
                    orderService.findProductById("p4"),
                    orderService.findProductById("p1"));
            orderService.addOrder(new Order("order2", bobProducts, "Bob"));

            List<Product> claraProducts = List.of(
                    orderService.findProductById("p1"),
                    orderService.findProductById("p6"),
                    orderService.findProductById("p2"),
                    orderService.findProductById("p2"));
            orderService.addOrder(new Order("order3", claraProducts, "Clara"));

            List<Product> annaSecondOrder = List.of(
                    orderService.findProductById("p5"),
                    orderService.findProductById("p7"),
                    orderService.findProductById("p8"));
            orderService.addOrder(new Order("order4", annaSecondOrder, "Anna"));

            // Logga att testdata lades till
            logger.info("Testdata (produkter och ordrar) har lagts till för snabb testning.");

        } catch (InvalidPriceException e) {
            // Misslyckades skapa produkt med ogiltigt pris
            System.out.println("Kunde inte initiera produkter: " + e.getMessage());
            logger.error("Fel vid initiering av produktdata", e);
        } catch (NullPointerException e) {
            //produkt-ID hittades inte
            System.out.println("Ett datafel inträffade vid skapande av testordrar.");
            logger.error("Testdata innehöll null-värden: {} ", e.getMessage());
        } catch (Exception e) {
            // Fångar eventuella andra, oförutsedda fel
            System.out.println("Ett oväntat fel inträffade vid initialisering: " + e.getMessage());
            logger.error("Oväntat fel vid initialisering av testdata", e);
        }

        // Skapa analysservice och menyer även om testdata misslyckas
        this.analysisService = new AnalysisService(orderService);
        this.menuHandler = new MenuHandler(scan, orderService, analysisService);
    }

    public void run() {
        //start/finish
        menuHandler.runMainMenu();
        scan.close();
        logger.info("Programmet avslutas och Scanner stängs.");
    }
}
