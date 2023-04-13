package com.group3.nutriapp.cli.states;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;
import com.group3.nutriapp.model.Food;
import com.group3.nutriapp.model.Ingredient;
import com.group3.nutriapp.model.Meal;
import com.group3.nutriapp.model.Recipe;
import com.group3.nutriapp.model.User;

/**
 * @author Aidan Ruiz + Group 3
 * @description CLI state that handles the main interaction with the application
 * @date 4/11/23
 */
public class CLIStateMainMenu extends CLIState {
    public CLIStateMainMenu(CLI cli) { super(cli, "NutriApp"); }

    /**
     * Option indices and strings for user menu.
     */
    private static class UserOptions {
        private static final String[] MENU = {
            "My Profile",
            "My Team",
            "$DIVIDER",
            "Create a Shopping List",
            "$DIVIDER",
            "Ingredients",
            "Recipes",
            "Meals",
            "$DIVIDER",
            "Adjust Daylight Cycle",
            "$DIVIDER",
            "Logout",
            "Quit"
        };

        private static final int MY_PROFILE = 0;
        private static final int MY_TEAM = 1;
        private static final int CREATE_SHOPPING_LIST = 2;
        private static final int INGREDIENTS = 3;
        private static final int RECIPES = 4;
        private static final int MEALS = 5;
        private static final int ADJUST_DAYLIGHT_CYCLE = 6;
        private static final int LOGOUT = 7;
        private static final int QUIT = 8;
    }

    /**
     * Option indices and strings for guest menu.
     */
    private static class GuestOptions {
        private static final String[] MENU = {
            "Login",
            "Register",
            "$DIVIDER",
            "Ingredients",
            "Recipes",
            "Meals",
            "$DIVIDER",
            "Quit"
        };

        private static final int LOGIN = 0;
        private static final int REGISTER = 1;
        private static final int INGREDIENTS = 2;
        private static final int RECIPES = 3;
        private static final int MEALS = 4;
        private static final int QUIT = 5;
    }

    /**
     * Returns the SHA1 digest of a given string
     * @param text String to compute hash of
     * @return Computed hash in lowercase string
     */
    private String makeSHA1(String text) {
        // Compute the hash
        MessageDigest hasher;
        try { hasher = MessageDigest.getInstance("SHA-1"); }
        catch (NoSuchAlgorithmException ex) { return null; }
        byte[] digest = hasher.digest(text.getBytes(StandardCharsets.US_ASCII));

        // Convert hash to string
        final char[] HEX_DIGITS = ("0123456789abcdef".toCharArray());
        final char[] hex = new char[digest.length * 2];
        for (int i = 0; i < digest.length; ++i) {
            int b = digest[i] & 0xFF;
            hex[i * 2] = HEX_DIGITS[b >>> 4];
            hex[(i * 2) + 1] = HEX_DIGITS[b & 0xF];
        }

        // Return the computed string
        return String.valueOf(hex);
    }

    /**
     * Pushes a searchable food table state onto the stack using specified array of foodstuff.
     * @param name The name of the table
     * @param food The array of foodstuff
     * @param isIngredientArray Whether or not this array contains ingredients
     */
    private void pushFoodSearchState(String name, Food[] food, boolean isIngredientArray) {
        // A bit nasty to do it like this, but I don't see any reason to have to copy/paste the table generation logic.
        // Ingredients have stock, the rest of the foodstuff does not, other than that all properties are the same.
        String[] headers;
        if (isIngredientArray)
            headers = new String[] { "Name", "Calories", "Protein", "Carbs", "Stock" };
        else
            headers = new String[] { "Name", "Calories", "Protein", "Carbs" };
        String[][] values = new String[food.length][];
        for (int i = 0; i < food.length; i++) {
            Food item = food[i];

            // Are we missing fat and fiber?
            values[i] = new String[] {
                item.getName(),
                Double.toString(item.getCalories()),
                Double.toString(item.getProtein()),
                Double.toString(item.getCarbs()),

                // Only ingredients have stock
                isIngredientArray ? Integer.toString(((Ingredient) item).getStockCount()) : ""
            };
        }

        // Push the state
        this.getOwner().push(new CLIStateSearchableTable(
            this.getOwner(), 
            name, 
            headers, 
            values
        ));
    }

