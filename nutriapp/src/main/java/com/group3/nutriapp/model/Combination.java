package com.group3.nutriapp.model;

import java.time.LocalDateTime;

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

    public void setExcercises(){
        this.excercises = new Workout[2];
        if(status == Status.gain){
            if(currentCalories >= targetCalories){
                this.excercises[0] = new Workout(0, 30, 3, 6, 6, LocalDateTime.now());
                this.excercises[1] = new Workout(1, 30, 3, 3, 12, LocalDateTime.now());
            }
            else{
                this.excercises[0] = new Workout(0, 15, 2, 3, 3, LocalDateTime.now());
                this.excercises[1] = new Workout(1, 15, 2, 3, 12, LocalDateTime.now());
            }
        }
        if(status == Status.lose){
            if(currentCalories >= targetCalories){
                this.excercises[0] = new Workout(0, 10, 3, 3, 10, LocalDateTime.now());
                this.excercises[1] = new Workout(1, 10, 3, 3, 12, LocalDateTime.now());
            }
            else{
                this.excercises[0] = new Workout(0, 150, 2, 1, 4, LocalDateTime.now());
                this.excercises[1] = new Workout(1, 150, 2, 3, 4, LocalDateTime.now());
            }
        }
        if(status == Status.maintain){
            if(currentCalories >= targetCalories){
                this.excercises[0] = new Workout(0, 20, 3, 3, 20, LocalDateTime.now());
                this.excercises[1] = new Workout(1, 20, 3, 2, 30, LocalDateTime.now());
            }
            else{
                this.excercises[0] = new Workout(0, 10, 2, 3, 20, LocalDateTime.now());
                this.excercises[1] = new Workout(1, 10, 2, 2, 30, LocalDateTime.now());
            }
        }
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
