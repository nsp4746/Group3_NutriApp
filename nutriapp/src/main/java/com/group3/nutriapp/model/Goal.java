package com.group3.nutriapp.model;

public interface Goal {
    public int targetCalories;
    public final double targetWeight;
    public String[] excercises;
    public int currentCalories;

    public int getTargetCalories();
    public boolean setTargetCalories();
    public double getTargetWeight();
    public boolean addCurrentCalories();
    public boolean subtractCurrentCalories();
    public String[] getExcercises();
    public void setExcercises();
}
