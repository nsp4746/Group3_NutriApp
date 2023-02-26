package com.group3.nutriapp.persistence;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FoodFileDAO {
   private Map<Integer, Ingredient> ingredients;
   private int nextInID;
   private Map<Integer, Meal> meals;
   private int nextMealID;
   private Map<Integer, Recipe> recipes;
   private int nextRecipeID;
   private ObjectMapper objectMapper;

   public FoodFileDAO(){
      nextInID = 0;
      nextMealID = 0;
      nextRecipeID = 0;
      this.objectMapper = new ObjectMapper();
   }

   private boolean save(){
      Ingredient[] ingredientList = getIngredientArray();
      Meal[] mealList = getMealArray();
      Recipe[] recipeList = getRecipeList();

      objectMapper.writeValue(new File("data/ingredients.json"), ingredientList);
      objectMapper.writeValue(new File("data/meals.json"), ingredientList);
      objectMapper.writeValue(new File("data/recipes.json"), ingredientList);
   
      return true;
   }

   private boolean load(){
      this.ingredients = new HashMap<Integer, Ingredient>();
      this.meals = new HashMap<Integer, Meals>();
      this.recipes = new HashMap<Integer, Recipes>();

      Ingredient[] ingredientList = objectMapper.readValue("data/ingredients.json", Ingredient[].class);
      Meal[] mealList = objectMapper.readValue("data/meals.json", Meal[].class);
      Recipes[] recipeList = objectMapper.readValue("data/recipess.json", Recipes[].class);

      for(Ingredient ingredient : ingredientList) {
         ingredients.put(ingredient.getID(), ingredient);
      }

      for(Meal meal : mealList) {
         meals.put(meal.getID(), meal);
      }

      for(Recipe recipe : recipeList) {
         recipes.put(recipe.getID(), recipe);
      }

      return true;
   }

   public Ingredient[] getIngredientArray(){
      findIngredients(null);
   }

   public Meal[] getMealArray(){
      findMeals(null);
   }

   public Recipe[] getRecipeArray(){
      findRecipe(null);
   }

   public Ingredient[] findIngredients(String containsText){
      ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();
      for (Ingredient ingredient : ingredients.values()) {
         if(containsText == null || ingredient.getName().contains(containsText)) {
            ingredientArrayList.add(ingredient);
         }
      }

      Ingredient[] ingredientArray = new Ingredient[ingredientArrayList.size()];
      ingredientArrayList.toArray(ingredientArray);
      return ingredientArray;
   }

   public Meal[] findMeals(String containsText){
      ArrayList<Meal> mealArrayList = new ArrayList<>();
      for (Meal meal : meals.values()) {
         if(containsText == null || meal.getName().contains(containsText)) {
            mealArrayList.add(meal);
         }
      }

      Meal[] mealArray = new Meal[mealArrayList.size()];
      mealArrayList.toArray(mealArray);
      return mealArray;
   }

   public Recipe[] findRecipe(String containsText){
      ArrayList<Recipe> recipeArrayList = new ArrayList<>();
      for (Recipe recipe : recipes.values()) {
         if(containsText == null || recipe.getName().contains(containsText)) {
            recipeArrayList.add(recipe);
         }
      }

      Recipe[] recipeArray = new Recipe[recipeArrayList.size()];
      recipeArrayList.toArray(recipeArray);
      return recipeArray;
   }
   
   public Ingredient getIngredient(int ID){
      return recipes.get(ID);
   }

   public Meal getMeal(int ID){
      return meals.get(ID);
   }

   public Recipe getRecipe(int ID){
      return recipes.get(ID);
   }

   public Ingredient addIngredient(double calories, double proteins, String name, int calories){
      Ingredient ingredient = new Ingredient(nextInID, calories, proteins, name, calories);
      ingredients.put(ingredient.getID(), ingredient);
      save();
      nextInID();
      return ingredient;
   }

   public Meal addMeal(ArrayList<Food> ingredients){
      Meal meal = new Meal(nextMealID, ingredients);
      meals.put(meal.getID(), meal);
      save();
      nextMealID();
      return meal;
   }

   public Recipe addRecipe(ArrayList<Food> ingredient){
      Recipe recipe = new Recipe(nextRecipeID, ingredient);
      recipes.put(recipe.getID(), recipe);
      save();
      nextRecipeID();
      return recipe;
   }

   public Ingredient updateIngredient(Ingredient ingredient){
      if(!ingredients.contains(ingredient.getID())){
         return null;
      }
      else{
         ingredients.put(ingredient.getID(), ingredient);
         save();
         return ingredient;
      }
   }

   public Meal updateMeal(Meal meal){
      if(!meals.contains(meal.getID())){
         return null;
      }
      else{
         meals.put(meal.getID(), meal);
         save();
         return meal;
      }
   }

   public Recipe updateRecipe(Recipe recipe){
      if(!recipes.contains(recipe.getID())){
         return null;
      }
      else {
         recipes.put(recipe.getID(), recipe);
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

   public boolean deleteRecipe(){
      if(recipes.containsKey(ID)) {
         recipes.remove(ID);
         return save();
      }
      else{
         return false;
      }
   }

}
