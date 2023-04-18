package com.group3.nutriapp.cli.states;

import java.util.ArrayList;

import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;
import com.group3.nutriapp.model.Food;
import com.group3.nutriapp.model.Ingredient;
import com.group3.nutriapp.model.Recipe;
import com.group3.nutriapp.persistence.FoodFileDAO;

public class CLIStateCreateFood extends CLIState {
    public static enum FoodType {
        RECIPE,
        MEAL
    }

    private String name;
    private FoodType type = FoodType.RECIPE;
    private Food[] food;
    private ArrayList<Integer> selections = new ArrayList<>();

    public CLIStateCreateFood(CLI cli, String name, FoodType type) { 
        super(cli, type == FoodType.RECIPE ? "Create Recipe: " + name : "Create Meal: " + name); 
        this.name = name;
        this.type = type;

        FoodFileDAO dao = cli.getFoodDatabase();
        this.food = (this.type == FoodType.RECIPE) ? dao.getIngredientArray() : dao.getRecipeArray();
    }

    @Override public void prerun() {
        // Make sure all food titles fit within the window.
        for (int foodIndex : this.selections) {
            String name = food[foodIndex].getName();
            int size = name.length() + 4;
            if (size > this.getTableWidth())
                this.setTableWidth(size);
        }
    }

    @Override public void run() {
        CLI cli = this.getOwner();
        FoodFileDAO dao = cli.getFoodDatabase();

        String title = (this.type == FoodType.RECIPE) ? "Ingredients" : "Recipes";

        this.showLine(title);
        this.showDivider(true);
        for (int foodIndex : this.selections)
            this.showLine(food[foodIndex].getName());
        if (this.selections.size() == 0)
            this.showLine("No foodstuff entered");
        this.showDivider(false);

        String option = (this.type == FoodType.RECIPE) ? "Add Ingredient" : "Add Recipe";
        
        this.addOption(option, () -> {
            CLIStateSearchableTable.pushFoodSearchState(
                cli, 
                title, 
                food, 
                this.type == FoodType.RECIPE, 
                this.selections
            );
        });

        this.addOption("Create", () -> {

            if (selections.size() == 0) {
                this.showError("Can't create food with no items!");
                return;
            }

            if (this.type == FoodType.RECIPE) {
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                for (int selection : this.selections)
                    ingredients.add((Ingredient) food[selection]);
                dao.addRecipe(this.name, ingredients);

                this.showMessage("Successfully created recipe!");
            } else if (this.type == FoodType.MEAL) {
                ArrayList<Recipe> recipes = new ArrayList<>();
                for (int selection : this.selections)
                    recipes.add((Recipe) food[selection]);
                dao.addMeal(this.name, recipes);

                this.showMessage("Successfully created meal!");
            }

            // Pop the current state since we're done here.
            cli.pop();
        });

        this.addOptionDivider();
        this.addBackOption();
    }


}
