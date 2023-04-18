package com.group3.nutriapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.group3.nutriapp.io.LocalDateTimeDeserializer;
import com.group3.nutriapp.io.LocalDateTimeSerializer;

public class Day {
   @JsonProperty("userId") private int userId;
   @JsonProperty("id") private int id;

   @JsonSerialize(using = LocalDateTimeSerializer.class)
   @JsonDeserialize(using = LocalDateTimeDeserializer.class)
   @JsonProperty("date")
   private LocalDateTime date = LocalDateTime.now();
   
   @JsonProperty("weight") private double weight;
   @JsonProperty("calorieIntake") private int calorieIntake;
   @JsonProperty("calorieGoal") private int calorieGoal;
   @JsonProperty("meals") private ArrayList<Meal> meals = new ArrayList<>();
   @JsonProperty("workouts") private ArrayList<Workout> workouts = new ArrayList<>();

   public Day(int userId) {
      this.userId = userId;
   }

   public Day(
      @JsonProperty("userId") int userId,
      @JsonProperty("id") int id,
      @JsonProperty("date") LocalDateTime date,
      @JsonProperty("weight") double weight,
      @JsonProperty("calorieIntake") int calorieIntake,
      @JsonProperty("calorieGoal") int calorieGoal,
      @JsonProperty("meals") ArrayList<Meal> meals,
      @JsonProperty("workouts") ArrayList<Workout> workouts
   ) {
      this.userId = userId;
      this.id = id;
      this.date = date;
      this.weight = weight;
      this.calorieIntake = calorieIntake;
      this.calorieGoal = calorieGoal;
      this.meals = meals;
      this.workouts = workouts;
   }

   public int getUserId() {
      return userId;
   }

   public int getId() {
      return id;
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

   public ArrayList<Workout> getWorkouts() {
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
