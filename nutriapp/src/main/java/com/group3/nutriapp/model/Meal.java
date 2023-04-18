package com.group3.nutriapp.model;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Nikhil Patil + Group 3
 * @description This class is a subclass of Food and is used to create Meal objects
 * @date 2/26/2023
 */
public class Meal extends Food {
     
    private ArrayList<Recipe> recipes;

    /* Default constructor for serialization.  */
    public Meal() { super(); }
    
    public Meal(double calories, double protein, double carbs, double fat, double fiber, String name, int id, ArrayList<Recipe> recipes) {
        super(calories, protein, carbs, fat, fiber, name, id);
        this.recipes = recipes;
    }

    /**
     * Generates a map of how much stock each ingredient needs in this meal.
     * @return The generated stock map
     */
    public HashMap<Ingredient, Integer> generateStockMap() {
        HashMap<Ingredient, Integer> usages = new HashMap<>();
        for (Recipe recipe : recipes) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                int count = usages.getOrDefault(ingredient, 0) + 1;
                usages.put(ingredient, count);
            }
        }
        return usages;
    }

    public boolean addRecipe(Recipe recipe) {
        if (recipe == null || this.recipes.contains(recipe)) return false;
        this.recipes.add(recipe);

        this.setCalories(this.getCalories() + recipe.getCalories());
        this.setProtein(this.getProtein() + recipe.getProtein());
        this.setCarbs(this.getCarbs() + recipe.getCarbs());
        this.setFat(this.getFat() + recipe.getFat());
        this.setFiber(this.getFiber() + recipe.getFiber());

        return true;
    }

    public boolean removeRecipe(Recipe recipe) {
        if (recipe == null || !this.recipes.contains(recipe)) return false;
        this.recipes.remove(recipe);

        this.setCalories(this.getCalories() - recipe.getCalories());
        this.setProtein(this.getProtein() - recipe.getProtein());
        this.setCarbs(this.getCarbs() - recipe.getCarbs());
        this.setFat(this.getFat() - recipe.getFat());
        this.setFiber(this.getFiber() - recipe.getFiber());
        
        return true;
    }

    // Getters
    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    /**
     * Simple function that uses a for each loop to assemble a string that only has the names of the FOODS
     * @return String of all ingredients in the FOODS
     */
    public String recipesString(){
        String s = "";
        for(Recipe r : this.recipes){
            s += r.getName() + ", ";
        }
        return s;

    
    }

    @Override
    public String toString() {
        return "Meal" + super.toString() + "foods=" + recipes;
    }
}
