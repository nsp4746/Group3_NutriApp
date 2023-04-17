package com.group3.nutriapp.model;

//Imports
import java.util.ArrayList;

/**
 * @author Nikhil Patil + Group 3
 * @description This class is a subclass of Food and is used to create Recipe objects
 * @date 2/26/2023
 */

public class Recipe extends Food {
    private ArrayList<Ingredient> ingredients;
    
    public Recipe(double calories, double protein, double carbs, double fat, double fiber, String name, int id, ArrayList<Ingredient> ingredients) {
        super(calories, protein, carbs, fat, fiber, name, id);
        this.ingredients = ingredients;
    }

    public boolean addIngredient(Ingredient ingredient) {
        if (ingredient == null || this.ingredients.contains(ingredient)) return false;
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