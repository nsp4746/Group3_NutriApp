package com.group3.nutriapp.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

/**
 * @author Collin Cleary + Group 3
 * @description This class is a subclass of Food and is used to create Meal objects
 * @date 2/27/2023
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @Type(value = Combination.class, name = "combination"),
    @Type(value = GainWeight.class, name = "gain"),
    @Type(value = LoseWeight.class, name = "lose"),
    @Type(value = MaintainWeight.class, name = "maintain")
})
public interface Goal {
    public int getTargetCalories();
    public boolean setTargetCalories(int targetCalories);
    public double getTargetWeight();
    public void reset();
    public int getCurrentCalories();
    public void setCurrentCalories(int calories);
    public boolean addCurrentCalories(int calories);
    public boolean subtractCurrentCalories(int calories);
    public Workout[] getExcercises();
    public void setExcercises();
    public boolean checkGoalMet(double weight);
}

