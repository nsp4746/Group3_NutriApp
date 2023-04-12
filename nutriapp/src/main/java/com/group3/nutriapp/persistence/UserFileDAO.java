package com.group3.nutriapp.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group3.nutriapp.model.User;

public class UserFileDAO {

    private Map<Integer, User> users;
    private int nextUserID;
    private ObjectMapper objectMapper;

    public UserFileDAO() {
        nextUserID = 0;
        objectMapper = new ObjectMapper();
        load();
    }

    private int getNextUserID() {
        nextUserID++;
        return nextUserID;
    }

    private boolean save() {
        User[] userArray = getUserArray();
        try {
            objectMapper.writeValue(new File("data/users.json"), userArray);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

   private boolean load(){
        users = new HashMap<Integer, User>();

        User[] userArray;
        try{userArray = objectMapper.readValue(new File("data/users.json"), User[].class);}
        catch(Exception ex){return false;}

        for(User user : userArray) {
            int id = user.getId();
            users.put(id, user);
            if(id > nextUserID){
                nextUserID = id+1;
            }
        }
        return true;
   }

    public User[] getUserArray() {
        return findUsers(null);
    }

    public User[] findUsers(String containsText) {
        ArrayList<User> userList = new ArrayList<>();
        for (User user : users.values()) {
            if (containsText == null || user.getName().contains(containsText)) {
                userList.add(user);
            }
        }

        User[] userArray = new User[userList.size()];
        userList.toArray(userArray);
        return userArray;
    }

    public User addUser(String name, double height, double weight, int age) {
        User user = new User(nextUserID, name, height, weight, age);
        users.put(user.getId(), user);
        save();
        getNextUserID();
        return user;
    }

    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            return null;
        } else {
            users.put(user.getId(), user);
            save();
            return user;
        }
    }

    public boolean deleteUser(int ID) {
        if (users.containsKey(ID)) {
            users.remove(ID);
            return save();
        } else {
            return false;
        }
    }
}
