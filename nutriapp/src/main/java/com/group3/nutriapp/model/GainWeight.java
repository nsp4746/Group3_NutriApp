package com.group3.nutriapp.model;
import java.time.LocalDateTime;

public class GainWeight implements Goal{
    public int targetCalories = 2200;
    public double targetWeight;
    public Workout[] excercises = new Workout[0];
    public int currentCalories = 0;

    /**
     * Default constructor used for serialization.
     */
    public GainWeight() {};

    public GainWeight(double targetWeight){
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
        if(currentCalories >= targetCalories){
            this.excercises[0] = new Workout(0, 30, 1, 6, 6, LocalDateTime.now());
            this.excercises[1] = new Workout(1, 30, 1, 3, 12, LocalDateTime.now());
        }
        else{
            this.excercises[0] = new Workout(0, 15, 1, 3, 3, LocalDateTime.now());
            this.excercises[1] = new Workout(1, 15, 1, 3, 12, LocalDateTime.now());
        }
    }

    public boolean checkGoalMet(double weight){
        if(weight >= this.targetWeight){
            return true;
        }
        else{
            return false;
        }
    }

    public String goalType(){
        return "gain";
    }

    @Override public String toString() {
        return "Gain Weight";
    }
}
