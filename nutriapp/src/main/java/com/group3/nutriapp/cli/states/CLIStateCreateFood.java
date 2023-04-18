package com.group3.nutriapp.cli.states;

import java.util.ArrayList;

import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;
import com.group3.nutriapp.model.Food;
import com.group3.nutriapp.model.Ingredient;
import com.group3.nutriapp.model.Recipe;
import com.group3.nutriapp.persistence.FoodFileDAO;

/**
 * @author Aidan Ruiz + Group 3
 * @description CLI state for creating either a recipe or meal.
 * @date 4/18/23
 */
public class CLIStateCreateFood extends CLIState {
    /**
     * The types of food that can be created.
     */
    public static enum FoodType {
        RECIPE,
        MEAL
    }

    /**
     * The name of the food that we're creating.
     */
    private String name;

    /**
     * The instructions to prepare the recipe if applicable.
     */
    private String instructions;

    /**
     * The type of food that we're currently creating.
     */
    private FoodType type = FoodType.RECIPE;

    /**
     * Persistent food storage.
     */
    private FoodFileDAO dao;

    /**
     * All available food items that can be used to form this recipe/meal.
     */
    private Food[] food;

    /**
     * List of food items that the user has selected to form this recipe/meal.
     */
    private ArrayList<Integer> selections = new ArrayList<>();

    public CLIStateCreateFood(CLI cli, String name, FoodType type) { 
        super(cli, type == FoodType.RECIPE ? "Create Recipe: " + name : "Create Meal: " + name); 
        this.name = name;
        this.type = type;

        dao = cli.getFoodDatabase();

        // Recipes are made of ingredients, meals are made of recipes
        // Load whichever array appropriately.
        food = (type == FoodType.RECIPE) ? 
            dao.getIngredientArray() : dao.getRecipeArray();
    }

    /**
     * Event that triggers when the user opts to finalize their recipe/meal.
     * 
     * If there's no food items selected, print an error and return.
     * 
     * Otherwise, gather all food items to compose recipe/meal out of
     * , persist it to the database, then pop the current CLI state.
     */
    public void onCreate() {
        if (selections.size() == 0) {
            showError("Can't create food with no items!");
            return;
        }

        // Can't avoid having mostly duplicated code here
        // since the types of ArrayLists are different.
        if (type == FoodType.RECIPE) {
            if (instructions == null || instructions.isEmpty()) {
                showError("Can't create recipe with no instructions!");
                return;
            }

            ArrayList<Ingredient> ingredients = new ArrayList<>();
            for (int selection : selections)
                ingredients.add((Ingredient) food[selection]);
            dao.addRecipe(name, ingredients, instructions);

            showMessage("Successfully created recipe!");
        } else if (type == FoodType.MEAL) {
            ArrayList<Recipe> recipes = new ArrayList<>();
            for (int selection : selections)
                recipes.add((Recipe) food[selection]);
            dao.addMeal(name, recipes);

            showMessage("Successfully created meal!");
        }

        // Pop the current state since we're done here.
        getOwner().pop();
    }

    /**
     * Runs before the main menu is rendered to make
     * sure all text contents fit within the frame.
     */
    @Override public void prerun() {
        // Make sure all food titles fit within the window.
        for (int foodIndex : selections) {
            String name = food[foodIndex].getName();
            int size = name.length() + 4;
            if (size > getTableWidth())
                setTableWidth(size);
        }
    }


    @Override public void run() {
        CLI cli = getOwner();

        String title = (type == FoodType.RECIPE) ? "Ingredients" : "Recipes";

        // Show a table for what food items we've decided to compose this meal/recipe out of.
        showLine(title);
        showDivider(true);
        for (int foodIndex : selections)
            showLine(food[foodIndex].getName());
        if (selections.size() == 0)
            showLine("No foodstuff entered");
        showDivider(false);

        // Display an option to add ingredients/recipes to the recipe/meal.
        String option = (type == FoodType.RECIPE) ? "Add Ingredient" : "Add Recipe";
        addOption(option, () -> {
            // Push a searchable table that can also be selected from.
            CLIStateSearchableTable.pushFoodSearchState(
                cli, 
                title, 
                food, 
                type == FoodType.RECIPE, 
                selections
            );
        });

        // If we're a recipe, add option to set instructions
        if (type == FoodType.RECIPE) {
            addOption("Set Instructions", () -> {
                instructions = getInput("Enter instructions");
            });
        }

        // Option to finalize the recipe/meal
        addOption("Create", this::onCreate);

        // Default functionality for going back to previous page
        addOptionDivider();
        addBackOption();
    }


}
