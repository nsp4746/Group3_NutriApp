package com.group3.nutriapp;

import java.util.Scanner;

import com.group3.nutriapp.persistence.*;
import com.group3.nutriapp.model.*;

/**
 * @author Aidan Ruiz + Group 3
 * @description Handles all application interactions via command line.
 * @date 2/26/2023
 */
public class CLI {
    /**
     * Persistent food storage.
     */
    private FoodFileDAO foodDAO;

    /**
     * Persistent user storage.
     */
    private UserFileDAO userDAO;

    /**
     * Persistent user history storage.
     */
    private HistoryFileDAO historyDAO;

    /**
     * User input stream.
     */
    private Scanner scanner = new Scanner(System.in);

    // private TimeManager timeManager;

    public CLI(FoodFileDAO foodDAO, UserFileDAO userDAO, HistoryFileDAO historyDAO) {
        this.foodDAO = foodDAO;
        this.userDAO = userDAO;
        this.historyDAO = historyDAO;
    }


    /**
     * Resets the console and displays a menu with options.
     * @param options A list of options available in the menu
     * Use $DIVIDER to organize options into subsections.
     */
    private void displayMainMenu(String[] options) {

        // Resets the console, sourced from https://stackoverflow.com/questions/2979383/how-to-clear-the-console
        System.out.print("\033[H\033[2J");  
        System.out.flush();

        System.out.println("--------------------------------");
        System.out.println("|          NutriApp            |");
        System.out.println("--------------------------------");

        for (int i = 0, j = 0; i < options.length; ++i, ++j) {

            // Used for organization
            if (options[i].equals("$DIVIDER")) {
                System.out.println("| ---------------------------  |");
                j--;
                continue;
            }

            String padding = j > 9 ? "25" : "26"; // Hack to get around spacing issues after 10 options.
            System.out.println(String.format("| %d. %-" + padding + "s|", j, options[i]));
        }
        
        System.out.println("--------------------------------");
    }

    /**
     * Displays an error message and waits for the user to confirm.
     * @param message Error message to display.
     */
    private void displayError(String message) {
        System.out.println("[!] " + message);
        System.out.println("[*] Press enter to continue...");
        this.scanner.nextLine();
    }

    /**
     * Runs the main CLI execution loop.
     */
    public void run() {
        final String[] options = {
            "My Profile",
            "My Goals",
            "My Recipes",
            "My Meals",
            "My Workouts",
            "My History",
            "$DIVIDER",
            "Create a Shopping List",
            "$DIVIDER",
            "Ingredients",
            "$DIVIDER",
            "Adjust Daylight Cycle",
            "Quit"
        };

        // Might be better to use something like an enum 
        // or abstract classes for each command for organization
        // but shouldn't matter too much? It's a relatively small application.

        final int COMMAND_MY_PROFILE = 0;
        final int COMMAND_MY_GOALS = 1;
        final int COMMAND_MY_RECIPES = 2;
        final int COMMAND_MY_MEALS = 3;
        final int COMMAND_MY_WORKOUTS = 4;
        final int COMMAND_MY_HISTORY = 5;
        final int COMMAND_SHOPPING_LIST = 6;
        final int COMMAND_INGREDIENTS = 7;
        final int COMMAND_ADJUST_DAY_SPEED = 8;
        final int COMMAND_QUIT = 9;

        boolean isRunning = true;
        while (isRunning) {

            this.displayMainMenu(options);
            System.out.print("[?] Please enter your choice: ");
            
            int command;
            try { command = Integer.parseInt(this.scanner.nextLine()); }
            catch (Exception ex) {
                this.displayError("Invalid command!");
                continue;
            }

            switch (command) {
                case COMMAND_QUIT: {
                    isRunning = false;
                    break;
                }
                default: {
                    this.displayError("Unhandled command!");
                    break;
                }
            }
        }
    }


}
