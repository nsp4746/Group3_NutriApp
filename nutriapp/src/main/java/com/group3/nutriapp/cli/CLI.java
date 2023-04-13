package com.group3.nutriapp.cli;

import java.util.Scanner;
import java.util.Stack;

import com.group3.nutriapp.persistence.*;
import com.group3.nutriapp.cli.states.CLIStateMainMenu;
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
     * Currently logged in user.
     */
    private User user;

    /**
     * User input stream.
     */
    private Scanner scanner = new Scanner(System.in);

    /**
     * CLI menu stack.
     */
    private Stack<CLIState> stack = new Stack<>();

    /**
     * Whether or not the CLI is currently running.
     */
    private boolean isRunning = true;

    // private TimeManager timeManager;

    public CLI(FoodFileDAO foodDAO, UserFileDAO userDAO, HistoryFileDAO historyDAO) {
        this.foodDAO = foodDAO;
        this.userDAO = userDAO;
        this.historyDAO = historyDAO;

        // Push default state onto stack
        this.push(new CLIStateMainMenu(this));
    }

    public FoodFileDAO getFoodDatabase() { return this.foodDAO; }
    public UserFileDAO getUserDatabase() { return this.userDAO; }
    public HistoryFileDAO getHistoryDatabase() { return this.historyDAO; }
    public User getUser() { return this.user; }
    public Scanner getScanner() { return this.scanner; }

    public void setUser(User user) { this.user = user; }

    /**
     * Pushes a new state to the CLI.
     * @param state State to push
     */
    public void push(CLIState state) {
        this.stack.add(state);
    }

    /**
     * Pops the most recent state from the CLI.
     */
    public void pop() {
        this.stack.pop();
    }

    /**
     * Stops the program execution loop.
     */
    public void quit() {
        this.isRunning = false;
    }

    /**
     * Runs the main CLI execution loop.
     */
    public void run() {
        while (this.isRunning) {
            // Resets the console, sourced from https://stackoverflow.com/questions/2979383/how-to-clear-the-console
            System.out.print("\033[H\033[2J");
            System.out.flush();

            // Run whatever's at the top of the stack.
            this.stack.peek().run();
        }
    }


}
