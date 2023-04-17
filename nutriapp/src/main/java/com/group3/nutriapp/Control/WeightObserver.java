package com.group3.nutriapp.Control;

import com.group3.nutriapp.model.MaintainWeight;
import com.group3.nutriapp.model.User;
import com.group3.nutriapp.persistence.UserFileDAO;

public class WeightObserver implements Observer{
   private UserFileDAO fileDAO;
   private User user;
   
   public WeightObserver(UserFileDAO fileDAO, User user){
      this.user = user;
      this.fileDAO = fileDAO;
   }

   public void update(){
      this.user.setGoal(new MaintainWeight(user.getWeight()));
      fileDAO.updateUser(user);
   }
}
