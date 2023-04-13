package com.group3.nutriapp.model;

/**
 * @author Collin Cleary + Group 3
 * @description This class is a subclass of Food and is used to create Meal objects
 * @date 2/27/2023
 */
public interface Goal {

    public int getTargetCalories();
    public boolean setTargetCalories(int targetCalories);
    public double getTargetWeight();
    public boolean addCurrentCalories(int calories);
    public boolean subtractCurrentCalories(int calories);
    public Workout[] getExcercises();
    public void setExcercises();
    public boolean checkGoalMet(double weight);
}
