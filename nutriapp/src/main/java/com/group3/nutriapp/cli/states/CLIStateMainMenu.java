package com.group3.nutriapp.cli.states;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;
import com.group3.nutriapp.cli.states.CLIStateCreateFood.FoodType;
import com.group3.nutriapp.model.Ingredient;
import com.group3.nutriapp.model.MaintainWeight;
import com.group3.nutriapp.model.Meal;
import com.group3.nutriapp.model.Recipe;
import com.group3.nutriapp.model.User;
import com.group3.nutriapp.persistence.UserFileDAO;
import com.group3.nutriapp.util.Crypto;

/**
 * @author Aidan Ruiz + Group 3
 * @description CLI state that handles the main interaction with the application
 * @date 4/11/23
 */
public class CLIStateMainMenu extends CLIState {
    public CLIStateMainMenu(CLI cli) { super(cli, "NutriApp"); }

    /**
     * Event that triggers when user opts to login.
     * 
     * Prompts the user for their username, if it exists, prompt for the password,
     * otherwise print error and return.
     * 
     * If password matches, log in and setup user object listeners,
     * otherwise print an error and return.
     */
    private void login() {
        CLI cli = getOwner();
        String username = getInput("Enter your username");
        UserFileDAO dao = cli.getUserDatabase();

        User user = dao.getUser(username);
        if (user == null) {
            showError("Username doesn't exist! Did you mean to register?");
            return;
        }

        String password = getInput("Enter your password");

        // We're sticking with simple SHA1 for password authentication.
        String hash = Crypto.makeSHA1(password);

        if (hash.equals(user.getPasswordHash())) {
            cli.setUser(user);
            showMessage("Successfully logged in!");
            return;
        }
    
        showError("Failed to login, password is incorrect!");
    }

    /**
     * Event that triggers when the user opts to register an account.
     * 
     * Prompts the user for a username to register with, if it exists, print an error and return.
     * If it doesn't, prompt the user for a password.
     * 
     * Once authentication details are setup, prompt the user for basic information about themselves,
     * including height, weight, and birthdate.
     * 
     * After gathering all details, persist the user to the database and login.
     * 
     * Starts the user with a default goal of maintaining their weight.
     */
    private void register() {
        CLI cli = getOwner();
        String username = getInput("Enter your username");
        User user = cli.getUserDatabase().getUser(username);
        if (user != null) {
            showError("Username already exists! Did you mean to login?");
            return;
        }

        String password = getInput("Enter your password");
        
        // Normally I'd do bcrypt or something similar, but I imagine
        // for this project, just a simple SHA1 is fine.
        String hash = Crypto.makeSHA1(password);

        // Now that we've gathered credentials, we need to get some basic account information
        // before we can actually create the user object.

        double height = getInputDouble("Enter your height");
        double weight = getInputDouble("Enter your weight");

        // Keep looping until the user provides a valid birthdate
        LocalDate birth = null;
        while (birth == null) {
            String input = getInput("Enter your birthdate (YYYY-MM-DD)");
            try { birth = LocalDate.parse(input); } 
            catch (DateTimeParseException ex) {
                showError("Not a valid date!");
                continue;
            }
        }

        // Create the user
        user = cli.getUserDatabase().addUser(username, height, weight, birth, hash);
        // Use maintain weight as a default goal
        user.setGoal(new MaintainWeight(weight));
        // Set the current user in the CLI
        cli.setUser(user);

        showMessage("Succesfully logged in!");
    }

    /**
     * Event that triggers when the user opts to logout.
     * 
     * Destroys the user session and stops the time manager.
     */
    public void logout() {
        getOwner().setUser(null);
        showMessage("Successfully logged out!");
    }

    /**
     * Shows any notifications that the user may currently have.
     * This may include pending invites to a team.
     */
    public void displayNotifications() {
        User user = getOwner().getUser();
        boolean hasNotification = false;

        if (user.hasPendingRequests()) {
            showLine("*  You've been invited to a team!");
            hasNotification = true;
        }

        // Only print an extra UI divider if we had a notification.
        if (hasNotification)
            showDivider(false);
    }

    /**
     * Sets up all the options that the user or guest
     * can select to use the NutriApp client.
     */
    @Override public void run() {
        // Privileged and guest users have different options,
        // so show each accordingly.
        boolean isGuest = !isAuthenticated();
        boolean isUser = !isGuest;

        CLI cli = getOwner();

        // Show any notifications that may exist
        if (isUser) displayNotifications();

        // If we're a guest, provide options to login/register
        if (isGuest) {
            addOption("Login", this::login);
            addOption("Register", this::register);
        } else {
            // Otherwise, if we're logged in, allow pushing profile and team menus
            addOption("My Profile", () ->  cli.push(new CLIStateMyProfile(cli)));
            addOption("My Team", () -> cli.push(new CLIStateMyTeam(cli)));
        }

        // Section for browsing foodstuffs
        addOptionDivider();
        {
            addOption("Ingredients", () -> {
                Ingredient[] ingredients = cli.getFoodDatabase().getIngredientArray();
                CLIStateSearchableTable.pushFoodSearchState(cli, "Ingredients", ingredients, true, null);
            });
    
            addOption("Recipes", () -> {
                Recipe[] recipes = cli.getFoodDatabase().getRecipeArray();
                CLIStateSearchableTable.pushFoodSearchState(cli, "Recipes", recipes, false, null);
            });
    
            addOption("Meals", () -> {
                Meal[] meals = cli.getFoodDatabase().getMealArray();
                CLIStateSearchableTable.pushFoodSearchState(cli, "Meals", meals, false, null);
            });
        }
        addOptionDivider();

        if (isUser) {
            addOption("Create Recipe", () -> {
                String name = getInput("Enter name for recipe meal");
                if (name.isEmpty()) {
                    showError("Recipe name cannot be empty");
                    return;
                }
                
                if (cli.getFoodDatabase().findRecipe(name) != null) {
                    this.showError("Recipe already exists!");
                    return;
                }

                cli.push(new CLIStateCreateFood(cli, name, FoodType.RECIPE));
            });

            addOption("Create Meal", () -> {
                String name = getInput("Enter name for your meal");
                if (name.isEmpty()) {
                    showError("Meal name cannot be empty");
                    return;
                }

                if (cli.getFoodDatabase().findMeal(name) != null) {
                    this.showError("Meal already exists!");
                    return;
                }

                cli.push(new CLIStateCreateFood(cli, name, FoodType.MEAL));
            });
            
            addOptionDivider();
        }

        // For debugging purposes, allow users to set the length of a day.
        if (isUser) {
            addOption("Set Day Length", () -> {
                double seconds = getInputDouble("Enter day length in seconds");
                int ms = (int) Math.round(seconds * 1000.0);
                cli.getTimeManager().setDayLength(ms);
                showMessage("Successfully set day length!");
            });
            addOptionDivider();
        }

        if (isUser) 
            addOption("Logout", this::logout);

        addOption("Quit", () -> cli.quit());
    }
}
