package com.group3.nutriapp.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group3.nutriapp.model.User;

public class UserFileDAO {

    private Map<Integer, User> users;
    private int nextUserID;
    private ObjectMapper objectMapper;

   private boolean save() {
      User[] userArray = getUserArray();
      try { objectMapper.writeValue(new File("data/users.json"), userArray); }
      catch (Exception ex) { return false; }
      return true;
   }

   private boolean load() {
      this.users = new HashMap<Integer, User>();

      User[] users;
      try { users = objectMapper.readValue(new File("data/users.json"), User[].class); }
      catch (Exception ex) { return false; }

      for (User user : users) {
         int id = user.getId();
         this.users.put(id, user);
         if (id > this.nextUserID)
            this.nextUserID = id;
      }

      this.nextUserID++;

      return true;
   }

   public User getUser(String username) {
      for (User user : this.users.values()) {
         if (user.getName().equals(username))
            return user;
      }
      return null;
   }

   public User[] getUserArray(){
      return findUsers(null);
   }

   public User addUser(String name, double height, double weight, LocalDate birthday, String password){
      int age = (int) ChronoUnit.YEARS.between(birthday, LocalDate.now());
      User user = new User(nextUserID, name, height, weight, age, password);
      users.put(user.getId(), user);
      save();
      nextUserID++;
      return user;
   }

   public User updateUser(User user){
      if(!users.containsKey(user.getId())){
         return null;
      }
      else{
         users.put(user.getId(), user);
         save();
         return user;
      }
   }

   public boolean deleteUser(int ID){
      if(users.containsKey(ID)){
         users.remove(ID);
         return save();
      }
      else {
         return false;
      }
   }
}
