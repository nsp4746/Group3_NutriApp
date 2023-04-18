package com.group3.nutriapp.persistence;

import java.time.LocalDate;
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

   private boolean load() {
      history = new HashMap<Integer, Day>();

      Day[] days;
      try { days = objectMapper.readValue(new File("data/history.json"), Day[].class); }
      catch (Exception ex) { return false; }

      for (Day day : days) {
         int id = day.getID();
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

   public Day addDay(int userID, LocalDate date, double weight, int calorieIntake, int calorieGoal, ArrayList<Meal> meals, ArrayList<Workout> workout) {
      Day day = new Day(userID, this.getNextDayID(), date, weight, calorieIntake, calorieGoal, meals, workout);
      this.history.put(day.getID(), day);
      this.save();
      getNextDayID();
      return day;
   }

   public Day updateDay(Day day){
      if(!history.containsKey(day.getID())){
         return null;
      }
      else{
         history.put(day.getID(), day);
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
