package com.group3.nutriapp.model;
import java.sql.Time;

/**
 * @author Collin Cleary + Group 3
 * @description This class is a subclass of Food and is used to create Meal objects
 * @date 2/27/2023
 */
public class User {
    private String name;
    private double height;
    private double weight;
    private Time birthdate;
    private int age;
    private Goal goal;
    //private Observer observer;
    
    public User(String name, double height, double weight, Time birthdate, int age){
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.birthdate = birthdate;
        this.age = age;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setHeight(double height){
        this.height = height;
    }

    public void setWeight(double weight){
        this.weight = weight;
    //    notifyObserver();
    }

    public void setGoal(Goal goal){
        this.goal = goal;
    //    notifyObserver();
    }

    //public void subscribe(Observer observer){
    //    this.observer = observer;
    //}

    //public void notifyObserver(){
    //    observer.notify();
    //}
}