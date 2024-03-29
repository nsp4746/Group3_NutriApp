package com.group3.nutriapp.model;

//Imports
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Nikhil Patil + Group 3
 * @description This class is a subclass of Food and is used to create Recipe objects
 * @date 2/26/2023
 */

public class Recipe extends Food {
    private ArrayList<Ingredient> ingredients;
    private String instructions;

    /* Default constructor for serialization.  */
    public Recipe() { super(); };

    public Recipe(double calories, double protein, double carbs, double fat, double fiber, String name, int id, ArrayList<Ingredient> ingredients, String instructions) {
        super(calories, protein, carbs, fat, fiber, name, id);
        this.ingredients = ingredients;
        this.instructions = instructions;
    }

    /**
     * Generates a map of how much stock each ingredient needs in this recipe.
     * @return The generated stock map
     */
    public HashMap<Ingredient, Integer> generateStockMap() {
        HashMap<Ingredient, Integer> usages = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            int count = usages.getOrDefault(ingredient, 0) + 1;
            usages.put(ingredient, count);
        }
        return usages;
    }

    public boolean addIngredient(Ingredient ingredient) {
        if (ingredient == null) return false;
        this.ingredients.add(ingredient);

        this.setCalories(this.getCalories() + ingredient.getCalories());
        this.setProtein(this.getProtein() + ingredient.getProtein());
        this.setCarbs(this.getCarbs() + ingredient.getCarbs());
        this.setFat(this.getFat() + ingredient.getFat());
        this.setFiber(this.getFiber() + ingredient.getFiber());

        return true;
    }

    public boolean removeIngredient(Ingredient ingredient){
        if (ingredient == null || !this.ingredients.contains(ingredient)) return false;
        this.ingredients.remove(ingredient);

        this.setCalories(this.getCalories() - ingredient.getCalories());
        this.setProtein(this.getProtein() - ingredient.getProtein());
        this.setCarbs(this.getCarbs() - ingredient.getCarbs());
        this.setFat(this.getFat() + ingredient.getFat());
        this.setFiber(this.getFiber() + ingredient.getFiber());

        return true;
    }

    // Getters
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    /**
     * Simple function that uses a for each loop to assemble a string that only has the names of the ingredients
     * @return String of all ingredients in the recipe
     */
    public String ingredientsString(){
        String s = "";
        for(Ingredient i : this.ingredients){
            s += i.getName() + ", ";
        }
        return s;
    }

    @Override
    public String toString() {
        return "Recipe=" + super.toString() + ", ingredients=" + ingredientsString();
    }
}