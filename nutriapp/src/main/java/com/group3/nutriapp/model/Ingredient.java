package com.group3.nutriapp.model;

public class Ingredient {
   private int stockCount;

   public Ingredient(double calories, double protein, double carbs, String name, int id, int stockCount) {
       super(calories, protein, carbs, name, id);
       this.stockCount = stockCount;

   }

   // Getters
   public int getStockCount() {
       return stockCount;
   }
   //Setters
   public void setStockCount(int stockCount) {
       this.stockCount = stockCount;
   }

   @Override
   public String toString() {
       return "Ingredient=" + super.toString() + ", stockCount=" + stockCount;
   }

   //testing
   public static void main(String[] args) {
       Ingredient i = new Ingredient(100, 10, 20, "Chicken", 1, 10);
       System.out.println(i);
   }
}
