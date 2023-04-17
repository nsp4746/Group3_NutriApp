package com.group3.nutriapp.model;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GainWeight implements Goal{
    public int targetCalories;
    @JsonProperty("targetWeight") public double targetWeight;
    @JsonProperty("Objective") private Objective objective;
    public Workout[] excercises;
    public int currentCalories;

    public GainWeight(@JsonProperty("targetWeight") double targetWeight, @JsonProperty("objective") Objective objective){
        this.targetWeight = targetWeight;
        this.targetCalories = 2200;
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

    public String toString(){
        return String.format(STRING_FORMAT, targetWeight, objective);
    }
}
