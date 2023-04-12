package com.group3.nutriapp.model;

public class MaintainWeight implements Goal{

    public int targetCalories;
    public double targetWeight;
    public Workout[] excercises;
    public int currentCalories;

    public MaintainWeight(double weight){
        this.targetWeight = weight;
        this.targetCalories = 2000;
        this.excercises = new Workout[0];
        this.currentCalories = 0;
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

    @Override
    public void setExcercises(Workout[] excercisesArray){
        this.excercises = excercisesArray;
    }

    @Override
    public boolean checkGoalMet(double weight) {
        return false;
    }
    
    public String toString(){
        return "maintain";
    }
}
