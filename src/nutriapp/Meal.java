package nutriapp;

import java.util.ArrayList;

/**
 * @author Nikhil Patil + Group 3
 * @description This class is a subclass of Food and is used to create Meal objects
 * @date 2/26/2023
 */
public class Meal extends Food {
     
    private ArrayList<Food> foods;
    
    public Meal(double calories, double protein, double carbs, String name, int id, ArrayList<Food> foods) {
        super(calories, protein, carbs, name, id);
        this.foods = foods;
    }

    public boolean addFood(Food f) {
        int size = foods.size();
        int newSize = size + 1;
        foods.add(f);
        if (foods.size() == newSize) {
            return true;
        } else {
            return false;
        }
    }

    public boolean removeFood(Food f){
        int size = foods.size();
        int newSize = size - 1;
        foods.remove(f);
        if (foods.size() == newSize) {
            return true;
        } else {
            return false;
        }
    }

    // Getters
    public ArrayList<Food> getFoods() {
        return foods;
    }

    /**
     * Simple function that uses a for each loop to assemble a string that only has the names of the FOODS
     * @return String of all ingredients in the FOODS
     */
    public String foodsString(){
        String s = "";
        for(Food f : this.foods){
            s += f.getName() + ", ";
        }
        return s;

    
    }

    @Override
    public String toString() {
        return "Meal" + super.toString() + "foods=" + foods;
    }
}
