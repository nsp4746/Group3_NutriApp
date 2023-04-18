package com.group3.nutriapp.model;

public class Food {
    private double calories;
    private double protein;
    private double carbs;
    private double fat;
    private double fiber;
    private String name;
    private int id;

    /* Default constructor for serialization.  */
    public Food() {};

    public Food(double calories, double protein, double carbs, double fat, double fiber, String name, int id) {
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
        this.fiber = fiber;
        this.name = name;
        this.id = id;
    }

    // Getters
    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public double getFat() {
        return fat;
    }

    public double getFiber() {
        return fiber;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    // Setters
    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    //////////////////////////////

    @Override
    public String toString() {
        return "[Name=" + name + ", Calories=" + calories + ", Protein=" + protein + ", Carbs=" + carbs + "]";
    }
}
