package com.group3.nutriapp.model;

public class Ingredient extends Food {
    private int stockCount;

    /* Default constructor for serialization.  */
    public Ingredient() { super(); };

    public Ingredient(double calories, double protein, double carbs, double fat, double fiber, String name, int id,
            int stockCount) {
        super(calories, protein, carbs, fat, fiber, name, id);
        this.stockCount = stockCount;

    }

    // Getters
    public int getStockCount() {
        return stockCount;
    }

    // Setters
    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    @Override
    public String toString() {
        return "Ingredient=" + super.toString() + ", stockCount=" + stockCount;
    }

    // testing
    public static void main(String[] args) {
        Ingredient i = new Ingredient(100, 10, 20, 1, 1, "Chicken", 1, 10);
        System.out.println(i);
    }
}
