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

    private Map<Integer, User> users = new HashMap<>();
    private int nextUserID = 1;
    private ObjectMapper objectMapper = new ObjectMapper();

    public UserFileDAO() { this.load(); }

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

    public User getUser(int id) {
        return this.users.get(id);
    }
    
    public User[] getUserArray() { 
        return this.users.values().toArray(User[]::new); 
    }

    public User[] findUser(String containsText){
        ArrayList<User> userList = new ArrayList<>();
        for (User user : this.users.values()) {
            if(containsText == null || user.getName().contains(containsText)) {
                userList.add(user);
            }
        }

        User[] userArray = new User[userList.size()];
        userList.toArray(userArray);
        return userArray;
    }

    public User addUser(String name, double height, double weight, LocalDate birthday, String password){
        int age = (int) ChronoUnit.YEARS.between(birthday, LocalDate.now());
        User user = new User(nextUserID, name, height, weight, age, password);
        users.put(user.getId(), user);
        save();
        nextUserID++;
        return user;
    }

    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            save();
            return user;
        }

        return null;
    }

    public boolean deleteUser(int ID){
        if (users.containsKey(ID)) {
            users.remove(ID);
            return save();
        }

        return false;
    }
}
