package com.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Programstart: Main.main() körs - applikationen initieras.");
        new Application().run();
        logger.info("Programmet har avslutats.");
    }
}