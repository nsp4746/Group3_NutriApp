package com.group3.nutriapp.Control;

import com.group3.nutriapp.model.Day;
import com.group3.nutriapp.model.User;
import com.group3.nutriapp.persistence.HistoryFileDAO;

public class DayObserver implements Observer {
   private User user;
   private HistoryFileDAO dao;
   private Runnable callback;

   public DayObserver(User user, HistoryFileDAO dao, Runnable callback) {
      this.user = user;
      this.dao = dao;
      this.callback = callback;
   }

   @Override public void update() {
      // Add the current day to our history
      Day day = user.getDay();
      dao.addDay(
         day.getUserId(),
         day.getDate(),
         day.getWeight(),
         day.getCalorieIntake(),
         day.getCalorieGoal(),
         day.getMeals(),
         day.getWorkouts()
      );

      // Reset the day attached to user
      user.startNewDay();

      // Run the user specified callback after we've added the day to the history
      if (callback != null)
         callback.run();
   }
}
