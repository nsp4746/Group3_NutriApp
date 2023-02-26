package com.group3.nutriapp.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserFileDAO {
   private Map<Integer, User> users;
   private int nextUserID;
   private ObjectMapper objectMapper;

   public UserFileDAO(){
      nextUserID = 0;
      objectMapper = new ObjectMapper();
      load();
   }

   private int getNextUserID(){
      nextUserID++;
      return nextUserID;
   }

   private boolean save(){
      User[] userArray = getUserArray();
      ObjectMapper.writeValue(new File("data/users.json"), userArray);
      return true;
   }

   private boolean load(){
      users = new HashMap<Integer, User>();
      User[] userArray = ObjectMapper.readValue(new File("data/users.json"), userArray);
      for(User user : userArray) {
         users.put(user.getName(), user);
      }
      return true;
   }

   public User[] getUserArray(){
      return findUsers(null);
   }

   public User[] findUsers(String containsText){
      ArrayList<User> userList = new ArrayList<>();
      for(User user : users.values()) {
         if(containsText == null || user.getName().contains(containsText)){
            userList.add(user);
         }
      }

      User[] userArray = new User[userList.size()];
      userList.toArray(userArray);
      return userArray;
   }

   public User addUser(String name, double height, double weight, LocalDate birthday, Goal goal){
      User user = new User(nextUserID, name, height, weight, birthday, goal);
      users.put(user.getID(), user);
      save();
      getNextUserID();
      return user;
   }

   public User updateUser(User user){
      if(!users.containsKey(user.getID())){
         return null;
      }
      else{
         users.put(user.getID(), user);
         save();
         return user;
      }
   }

   public User deleteUser(int ID){
      if(users.containsKey(ID)){
         users.remove(ID);
         return save();
      }
      else {
         return false;
      }
   }
}
