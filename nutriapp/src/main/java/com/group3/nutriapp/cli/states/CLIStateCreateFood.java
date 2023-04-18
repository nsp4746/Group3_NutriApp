package com.group3.nutriapp.cli.states;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

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
     * List of food items that have been selected to form this recipe/meal.
     */
    private ArrayList<Food> selections = new ArrayList<>();

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
            String instructions = getInput("Enter instructions for recipe");
            ArrayList<Ingredient> ingredients = new ArrayList<>();
            for (Food selection : selections)
                ingredients.add((Ingredient) selection);
            dao.addRecipe(name, ingredients, instructions);

            showMessage("Successfully created recipe!");
        } else if (type == FoodType.MEAL) {
            ArrayList<Recipe> recipes = new ArrayList<>();
            for (Food selection : selections)
                recipes.add((Recipe) selection);
            dao.addMeal(name, recipes);

            showMessage("Successfully created meal!");
        }

        // Pop the current state since we're done here.
        getOwner().pop();
    }

    /**
     * Returns how many duplicates of a food item exist in the array.
     * @return The number of duplicates of a food item that exist in the array.
     */
    private int getFoodCount(Food item) {
        int count = 0;
        for (Food f : selections) {
            if (item == f)
                count++;
        }
        return count;
    }

    /**
     * Runs before the main menu is rendered to make
     * sure all text contents fit within the frame.
     */
    @Override public void prerun() {
        // Make sure all food titles fit within the window.
        for (Food food : selections) {
            String name = food.getName();
            int size = name.length() + 8; // Account for size field
            if (size > getTableWidth())
                setTableWidth(size);
        }
    }

    /**
     * Callback that gets triggered when a user selects a food item.
     * Adds the food item to the selection list.
     */
    public void onSelectCallback(int index) {
        Food selection = food[index];

        // Recipes can add multiple of ingredients
        if (type == FoodType.RECIPE) {
            double count = getInputDouble(String.format("How many %s do you want to add?", selection.getName()));
            // We don't have a specific class for the amount of ingredients
            // so we'll have to just add n-copies.
            if (count > 0)
                selections.addAll(Collections.nCopies((int) count, selection));
        } else {
            // Meals just have a single of each recipe
            if (selections.indexOf(selection) == -1)
                selections.add(selection);
            else
                showError("Recipe already exists in meal!");
        }
    }

    @Override public void run() {
        CLI cli = getOwner();

        String title = (type == FoodType.RECIPE) ? "Ingredients" : "Recipes";

        // Show a table for what food items we've decided to compose this meal/recipe out of.
        showLine(title);
        showDivider(true); 

        // Keep track of if a food item was already printed.
        HashSet<Food> duplicates = new HashSet<>();
        for (Food food : selections) {
            if (!duplicates.contains(food))
                showLine(String.format("%2dx %s", getFoodCount(food), food.getName()));
            duplicates.add(food);
        }
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
                this::onSelectCallback
            );
        });

        // Option to finalize the recipe/meal
        addOption("Create", this::onCreate);

        // Default functionality for going back to previous page
        addOptionDivider();
        addBackOption();
    }


}
