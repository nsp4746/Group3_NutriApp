package com.group3.nutriapp.persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group3.nutriapp.model.*;

import java.io.File;

public class HistoryFileDAO {
   private Map<Integer, Day> history;
   private int nextDayID;
   private ObjectMapper objectMapper;

   public HistoryFileDAO() {
        nextDayID = 0;
        objectMapper = new ObjectMapper();
        this.load();
   }

   private int getNextDayID() { return this.nextDayID++; }

   private boolean save(){
      Day[] dayArray = getDayArray();
      try { objectMapper.writeValue(new File("data/history.json"), dayArray); }
      catch (Exception ex) { return false; }
      return true;
   }

   public boolean load() { return load("data"); }
   public boolean load(String directory) {
      history = new HashMap<Integer, Day>();

      Day[] days;
      try { days = objectMapper.readValue(new File(directory, "history.json"), Day[].class); }
      catch (Exception ex) { return false; }

      for (Day day : days) {
         int id = day.getId();
         this.history.put(id, day);
         if (id > this.nextDayID)
            this.nextDayID = id + 1;
      }
      return true;
   }

   public Day[] getDayArray() { 
      Collection<Day> days = this.history.values();
      return days.toArray(new Day[days.size()]); 
   }

   public Day[] getUserDayArray(int id) {
      Collection<Day> days = this.history.values();
      return days.stream().filter((day) -> day.getUserId() == id).toArray(Day[]::new);
   }

   public Day addDay(int userID, LocalDateTime date, double weight, int calorieIntake, int calorieGoal, ArrayList<Meal> meals, ArrayList<Workout> workout) {
      Day day = new Day(userID, this.getNextDayID(), date, weight, calorieIntake, calorieGoal, meals, workout);
      this.history.put(day.getId(), day);
      this.save();
      getNextDayID();
      return day;
   }

   public Day updateDay(Day day){
      if(!history.containsKey(day.getId())){
         return null;
      }
      else{
         history.put(day.getId(), day);
         save();
         return day;
      }
   }

   public boolean deleteDay(int ID){
      if(history.containsKey(ID)){
         history.remove(ID);
         return save();
      }
      else {
         return false;
      }
   }
}
