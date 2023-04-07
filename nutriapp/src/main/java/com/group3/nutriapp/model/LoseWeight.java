package com.group3.nutriapp.model;

/**
 * @author Collin Cleary + Group 3
 * @description This class is a Goal with the intention of losing weight
 * @date 2/27/2023
 */

public class LoseWeight implements Goal{
    public int targetCalories;
    public double targetWeight;
    public Workout[] excercises;
    public int currentCalories;

    public LoseWeight(double targetWeight){
        this.targetWeight = targetWeight;
        this.targetCalories = 1800;
        this.excercises = new Workout[0];
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

    public Workout[] getExcercises() {
        return this.excercises;
    }

    public void setExcercises(Workout[] excercisesArray){
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

    public String toString(){
        return "lose";
    }
}
