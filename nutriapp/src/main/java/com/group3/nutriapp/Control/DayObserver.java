package com.group3.nutriapp.Control;

import java.util.TimerTask;

import com.group3.nutriapp.persistence.HistoryFileDAO;

public class DayObserver extends TimerTask implements Observer{
   TimeManager timeManager;
   HistoryFileDAO fileDAO;

   public DayObserver(TimeManager timeManager) {
      this.timeManager = timeManager;
   }

   @Override
   public void run(){
      update();
   }

   @Override
   public void update(){
      timeManager.endDay();
   }

}
