package com.dss56.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private static Scanner sc = new Scanner(System.in);
    private List<String> options;
    private List<PreCondition> available;
    private List<Handler> handlers;
    private final boolean isWindows;


    public interface Handler {
        public void execute();
    }

    public void setHandler(int i, Handler h) {
        this.handlers.set(i, h);
    }

    public interface PreCondition {
        public boolean validate();
    }

    public void setPreCondition(int i, PreCondition condition) {
        this.available.set(i,condition);
    }

    public Menu(String[] options) {
        this.options = Arrays.asList(options);
        this.available = new ArrayList<>();
        this.handlers = new ArrayList<>();

        String os = System.getProperty("os.name").toLowerCase();
        this.isWindows = os.contains("win");

        this.options.forEach(s-> {
            this.available.add(()->true);
            this.handlers.add(()->System.out.println("\nThis function comes in next DLC!"));
        });
    }

    private void show() {
        System.out.println("\n --- Schedule Generator --- ");

        for (int i=1; i<this.options.size(); i++) {
            System.out.print(i);
            System.out.print(" - ");
            System.out.println(this.available.get(i).validate()?this.options.get(i):"---");
        }
        System.out.print(0);
        System.out.print(" - ");
        System.out.println(this.available.getFirst().validate()?this.options.getFirst():"---");
    }

    private int readOption() {
        int option;
        System.out.print("Option: ");

        try {
            String line = sc.nextLine();
            option = Integer.parseInt(line);
        }
        catch (NumberFormatException e) {
            option = -1;
        }

        if (option<0 || option>this.options.size()) {
            System.out.println("Invalid option! Try again.!!!");
            option = -1;
        }
        return option;
    }

    public void run() {
        int option;

        do {
            this.show();
            option = readOption();

            if (option>0 && !this.available.get(option).validate()) {
                System.out.println("Option unavailable! Try again.");
            } else if (option>0) {
                this.handlers.get(option).execute();
            }
        } while (option != 0);

        this.handlers.get(option).execute();
    }

    public void clearScreen() {
        try {

            if (this.isWindows) {
                // Verificar se estamos no Git Bash ou outro terminal Unix-like no Windows
                String term = System.getenv("TERM");

                if (term != null && term.contains("xterm")) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                } else {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                }
            } else {
                // Sistemas Unix-based (Linux/Mac)
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Unable to clear screen.");
        }
    }
}