package com.example;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MenuHandler {

    private final InputHelper input;
    private final ProductMenu productMenu;
    private final OrderMenu orderMenu;
    private final AnalysisMenu analysisMenu;
    private static final Logger logger = LoggerFactory.getLogger(MenuHandler.class);

    public MenuHandler(Scanner input, OrderService orderService, AnalysisService analysisService) {
        //Konstruktorn tar in scanner, etc.
        this.input = new InputHelper(input);//input
        this.productMenu = new ProductMenu(input, orderService, analysisService);//alla olika menyklasser
        this.orderMenu = new OrderMenu(input, orderService);
        this.analysisMenu = new AnalysisMenu(input, analysisService);
    }

    public void runMainMenu() {
        boolean running = true;
        while (running) {
            try {
                printMainMenu();
                int choice = input.getInt("Välj ett alternativ: ");
                switch (choice) {
                    case 1 -> productMenu.run();//hitta rätt meny att gå till
                    case 2 -> orderMenu.run();
                    case 3 -> analysisMenu.run();
                    case 4 -> running = false;
                    default -> System.out.println("Felaktigt val, försök igen!");
                }
            } catch (Exception e) {
                System.out.println("Ett oväntat fel uppstod i huvudmenyn: " + e.getMessage());
                logger.error("Fel i huvudmenyn (runMainMenu)", e);
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\n--- Huvudmeny ---");
        System.out.println("1. Produktmeny");
        System.out.println("2. Ordermeny");
        System.out.println("3. Analysmeny");
        System.out.println("4. Avsluta programmet");
    }
}