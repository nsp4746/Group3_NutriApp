package com.group3.nutriapp.model;
import java.sql.Time;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.group3.nutriapp.Control.Observer;

/**
 * @author Collin Cleary + Group 3
 * @description This class represents a user and their information
 * @date 2/27/2023
 */
public class User {

    public static final String STRING_FORMAT = "User [id=%d, name=%s, height=%f, weight=%f, age=%d, goal=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("height") private double height;
    @JsonProperty("weight") private double weight;
    @JsonProperty("age") private int age;
    @JsonProperty("Goal") private Goal goal;
    private Observer observer;
    
    public User(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("height") double height, @JsonProperty("weight") double weight, @JsonProperty("age") int age){
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.age = age;
    }

    public int getId() {return id;}

    public void setName(String name){
        this.name = name;
    }

    public String getName() {return name;}

    public void setHeight(double height){
        this.height = height;
    }

    public double getHeight() {return height;}

    public void setWeight(double weight){
        this.weight = weight;
        if(this.goal.checkGoalMet(weight)){
            observer.update(this);
        }
    }

    public double getWeight() {return weight;}

    public int getAge() {return age;}

    public void setGoal(Goal goal){
        this.goal = goal;
        //notifyObserver();
    }

    public Goal getGoal() {return goal;}

    public void subscribe(Observer observer){
       this.observer = observer;
    }

    public void notifyObserver(){
       observer.notify();
    }

    public String toString() {
        return String.format(STRING_FORMAT, getId(), getName(), getHeight(), getWeight(), getAge(), getGoal());
    }
}