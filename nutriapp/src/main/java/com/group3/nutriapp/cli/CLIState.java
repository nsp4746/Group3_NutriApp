package com.group3.nutriapp.cli;

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
    private int tableWidth = 30;
    
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
    protected void showHeader() {
        this.showDivider(false);
        this.showLineCentered(this.title);
        this.showDivider(false);
    }

    /**
     * Displays a menu with options.
     * @param options A list of options available in the menu
     * Use $DIVIDER to organize options into subsections.
     */
    protected void showMenu(String[] options) {
        for (int i = 0, j = 0; i < options.length; ++i, ++j) {

            // Used for organization
            if (options[i].equals("$DIVIDER")) {
                this.showDivider(true);
                j--;
                continue;
            }

            this.showLine(String.format("%d. %s", j, options[i]));
        }
        
        this.showDivider(false);
    }

    /**
     * Main loop that gets executed when this state is focused.
     */
    public abstract void run();
}
