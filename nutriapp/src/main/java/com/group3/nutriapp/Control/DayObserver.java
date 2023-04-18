package com.group3.nutriapp.Control;

import com.group3.nutriapp.model.User;
import com.group3.nutriapp.persistence.HistoryFileDAO;

public class DayObserver implements Observer {
   private User user;
   private HistoryFileDAO dao;

   public DayObserver(User user, HistoryFileDAO dao) {
      this.user = user;
      this.dao = dao;
   }

   @Override public void update() {


      
      System.out.println("A DAY HAS PASSED, DUN DUN DUN");
   }
}
