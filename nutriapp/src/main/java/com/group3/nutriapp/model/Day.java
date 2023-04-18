package com.group3.nutriapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.group3.nutriapp.io.LocalDateTimeDeserializer;
import com.group3.nutriapp.io.LocalDateTimeSerializer;

public class Day {
   private int userID;
   private int ID;

   @JsonSerialize(using = LocalDateTimeSerializer.class)
   @JsonDeserialize(using = LocalDateTimeDeserializer.class)
   private LocalDateTime date = LocalDateTime.now();
   
   private double weight;
   private int calorieIntake;
   private int calorieGoal;
   private ArrayList<Meal> meals = new ArrayList<>();
   private ArrayList<Workout> workouts = new ArrayList<>();

   public Day(@JsonProperty("userID") int userID) {
      this.userID = userID;
   }

   public Day(
      @JsonProperty("userID") int userID,
      @JsonProperty("ID") int ID,
      @JsonProperty("date") LocalDateTime date,
      @JsonProperty("weight") double weight,
      @JsonProperty("calorieIntake") int calorieIntake,
      @JsonProperty("calorieGoal") int calorieGoal,
      @JsonProperty("meals") ArrayList<Meal> meals,
      @JsonProperty("workouts") ArrayList<Workout> workouts
   ) {
      this.userID = userID;
      this.ID = ID;
      this.date = date;
      this.weight = weight;
      this.calorieIntake = calorieIntake;
      this.calorieGoal = calorieGoal;
      this.meals = meals;
      this.workouts = workouts;
   }

   public int getUserID() {
      return userID;
   }

   public int getID() {
      return ID;
   }

   public LocalDateTime getDate() {
      return date;
   }

   public double getWeight() {
      return weight;
   }

   public int getCalorieIntake() {
      return calorieIntake;
   }

   public int getCalorieGoal() {
      return calorieGoal;
   }

   public ArrayList<Meal> getMeals() {
      return meals;
   }

   public ArrayList<Workout> getWorkout() {
      return workouts;
   }
   
   public void setDate(LocalDateTime date) {
      this.date = date;
   }

   public void setWeight(double weight) {
      this.weight = weight;
   }

   public void setCalorieIntake(int calorieIntake) {
      this.calorieIntake = calorieIntake;
   }

   public void setCalorieGoal(int calorieGoal) {
      this.calorieGoal = calorieGoal;
   }
   
   public void addCalorieIntake(int addAmount) {
      this.calorieIntake += addAmount;
   }

   public void subtractCalorieIntake(int subtractAmount){
      this.calorieIntake -= subtractAmount;
   }

   public void setMeals(ArrayList<Meal> meals){
      this.meals = meals;
   }

   public void setWorkouts(ArrayList<Workout> workouts){
      this.workouts = workouts;
   }
}
