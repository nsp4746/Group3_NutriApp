package com.group3.nutriapp.persistence;

import java.time.LocalDate;
import java.io.File;

public class HistoryFileDAO {
   private Map<Integer, Day> history;
   private int nextDayID;
   private ObjectMapper objectMapper;

   public DayFileDAO(){
      nextDayID = 0;
      objectMapper = new ObjectMapper();
      load();
   }

   private int getNextDayID(){
      nextDayID++;
      return nextDayID;
   }

   private boolean save(){
      Day[] dayArray = getDayArray();
      ObjectMapper.writeValue(new File("data/history.json"), dayArray);
      return true;
   }

   private boolean load(){
      history = new HashMap<Integer, Day>();
      Day[] dayArray = ObjectMapper.readValue(new File("data/history.json"), dayArray);
      for(Day day : dayArray) {
         history.put(day.getName(), day);
      }
      return true;
   }

   public Day[] getDayArray(){
      ArrayList<Day> dayList = new ArrayList<>();
      for(Day day : history.values()) {
         dayList.add(day);
      }

      Day[] dayArray = new Day[dayList.size()];
      dayList.toArray(dayArray);
      return dayArray;
   }

   public Day addDay(){
      Day day = new Day(nextDayID, name, height, weight, birthday, goal);
      history.put(day.getID(), day);
      save();
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

   public Day deleteDay(int ID){
      if(history.containsKey(ID)){
         history.remove(ID);
         return save();
      }
      else {
         return false;
      }
   }
}
