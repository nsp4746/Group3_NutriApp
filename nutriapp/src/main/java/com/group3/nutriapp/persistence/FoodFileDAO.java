package com.group3.nutriapp.persistence;
import java.util.Map;

public class FoodFileDAO {
   private Map<Integer, Ingredient> ingredients;
   private int nextInID;
   private Map<Integer, Meal> meals;
   private int nextMealID;
   private Map<Integer, Recipe> recipes;
   private int nextRecipeID;
   private 

   public FoodFileDAO(){
      nextInID = 0;
      nextMealID = 0;
      nextRecipeID = 0;
   }
}
