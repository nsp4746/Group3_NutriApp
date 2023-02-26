package nutriapp;

//Imports
import java.util.ArrayList;

/**
 * @author Nikhil Patil + Group 3
 * @description This class is a subclass of Food and is used to create Recipe objects
 * @date 2/26/2023
 */

public class Recipe extends Food {
    private ArrayList<Ingredient> ingredients;
    
    public Recipe(double calories, double protein, double carbs, String name, int id, ArrayList<Ingredient> ingredients) {
        super(calories, protein, carbs, name, id);
        this.ingredients = ingredients;
    }

    public boolean addIngredient(Ingredient i) {
        int size = ingredients.size();
        int newSize = size + 1;
        ingredients.add(i);
        if (ingredients.size() == newSize) {
            return true;
        } else {
            return false;
        }
    }

    public boolean removeIngredient(Ingredient i){
        int size = ingredients.size();
        int newSize = size - 1;
        ingredients.remove(i);
        if (ingredients.size() == newSize) {
            return true;
        } else {
            return false;
        }
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
