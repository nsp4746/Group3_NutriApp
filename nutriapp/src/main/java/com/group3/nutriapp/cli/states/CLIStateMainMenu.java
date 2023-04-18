package com.group3.nutriapp.cli.states;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import com.group3.nutriapp.Control.Observer;
import com.group3.nutriapp.Control.TimeManager;
import com.group3.nutriapp.Control.WeightObserver;
import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;
import com.group3.nutriapp.cli.states.CLIStateCreateFood.FoodType;
import com.group3.nutriapp.model.Ingredient;
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

    private TimeManager manager;

    public CLIStateMainMenu(CLI cli) { super(cli, "NutriApp"); }

    private void login() {
        String username = this.getInput("Enter your username");

        User user = this.getOwner().getUserDatabase().getUser(username);
        if (user == null) {
            this.showError("Username doesn't exist! Did you mean to register?");
            return;
        }

        String password = this.getInput("Enter your password");
        String hash = Crypto.makeSHA1(password);

        if (hash.equals(user.getPasswordHash())) {
            this.getOwner().setUser(user);
            this.showMessage("Successfully logged in!");
            Observer observer = new WeightObserver(this.getOwner().getUserDatabase(), user); // add weight observer    
            manager = new TimeManager(user, this.getOwner().getHistoryDatabase(), LocalDateTime.now(), 24*3600); 
            user.registerObserver(observer);
            return;
        }
    
        this.showError("Failed to login, password is incorrect!");
    }

    private void register() {
        String username = this.getInput("Enter your username");
        User user = this.getOwner().getUserDatabase().getUser(username);
        if (user != null) {
            this.showError("Username already exists! Did you mean to login?");
            return;
        }

        String password = this.getInput("Enter your password");
        
        // Normally I'd do bcrypt or something similar, but I imagine
        // for this project, just a simple SHA1 is fine.
        String hash = Crypto.makeSHA1(password);

        // Now that we've gathered credentials, we need to get some basic account information
        // before we can actually create the user object.

        double height = this.getInputDouble("Enter your height");
        double weight = this.getInputDouble("Enter your weight");

        // Keep looping until the user provides a valid birthdate
        LocalDate birth = null;
        while (birth == null) {
            String input = this.getInput("Enter your birthdate (YYYY-MM-DD)");
            try { birth = LocalDate.parse(input); } 
            catch (DateTimeParseException ex) {
                this.showError("Not a valid date!");
                continue;
            }
        }

        // Create the user
        user = this.getOwner().getUserDatabase().addUser(username, height, weight, birth, hash);
        // Set the current user in the CLI
        this.getOwner().setUser(user);

        this.showMessage("Succesfully logged in!");
    }

    @Override public void run() {
        // Privileged and guest users have different options,
        // so show each accordingly.
        boolean isGuest = !this.isAuthenticated();
        boolean isUser = !isGuest;

        CLI cli = this.getOwner();

        // Show any notifications that may exist
        if (isUser) {
            if (cli.getUser().hasPendingRequests()) {
                this.showLine("*  You've been invited to a team!");
                this.showDivider(false);
            }
        }

        // If we're a guest, provide options to login/register
        if (isGuest) {
            addOption("Login", this::login);
            addOption("Register", this::register);
        } else {
            // Otherwise, if we're logged in, allow pushing profile and team menus
            addOption("My Profile", () -> 
                cli.push(new CLIStateMyProfile(cli))
            );

            addOption("My Team", () -> {
                cli.push(new CLIStateMyTeam(cli));
            });
        }

        // Section for browsing foodstuffs
        addOptionDivider();
        {
            addOption("Ingredients", () -> {
                Ingredient[] ingredients = cli.getFoodDatabase().getIngredientArray();
                CLIStateSearchableTable.pushFoodSearchState(this.getOwner(), "Ingredients", ingredients, true, null);
            });
    
            addOption("Recipes", () -> {
                Recipe[] recipes = cli.getFoodDatabase().getRecipeArray();
                CLIStateSearchableTable.pushFoodSearchState(this.getOwner(), "Recipes", recipes, false, null);
            });
    
            addOption("Meals", () -> {
                Meal[] meals = cli.getFoodDatabase().getMealArray();
                CLIStateSearchableTable.pushFoodSearchState(this.getOwner(), "Meals", meals, false, null);
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

                cli.push(new CLIStateCreateFood(cli, name, FoodType.RECIPE));
            });

            addOption("Create Meal", () -> {
                String name = getInput("Enter name for your meal");
                if (name.isEmpty()) {
                    showError("Meal name cannot be empty");
                    return;
                }

                cli.push(new CLIStateCreateFood(cli, name, FoodType.MEAL));
            });
            
            addOptionDivider();
        }

        if (isUser) {
            addOption("Logout", () -> {
                cli.setUser(null);
                showMessage("Successfully logged out!");
                manager.cancel();
                
            });
        }

        addOption("Quit", () -> cli.quit());
    }
}
