package com.group3.nutriapp.model;

enum Status {
    gain,
    lose,
    maintain
  }
  


public class Combination implements Goal{
    public int targetCalories;
    public double targetWeight;
    public Workout[] excercises;
    public int currentCalories;
    public Status status;

    public Combination(double weight, Status status){
        this.targetWeight = weight;
        this.targetCalories = 2000;
        this.excercises = new Workout[0];
        this.currentCalories = 0;
        this.status = status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
        if(status == Status.gain){
            if(weight >= this.targetWeight){
                return true;
            }
            else{
                return false;
            }
        }
        if(status ==Status.lose){
            if(weight <= this.targetWeight){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }
    
    public String toString(){
        return "Combination fitness "+ this.status;
    }
}
