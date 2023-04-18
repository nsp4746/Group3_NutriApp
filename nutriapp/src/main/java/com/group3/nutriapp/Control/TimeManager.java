package com.group3.nutriapp.Control;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

import com.group3.nutriapp.model.Day;
import com.group3.nutriapp.model.User;
import com.group3.nutriapp.persistence.HistoryFileDAO;

public class TimeManager extends Timer{

   private User user; 
   private Timer timer;
   private HistoryFileDAO fileDAO;
   private LocalDateTime currentDateTime;
   private transient TimerTask dayObserver;
   private int dayLengthSeconds;
   private Day day;

   public TimeManager(User user, HistoryFileDAO fileDAO, LocalDateTime currDateTime, int dayLengthSeconds){
      this.user = user;    
      this.fileDAO = fileDAO;
      this.currentDateTime = currDateTime;
      this.timer = new Timer();
      this.dayObserver = new DayObserver(this);
      this.dayLengthSeconds = dayLengthSeconds;

      setNextDayTimer();
   }

   public void setNextDayTimer() {
      LocalDateTime remainingTime = LocalDateTime.of(2000, 01, 01, 23, 59, 59);

      remainingTime = remainingTime.minusHours(currentDateTime.getHour());
      remainingTime = remainingTime.minusMinutes(currentDateTime.getMinute());
      remainingTime = remainingTime.minusSeconds(currentDateTime.getSecond());

      long milliseconds = remainingTime.getHour() * 3600000;
      milliseconds += remainingTime.getMinute() * 60000;
      milliseconds += remainingTime.getSecond() * 1000;

      long ratio = milliseconds/dayLengthSeconds;
      milliseconds /= ratio;

      this.timer.schedule(dayObserver, milliseconds);
   }

   public void startDay() {
      LocalDateTime newDateTime = currentDateTime;
      newDateTime = currentDateTime.plusDays(1);
      newDateTime = currentDateTime.minusHours(currentDateTime.getHour());
      newDateTime = currentDateTime.minusMinutes(currentDateTime.getMinute());
      newDateTime = currentDateTime.minusSeconds(currentDateTime.getMinute());
      currentDateTime = newDateTime;

      setNextDayTimer();
      day = fileDAO.addDay(currentDateTime.toLocalDate(), 0.0, 0, null, null);
   }

   public void endDay() {
      day.setWeight(0);
      day.setCalorieIntake(0);
      day.setMeals(null);
      day.setWorkouts(null);
      fileDAO.updateDay(day);
      startDay();
   }

   public void setDayLength(int seconds) {
      this.dayLengthSeconds = seconds;
   }
}