    /**
     * Handles commands for when no user is currently logged in, guest mode.
     * @param command Command that user entered
     */
    private void handleGuestCommand(int command) {
        switch (command) {
            case GuestOptions.LOGIN: {
                String username = this.getInput("Enter your username");

                User user = this.getOwner().getUserDatabase().getUser(username);
                if (user == null) {
                    this.showError("Username doesn't exist! Did you mean to register?");
                    break;
                }

                String password = this.getInput("Enter your password");
                String hash = this.makeSHA1(password);

                if (hash.equals(user.getPasswordHash())) {
                    this.getOwner().setUser(user);
                    this.showMessage("Successfully logged in!");
                    break;
                }
            
                this.showError("Failed to login, password is incorrect!");

                break;
            }
            case GuestOptions.REGISTER: {
                String username = this.getInput("Enter your username");
                User user = this.getOwner().getUserDatabase().getUser(username);
                if (user != null) {
                    this.showError("Username already exists! Did you mean to login?");
                    break;
                }

                String password = this.getInput("Enter your password");
                
                // Normally I'd do bcrypt or something similar, but I imagine
                // for this project, just a simple SHA1 is fine.
                String hash = this.makeSHA1(password);

                // Now that we've gathered credentials, we need to get some basic account information
                // before we can actually create the user object.

                double height = this.getInputDouble("Enter your height");
                double weight = this.getInputDouble("Enter your weight");

                // Keep looping until the user provides a valid birthdate
                LocalDate birth = null;
                while (birth == null) {
                    String input = this.getInput("Enter your birthdate");
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

                break;
            }
            case GuestOptions.INGREDIENTS: {
                Ingredient[] ingredients = this.getOwner().getFoodDatabase().getIngredientArray();
                this.pushFoodSearchState("Ingredients", ingredients, true);
                break;
            }
            case GuestOptions.RECIPES: {
                Recipe[] recipes = this.getOwner().getFoodDatabase().getRecipeArray();
                this.pushFoodSearchState("Recipes", recipes, false);
                break;
            }
            case GuestOptions.MEALS: {
                Meal[] meals = this.getOwner().getFoodDatabase().getMealArray();
                this.pushFoodSearchState("Meals", meals, false);
                break;
            }
            case GuestOptions.QUIT: {
                this.getOwner().quit();
                break;
            }
            default: {
                this.showError("Invalid command!");
                break;
            }
        }
    }

    /**
     * Handles commands for when a user is logged in.
     * @param command Command that user entered
     */
    private void handleUserCommand(int command) {
        switch (command) {
            case UserOptions.MY_PROFILE: {
                this.getOwner().push(new CLIStateMyProfile(this.getOwner()));
                break;
            }
            case UserOptions.INGREDIENTS: {
                Ingredient[] ingredients = this.getOwner().getFoodDatabase().getIngredientArray();
                this.pushFoodSearchState("Ingredients", ingredients, true);
                break;
            }
            case UserOptions.RECIPES: {
                Recipe[] recipes = this.getOwner().getFoodDatabase().getRecipeArray();
                this.pushFoodSearchState("Recipes", recipes, false);
                break;
            }
            case UserOptions.MEALS: {
                Meal[] meals = this.getOwner().getFoodDatabase().getMealArray();
                this.pushFoodSearchState("Meals", meals, false);
                break;
            }
            case UserOptions.LOGOUT: {
                this.getOwner().setUser(null);
                this.showMessage("Successfully logged out!");
                break;
            }
            case UserOptions.QUIT: {
                this.getOwner().quit();
                break;
            }
            default: {
                this.showError("Invalid command!");
                break;
            }
        }
    }

    @Override public void run() {
        this.showHeader();

        // Privileged and guest users have different options,
        // so show each accordingly.
        this.showMenu(this.isAuthenticated() ? UserOptions.MENU : GuestOptions.MENU);

        int command = this.getOptionIndex();
        if (command == -1) return;

        if (this.isAuthenticated()) this.handleUserCommand(command);
        else this.handleGuestCommand(command);
    }
}
