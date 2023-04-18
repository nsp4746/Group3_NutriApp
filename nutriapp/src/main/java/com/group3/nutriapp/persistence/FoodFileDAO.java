package com.group3.nutriapp.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.group3.nutriapp.model.*;

public class FoodFileDAO {
   private Map<Integer, Ingredient> ingredients;
   private int nextInID = 1;

   private Map<Integer, Meal> meals;
   private int nextMealID = 1;

   private Map<Integer, Recipe> recipes;
   private int nextRecipeID = 1;

   private ObjectMapper objectMapper = new ObjectMapper();

   public FoodFileDAO() {
      this.load();
   }

   private boolean save(){
      Ingredient[] ingredientList = getIngredientArray();
      Meal[] mealList = getMealArray();
      Recipe[] recipeList = getRecipeArray();

      try {
         objectMapper.writeValue(new File("data/ingredients.json"), ingredientList);
         objectMapper.writeValue(new File("data/meals.json"), mealList);
         objectMapper.writeValue(new File("data/recipes.json"), recipeList);
      } catch (Exception ex) { return false; }
   
      return true;
   }

   private void loadDefaultIngredients() {
      File ingredientsCSV = new File("data/ingredients.csv");
      if (!ingredientsCSV.exists()) return;
      Ingredient[] ingredients = CSVReader.readIngredients(ingredientsCSV.getAbsolutePath());
      for (Ingredient ingredient : ingredients) {
         int id = ingredient.getId();
         this.ingredients.put(id, ingredient);
         if (id > this.nextInID)
            this.nextInID = id;
      }
      this.nextInID++;
   }

   private boolean load() {
      this.ingredients = new HashMap<Integer, Ingredient>();
      this.meals = new HashMap<Integer, Meal>();
      this.recipes = new HashMap<Integer, Recipe>();

      Ingredient[] ingredientList;
      Meal[] mealList;
      Recipe[] recipeList;

      this.loadDefaultIngredients();
      
      try {
         ingredientList = objectMapper.readValue(new File("data/ingredients.json"), Ingredient[].class);
         mealList = objectMapper.readValue(new File("data/meals.json"), Meal[].class);
         recipeList = objectMapper.readValue(new File("data/recipes.json"), Recipe[].class);
      } catch (Exception ex) { return false; }

      // TODO: References need to be fixed up!

      for (Ingredient ingredient : ingredientList) {
         int id = ingredient.getId();
         ingredients.put(id, ingredient);
         if (id > this.nextInID)
            this.nextInID = id;
      }

      for (Meal meal : mealList) {
         int id = meal.getId();
         meals.put(id, meal);
         if (id > this.nextMealID)
            this.nextMealID = id;
      }

      for (Recipe recipe : recipeList) {
         int id = recipe.getId();
         recipes.put(id, recipe);
         if (id > this.nextRecipeID)
            this.nextRecipeID = id;
      }

      this.nextInID++;
      this.nextMealID++;
      this.nextRecipeID++;

      return true;
   }

   private int getNextInID() { return this.nextMealID++; }
   private int getNextMealID() { return this.nextMealID++; }
   private int getNextRecipeID() { return this.nextRecipeID++; }

   public Ingredient[] getIngredientArray(){
      return ingredients.values().toArray(Ingredient[]::new);
   }

   public Meal[] getMealArray(){
      return meals.values().toArray(Meal[]::new);
   }

   public Recipe[] getRecipeArray(){
      return recipes.values().toArray(Recipe[]::new);
   }

   public Ingredient findIngredient(String name){
      for (Ingredient ingredient : ingredients.values()) {
         if (ingredient.getName().equals(name))
            return ingredient;
      }
      return null;
   }

   public Meal findMeal(String name){
      for (Meal meal : meals.values()) {
         if (meal.getName().equals(name))
            return meal;
      }
      return null;
   }

   public Recipe findRecipe(String name){
      for (Recipe recipe : recipes.values()) {
         if (recipe.getName().equals(name))
            return recipe;
      }
      return null;
   }
   
   public Ingredient getIngredient(int ID){
      return ingredients.get(ID);
   }

   public Meal getMeal(int ID){
      return meals.get(ID);
   }

   public Recipe getRecipe(int ID){
      return recipes.get(ID);
   }

   public Ingredient addIngredient(double calories, double protein, double carbs, double fat, double fiber, String name, int stock) {
      Ingredient ingredient = new Ingredient(
         calories,
         protein,
         carbs,
         fat,
         fiber,
         name,
         this.getNextInID(),
         stock
      );

      this.ingredients.put(ingredient.getId(), ingredient);

      this.save();

      return ingredient;
   }

   public Meal addMeal(String name, ArrayList<Recipe> recipes) {
      double calories = recipes.stream().mapToDouble(Recipe::getCalories).sum();
      double protein = recipes.stream().mapToDouble(Recipe::getProtein).sum();
      double carbs = recipes.stream().mapToDouble(Recipe::getCarbs).sum();
      double fat = recipes.stream().mapToDouble(Recipe::getFat).sum();
      double fiber = recipes.stream().mapToDouble(Recipe::getFiber).sum();

      Meal meal = new Meal(calories, protein, carbs, fat, fiber, name, this.getNextMealID(), recipes);
      meals.put(meal.getId(), meal);

      this.save();

      return meal;
   }

   public Recipe addRecipe(String name, ArrayList<Ingredient> ingredients, String instructions) {
      double calories = ingredients.stream().mapToDouble(Ingredient::getCalories).sum();
      double protein = ingredients.stream().mapToDouble(Ingredient::getProtein).sum();
      double carbs = ingredients.stream().mapToDouble(Ingredient::getCarbs).sum();
      double fat = ingredients.stream().mapToDouble(Ingredient::getFat).sum();
      double fiber = ingredients.stream().mapToDouble(Ingredient::getFiber).sum();

      Recipe recipe = new Recipe(calories, protein, carbs, fat, fiber, name, this.getNextRecipeID(), ingredients, instructions);
      recipes.put(recipe.getId(), recipe);

      this.save();
      
      return recipe;
   }

   public Ingredient updateIngredient(Ingredient ingredient){
      if(!ingredients.containsKey(ingredient.getId())){
         return null;
      }
      else{
         ingredients.put(ingredient.getId(), ingredient);
         save();
         return ingredient;
      }
   }

   public Meal updateMeal(Meal meal){
      if(!meals.containsKey(meal.getId())){
         return null;
      }
      else{
         meals.put(meal.getId(), meal);
         save();
         return meal;
      }
   }

   public Recipe updateRecipe(Recipe recipe){
      if(!recipes.containsKey(recipe.getId())){
         return null;
      }
      else {
         recipes.put(recipe.getId(), recipe);
         save();
         return recipe;
      }
   }
   
   public boolean deleteIngredient(int ID){
      if(ingredients.containsKey(ID)) {
         ingredients.remove(ID);
         return save();
      }
      else{
         return false;
      }
   }

   public boolean deleteMeal(int ID){
      if(meals.containsKey(ID)) {
         meals.remove(ID);
         return save();
      }
      else{
         return false;
      }
   }

   public boolean deleteRecipe(int ID){
      if(recipes.containsKey(ID)) {
         recipes.remove(ID);
         return save();
      }
      else{
         return false;
      }
   }

}
