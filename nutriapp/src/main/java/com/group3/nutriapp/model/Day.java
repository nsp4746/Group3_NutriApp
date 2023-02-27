package com.group3.nutriapp.model;
import java.time.LocalDate;

public class Day {
   private int ID;
   private LocalDate date;
   private double weight;
   private int calorieIntake;
   private Meal[] meals;
   private Workout[] workouts;

   public Day(int ID, LocalDate date, double weight, int calorieIntake, Meal[] meals, Workout[] workout){
      this.ID = ID;
      this.date = date;
      this.weight = weight;
      this.calorieIntake = calorieIntake;
      this.meals = meals;
      this.workouts = workout;
   }

   public int getID() {
      return ID;
   }

   public LocalDate getDate() {
      return date;
   }

   public double getWeight() {
      return weight;
   }

   public int getCalorieIntake() {
      return calorieIntake;
   }

   public Meal[] getMeals() {
      return meals;
   }

   public Workouts[] getWorkout() {
      return workouts;
   }
   
   public void setDate(LocalDate date) {
      this.date = date;
   }

   public void setWeight(double weight) {
      this.weight = weight;
   }

   public void setCalorieIntake(int calorieIntake) {
      this.calorieIntake = calorieIntake;
   }

   public void addCalorieIntake(int addAmount) {
      this.calorieIntake += addAmount;
   }

   public void subtractCalorieIntake(int subtractAmount){
      this.calorieIntake -= subtractAmount;
   }

   public void setMeals(Meal[] meals){
      this.meals = meals;
   }

   public void setWorkouts(Workout[] workouts){
      this.workouts = workouts;
   }
}
