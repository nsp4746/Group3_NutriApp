package com.group3.nutriapp.model;

import java.time.LocalDateTime;

/**
 * @author Collin Cleary + Group 3
 * @description This class is a Goal with the intention of losing weight
 * @date 2/27/2023
 */

public class LoseWeight implements Goal{
    public int targetCalories = 1800;
    public double targetWeight;
    public Workout[] excercises = new Workout[0];
    public int currentCalories = 0;

    /**
     * Default constructor used for serialization.
     */
    public LoseWeight() {};

    public LoseWeight(double targetWeight){
        this.targetWeight = targetWeight;
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

    @Override
    public void reset() {
        this.currentCalories = 0;
        this.excercises = new Workout[0];
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

    public void setExcercises(){
        this.excercises = new Workout[2];
        // TODO: Dynamically set these values later
        if(currentCalories >= targetCalories){
            this.excercises[0] = new Workout(20, 3, LocalDateTime.now());
            this.excercises[1] = new Workout(20, 3, LocalDateTime.now());
        }
        else{
            this.excercises[0] = new Workout(150, 1, LocalDateTime.now());
            this.excercises[1] = new Workout(150, 1, LocalDateTime.now());
        }
    }

    public boolean checkGoalMet(double weight){
        if(weight <= this.targetWeight){
            return true;
        }
        else{
            return false;
        }
    }

    public String goalType(){
        return "lose";
    }

    @Override public String toString() {
        return "Lose Weight";
    }
}

