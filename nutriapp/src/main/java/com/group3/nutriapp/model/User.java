package com.group3.nutriapp.model;

import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.group3.nutriapp.Control.Observer;


/**
 * @author Collin Cleary + Group 3
 * @description This class represents a user and their information
 * @date 2/27/2023
 */
public class User {

    public static final String STRING_FORMAT = "User [id=%d, name=%s, height=%f, weight=%f, age=%d, PW=%s, requests=%s, goal=%s]";

    @JsonProperty("id") private int id;
    @JsonProperty("name") private String name;
    @JsonProperty("height") private double height;
    @JsonProperty("weight") private double weight;
    @JsonProperty("age") private int age;
    @JsonProperty("PW") private String passwordHash;
    @JsonProperty("requests") private HashSet<Integer> requests = new HashSet<>();
    @JsonProperty("goal") private Goal goal;
    @JsonProperty("day") private Day day;
    
    @JsonIgnore private transient Observer observer;
    @JsonIgnore private transient Goal oldGoal;
    @JsonIgnore private transient double oldWeight = Double.NaN;
    
    public User(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("height") double height, @JsonProperty("weight") double weight, @JsonProperty("age") int age, @JsonProperty("PW") String passwordHash){
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.passwordHash = passwordHash;
        if (day == null)
            startNewDay();
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

    public void setWeight(double weight) {
        this.oldWeight = this.weight;
        this.weight = weight;
        this.day.setWeight(weight);
        if (observer != null)
            observer.update();
    }

    public double getWeight() {return weight;}

    public int getAge() {return age;}

    public void setGoal(Goal goal){
        this.oldGoal = this.goal;
        this.goal = goal;
        // Make sure the calorie count matches even if we're switching goals
        if (this.oldGoal != null)
            this.goal.setCurrentCalories(this.oldGoal.getCurrentCalories());
    }

    @JsonIgnore
    public Goal getOldGoal() { return oldGoal; }
    public void clearOldGoal() { oldGoal = null; }

    @JsonIgnore
    public double getOldWeight() { return oldWeight; }
    public void clearOldWeight() { oldWeight = Double.NaN; }


    public Goal getGoal() {return goal;}

    public void setPassword(String pwhash){
        this.passwordHash = pwhash;
    }

    public String getPasswordHash(){
        return this.passwordHash;
    }

    public Day getDay() { return day; }
    public void startNewDay() { 
        day = new Day(id);
        day.setWeight(weight);
        if (goal != null) {
            day.setCalorieGoal(goal.getTargetCalories());
            // Clear the current calories since a new day has started.
            goal.subtractCurrentCalories(goal.getCurrentCalories());
        }
    }
    
    public void addRequest(int request) {
        this.requests.add(request);
    }

    public void removeRequest(int request) {
        this.requests.remove(request);
    }

    public boolean hasRequestFromUser(int user) {
        return this.requests.contains(user);
    }

    public boolean hasPendingRequests() {
        return this.requests.size() != 0;
    }

    public void clearAllRequests() {
        this.requests.clear();
    }

    public HashSet<Integer> getRequests() {
        return this.requests;
    }

    public void subscribe(Observer observer){
       this.observer = observer;
    }

    public void notifyObserver(){
       observer.update();
    }


    //Register Observer
    public void registerObserver(Observer observer){
       this.observer = observer;
    }

    public String toString() {
        return String.format(STRING_FORMAT, getId(), getName(), getHeight(), getWeight(), getAge(), passwordHash, requests, goal);
    }
}