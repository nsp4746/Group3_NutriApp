package com.group3.nutriapp.Control;

import java.util.Timer;
import java.util.TimerTask;

public class TimeManager {
   public static final long DEFAULT_DAY_MS = 86400000;

   private Timer timer;
   private Observer observer;

   public TimeManager() {
      this.setDayLength(DEFAULT_DAY_MS);
   }

   public void setObserver(Observer observer) {
      this.observer = observer;
   }

   public void setDayLength(long ms) {
      // Cancel the timer if it already exists
      if (timer != null)
         timer.cancel();
      
      // Initialize new timer
      timer = new Timer();

      // Run this timer at fixed rate according to specified milliseconds.
      timer.scheduleAtFixedRate(new TimerTask() {
         @Override public void run() {
            if (observer != null)
               observer.update();
         }
      }, ms, ms);
   }

   public void destroy() {
      if (timer != null)
         timer.cancel();
   }
}
