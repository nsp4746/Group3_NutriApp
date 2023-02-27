package com.group3.nutriapp.model;

import java.time.LocalDateTime;

public class Workout {
   private int ID;
   private double minutes;
   private int intensity;
   private int sets;
   private int reps;
   private LocalDateTime date;

   public Workout(int ID, double minutes, int intensity, int sets, int reps, LocalDateTime date){
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

   public double getMinutes() {
      return minutes;
   }

   public int getIntensity() {
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
