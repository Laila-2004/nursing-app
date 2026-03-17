package com.nursing.menu;

import java.util.Scanner;

public class ConsoleUtils {

    public static final String RESET  = "\u001B[0m";
    public static final String BOLD   = "\u001B[1m";
    public static final String CYAN   = "\u001B[36m";
    public static final String GREEN  = "\u001B[32m";
    public static final String RED    = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE   = "\u001B[34m";

    private ConsoleUtils() {}

    public static void ligne() {
        System.out.println(CYAN + "─".repeat(60) + RESET);
    }

    public static void titre(String texte) {
        System.out.println();
        ligne();
        System.out.println(BOLD + CYAN + "  " + texte + RESET);
        ligne();
    }

    public static void succes(String msg) {
        System.out.println(GREEN + "✔  " + msg + RESET);
    }

    public static void erreur(String msg) {
        System.out.println(RED + "✘  " + msg + RESET);
    }

    public static void info(String msg) {
        System.out.println(YELLOW + "ℹ  " + msg + RESET);
    }

    public static String lire(Scanner sc, String prompt) {
        System.out.print(BOLD + prompt + RESET + " ");
        return sc.nextLine().trim();
    }

    public static int lireInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(BOLD + prompt + RESET + " ");
            String input = sc.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                erreur("Veuillez entrer un nombre valide.");
            }
        }
    }

    public static void pauseEntree(Scanner sc) {
        System.out.print("\n" + YELLOW + "[ Appuyez sur Entrée pour continuer ]" + RESET);
        sc.nextLine();
    }
}
