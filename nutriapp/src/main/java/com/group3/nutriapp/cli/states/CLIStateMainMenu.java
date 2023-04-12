package com.group3.nutriapp.cli.states;

import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;
import com.group3.nutriapp.model.Food;
import com.group3.nutriapp.model.Ingredient;
import com.group3.nutriapp.model.Meal;
import com.group3.nutriapp.model.Recipe;

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
                String password = this.getInput("Enter your password");

                this.showError("Failed to login as no implementation exists!");

                break;
            }
            case GuestOptions.REGISTER: {
                String username = this.getInput("Enter your username");
                String password = this.getInput("Enter your password");

                this.showError("Failed to register account as no implementation exists!");

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
