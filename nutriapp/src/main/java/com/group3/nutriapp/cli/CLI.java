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
     * Whether or not to disable clearing the console
     * when using the CLI.
     */
    public static final boolean DISABLE_FLUSH = false;

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
     * Persistent user team storage.
     */
    private TeamFileDAO teamDAO;

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

    public CLI(FoodFileDAO foodDAO, UserFileDAO userDAO, HistoryFileDAO historyDAO, TeamFileDAO teamDAO) {
        this.foodDAO = foodDAO;
        this.userDAO = userDAO;
        this.historyDAO = historyDAO;
        this.teamDAO = teamDAO;

        // Push default state onto stack
        this.push(new CLIStateMainMenu(this));
    }

    public FoodFileDAO getFoodDatabase() { return this.foodDAO; }
    public UserFileDAO getUserDatabase() { return this.userDAO; }
    public HistoryFileDAO getHistoryDatabase() { return this.historyDAO; }
    public TeamFileDAO getTeamDatabase() { return this.teamDAO; }
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
     * Clears the console
     */
    public void clear() {
        if (!CLI.DISABLE_FLUSH) {
            // Resets the console, sourced from https://stackoverflow.com/questions/2979383/how-to-clear-the-console
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    /**
     * Runs the main CLI execution loop.
     */
    public void run() {
        while (this.isRunning) {
            this.clear();

            // Load whatever state's at the top of the stack
            CLIState state = this.stack.peek();

            // Clear the previous frame's options
            state.clearOptions();
            
            // Print the name of the state to the console
            state.showHeader();

            // Do any per frame processing/setup
            state.run();

            // Print out the options that were setup by the state
            state.showMenu();

            // Prompt the user for input
            state.trySelectOption();
        }
    }


}
