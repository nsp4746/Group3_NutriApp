package com.group3.nutriapp.model;

import java.time.LocalDateTime;



public class MaintainWeight implements Goal{
    public int targetCalories = 2000;
    public double targetWeight;
    public Workout[] excercises = new Workout[0];
    public int currentCalories = 0;

    /**
     * Default constructor used for serialization.
     */
    public MaintainWeight() {};

    public MaintainWeight(double weight){
        this.targetWeight = weight;
    }

    @Override
    public int getTargetCalories() {
        return this.targetCalories;
    }

    @Override
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

    @Override
    public double getTargetWeight(){
        return this.targetWeight;
    }

    @Override
    public void reset() {
        this.currentCalories = 0;
        this.excercises = new Workout[0];
    }

    public int getCurrentCalories() { return currentCalories; }
    public void setCurrentCalories(int calories) { currentCalories = calories; }

    @Override
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

    @Override
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

    @Override
    public Workout[] getExcercises() {
        return this.excercises;
    }

    public void setExcercises(){
        this.excercises = new Workout[2];
        // TODO: Dynamically set these values later
        if(currentCalories >= targetCalories){
            this.excercises[0] = new Workout(20, 2, LocalDateTime.now());
            this.excercises[1] = new Workout(20, 2, LocalDateTime.now());
        }
        else{
            this.excercises[0] = new Workout(10, 1, LocalDateTime.now());
            this.excercises[1] = new Workout(10, 1, LocalDateTime.now());
        }
    }

    @Override
    public boolean checkGoalMet(double weight) {
        return false;
    }
    
    public String goalType(){
        return "maintain";
    }

    @Override public String toString() {
        return "Maintain Weight";
    }
}

