package com.group3.nutriapp.cli;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Aidan Ruiz + Group- 3
 * @description Draws and performs logic for a command line state.
 * @date 04/11/2023
 */
public abstract class CLIState {
    /**
     * The command line interface that owns this state.
     */
    private CLI owner;

    /**
     * The title of this state.
     */
    private String title;

    /**
     * The width of the table to print.
     */
    private int tableWidth = 40;

    /**
     * The list of menu options available in this state.
     */
    private ArrayList<CLIMenuOption> options = new ArrayList<>();

    /**
     * The list of indices to display dividers at in this state.
     */
    private ArrayList<Integer> dividers = new ArrayList<>();
    
    /**
     * Creates a new command line interface state.
     * @param owner
     */
    protected CLIState(CLI owner, String title) { 
        this.owner = owner;
        this.title = title;
    }

    protected CLI getOwner() { return this.owner; }
    protected void setTableWidth(int width) {
        this.tableWidth = width;
    }

    /**
     * Adds a new option to the menu for this state.
     * @param name Name of the option to show on the console
     * @param callback Function that gets run when this option is selected
     */
    protected void addOption(String name, Runnable callback) {
        this.options.add(new CLIMenuOption(name, callback));
    }

    /**
     * Adds a divider at the end of the last added option.
     */
    protected void addOptionDivider() {
        this.dividers.add(this.options.size() - 1);
    }

    /**
     * Clears all options and dividers for this state.
     */
    protected void clearOptions() {
        this.options.clear();
        this.dividers.clear();
    }

    /**
     * Adds the default back command
     */
    protected void addBackOption() {
        this.options.add(new CLIMenuOption("Back", () -> {
            this.owner.pop();
        }));
    }

    /**
     * Gets whether or not we're currently signed in as a user.
     * @return Authentication status
     */
    protected boolean isAuthenticated() {
        // If the current session doesn't have a user, we aren't authenticated.
        return this.owner.getUser() != null;
    }

    /**
     * Prints a query and gets a line from user input stream.
     * @param query Question to ask the user
     * @return User input
     */
    protected String getInput(String query) {
        System.out.print(String.format("[?] %s: ", query));
        return this.owner.getScanner().nextLine();
    }

    /**
     * Prints a query and gets a double from user input stream/
     * @param Query Question to ask the user
     * @return User input
     */
    protected double getInputDouble(String query) {
        // Keep looping until user provides valid input
        while (true) {
            String input = this.getInput(query);
            try { return Double.parseDouble(input); } 
            catch (NumberFormatException ex) {
                this.showError("Not a valid number!");
                continue;
            }
        }
    }

    /**
     * Prompts the user for an option and returns the index selected.
     * @return The index of the option the user selected
     */
    protected int getOptionIndex() {
        try { 
            return Integer.parseInt(this.getInput("Please select a choice")); 
        }
        catch (Exception ex) {
            this.showError("Invalid command!");
            return - 1;
        }
    }

    /**
     * Prompts the user to select an option, then runs it, if possible.
     */
    public void trySelectOption() {
        int command = this.getOptionIndex();
        if (command == -1) return;
        
        if (command >= this.options.size() || command < 0) {
            this.showError("Invalid command!");
            return;
        }

        Runnable callback = this.options.get(command).getCallback();
        if (callback != null)
            callback.run();
    }

    /**
     * Displays an error message and waits for the user to confirm.
     * @param message Error message to display.
     */
    protected void showError(String message) {
        System.out.println("[!] " + message);
        System.out.println("[*] Press enter to continue...");
        this.owner.getScanner().nextLine();
    }

    /**
     * Display a message and waits for the user to confirm
     * @param message Message to display
     */
    protected void showMessage(String message) {
        System.out.println("[*] " + message);
        System.out.println("[*] Press enter to continue...");
        this.owner.getScanner().nextLine();
    }

    /**
     * Prints a bordered line with centered text.
     * @param text Text to display
     */
    protected void showLineCentered(String text) {
        // Using a string builder to center the title text within the header.
        StringBuilder sb = new StringBuilder(this.tableWidth);
        for (int i = 0; i < this.tableWidth; i++) sb.append(" ");

        // Insert the string at the center.
        int start = (this.tableWidth - text.length()) / 2; 
        sb.replace(start, start + text.length(), text);

        System.out.println(String.format("|%s|", sb.toString()));
    }

    /**
     * Prints a bordered line with text.
     * @param text Text to display
     */
    protected void showLine(String text) {
        StringBuilder sb = new StringBuilder(this.tableWidth);
        for (int i = 0; i < this.tableWidth; i++) sb.append(" ");

        sb.replace(1, text.length() + 1, text);

        System.out.println(String.format("|%s|", sb.toString()));
    }

    /**
     * Prints a divider
     * @param border Whether or not to include a side border
     */
    protected void showDivider(boolean border) {
        if (border) {
            System.out.println(
                "| " + 
                String.join("", Collections.nCopies(this.tableWidth - 2, "-"))
                + " |"
            );
        } else {
            System.out.println(String.join("", Collections.nCopies(this.tableWidth + 2, "-")));
        }
    }

    /**
     * Shows the title of the current state in a frame.
     */
    public void showHeader() {
        this.showDivider(false);
        this.showLineCentered(this.title);
        this.showDivider(false);
    }

    /**
     * Displays a menu with options.
     */
    protected void showMenu() {
        // For when a divider is added when no options are present.
        if (this.dividers.contains(-1)) this.showDivider(true);

        for (int i = 0; i < this.options.size(); i++) {
            CLIMenuOption option = this.options.get(i);

            this.showLine(String.format("%d. %s", i, option.getName()));

            if (this.dividers.contains(i))
                this.showDivider(true);
        }

        this.showDivider(false);
    }

    /**
     * Main loop that gets executed when this state is focused.
     */
    public abstract void run();
}
