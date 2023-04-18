package com.group3.nutriapp.Control;

import com.group3.nutriapp.model.GainWeight;
import com.group3.nutriapp.model.Goal;
import com.group3.nutriapp.model.LoseWeight;
import com.group3.nutriapp.model.MaintainWeight;
import com.group3.nutriapp.model.User;
import com.group3.nutriapp.persistence.UserFileDAO;

public class WeightObserver implements Observer{
   /**
    * Persistent user storage.
    */
   private UserFileDAO fileDAO;

   /**
    * The user that we are observing.
    */
   private User user;

   /**
    * The last weight the observer was notified of.
    */
   private double oldWeight;
   
   public WeightObserver(UserFileDAO fileDAO, User user){
      this.user = user;
      this.fileDAO = fileDAO;
      this.oldWeight = user.getWeight();
   }

   public void update() {
      Goal goal = user.getGoal();
      if (goal == null) return;

      double weight = user.getWeight();
      boolean isMaintainingWeight = goal instanceof MaintainWeight;

      if (isMaintainingWeight) {
         // If we're maintaining weight and we increase
         // our weight by 5 pounds, switch to lose weight goal
         if (weight >= (oldWeight + 5))
            this.user.setGoal(new LoseWeight(oldWeight));
         // Otherwise, if we've lost 5 pounds,
         // switch to gaining weight.
         else if (weight <= (oldWeight - 5))
            this.user.setGoal(new GainWeight(oldWeight));
      } else {
         // If we're not maintaining weight and we've met
         // our goal, switch to maintaining weight
         if (goal.checkGoalMet(weight))
            this.user.setGoal(new MaintainWeight(weight));
      }

      fileDAO.updateUser(user);
      oldWeight = weight;
   }
}
