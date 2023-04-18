package com.group3.nutriapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.group3.nutriapp.io.LocalDateTimeDeserializer;
import com.group3.nutriapp.io.LocalDateTimeSerializer;

public class Workout {
   private int minutes;
   private double intensity;

   @JsonSerialize(using = LocalDateTimeSerializer.class)
   @JsonDeserialize(using = LocalDateTimeDeserializer.class)
   private LocalDateTime date;

   public Workout(
      @JsonProperty("minutes") int minutes, 
      @JsonProperty("intensity") double intensity, 
      @JsonProperty("date") LocalDateTime date
   ) {
      this.minutes = minutes;
      this.intensity = intensity;
      this.date = date;
   }

   @Override public String toString() {
      String time = this.date.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_TIME);
      return String.format("%d minutes @ %.2fcpm (%s)", minutes, intensity, time);
   }

   @JsonIgnore public double getCaloriesBurned(){
      return minutes * intensity;
   }

   public int getMinutes() {
      return minutes;
   }

   public double getIntensity() {
      return intensity;
   }

   public LocalDateTime getDate() {
      return date;
   }
}
