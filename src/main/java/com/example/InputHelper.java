package com.example;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputHelper {

    private final Scanner scan;
    private static final Logger logger = LoggerFactory.getLogger(InputHelper.class);

    // Konstruktor – tar emot en scanner från main-programmet
    public InputHelper(Scanner scan) {
        this.scan = scan;
    }

    // Läser in ett heltal från användaren
    public int getInt(String prompt) {
        try {
            while (true) {
                System.out.print(prompt);
                if (scan.hasNextInt()) {
                    int value = scan.nextInt();
                    scan.nextLine(); // rensar raden
                    return value;
                } else {
                    System.out.println("Fel input. Skriv ett heltal tack.");
                    logger.warn("Ogiltig inmatning – användaren skrev inte ett heltal.");
                    scan.next(); // rensa felaktig input
                }
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            System.out.println("Ett fel uppstod vid inläsning av tal: " + e.getMessage());
            logger.error("Fel i getInt(): ", e);
            return -1; // returnerar standardvärde om input misslyckas
        }
    }

    // Läser in ett flyttal (double) från användaren
    public double getDouble(String prompt) {
        try {
            while (true) {
                System.out.print(prompt);
                try {
                    double value = scan.nextDouble();
                    scan.nextLine(); // rensar raden
                    return value;
                } catch (InputMismatchException e) {
                    System.out.println("Fel input. Skriv ett nummer tack.");
                    logger.warn("Ogiltig inmatning av double: {}", e.getMessage());
                    scan.next(); // rensar felaktig input
                }
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            System.out.println("Ett fel uppstod vid inläsning av nummer: " + e.getMessage());
            logger.error("Fel i getDouble(): ", e);
            return 0.0; // standardvärde om input misslyckas
        }
    }

    // Läser in en sträng med validering
    public String getString(String prompt) {
        try {
            while (true) {
                System.out.print(prompt);
                String input = scan.nextLine().trim();
                if (!input.isEmpty()) {
                    return input;
                }
                System.out.println("Inmatningen får inte vara tom. Försök igen.");
                logger.warn("Användaren skrev en tom sträng som input.");
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            System.out.println("Ett fel uppstod vid inläsning av text: " + e.getMessage());
            logger.error("Fel i getString(): ", e);
            return ""; // return tom sträng om input misslyckas
        }
    }
}