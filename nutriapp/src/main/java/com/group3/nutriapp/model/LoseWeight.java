package com.group3.nutriapp.model;

/**
 * @author Collin Cleary + Group 3
 * @description This class is a subclass of Food and is used to create Meal objects
 * @date 2/27/2023
 */

public class LoseWeight implements Goal{
    public int targetCalories;
    public double targetWeight;
    public String[] excercises;
    public int currentCalories;

    public LoseWeight(double targetWeight){
        this.targetWeight = targetWeight;
        this.targetCalories = 2200;
        this.excercises = new String[0];
        this.currentCalories = 0;
    }

    public int getTargetCalories(){
        return this.targetCalories;
    }

    public boolean setTargetCalories(int targetCalories){
        int cals = this.currentCalories;
        this.targetCalories = targetCalories;
        if(this.currentCalories != cals){
            return true;
        }
        else{
            return false;
        }
    }

    public double getTargetWeight(){
        return this.targetWeight;
    }

    public boolean addCurrentCalories(int calories){
        int cals = this.currentCalories;
        this.currentCalories += calories;
        if(this.currentCalories != cals){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean subtractCurrentCalories(int calories){
        int cals = this.currentCalories;
        this.currentCalories -= calories;
        if(this.currentCalories != cals){
            return true;
        }
        else{
            return false;
        }
    }

    public String[] getExcercises() {
        return this.excercises;
    }

    public void setExcercises(String[] excercisesArray){
        this.excercises = excercisesArray;
    }

    public boolean checkGoalMet(double weight){
        if(weight <= this.targetWeight){
            return true;
        }
        else{
            return false;
        }
    }
}
