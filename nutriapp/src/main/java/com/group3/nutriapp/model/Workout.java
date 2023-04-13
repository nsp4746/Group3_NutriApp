package com.group3.nutriapp.model;

import java.time.LocalDateTime;

public class Workout {
   private int ID;
   private int minutes;
   private double intensity;
   private int sets;
   private int reps;
   private LocalDateTime date;

   public Workout(int ID, int minutes, double intensity, int sets, int reps, LocalDateTime date){
      this.ID = ID;
      this.minutes = minutes;
      this.intensity = intensity;
      this.sets = sets;
      this.reps = reps;
      this.date = date;
   }

   public int getID() {
      return ID;
   }

   public double getCaloriesBurned(){
      return minutes * intensity;
   }

   public int getMinutes() {
      return minutes;
   }

   public double getIntensity() {
      return intensity;
   }

   public int getSets() {
      return sets;
   }

   public int getReps() {
      return reps;
   }

   public LocalDateTime getDate() {
      return date;
   }
}
