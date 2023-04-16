package com.group3.nutriapp.cli;

public class CLIMenuOption {
    /**
     * The name of the menu option to display.
     */
    private String name;

    /**
     * The function that gets run when this option is selected.
     */
    private Runnable callback;

    public CLIMenuOption(String name, Runnable callback) {
        this.name = name;
        this.callback = callback;
    }

    public String getName() { return this.name; }
    public Runnable getCallback() { return this.callback; }
}
