package com.group3.nutriapp;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.group3.nutriapp.model.Combination;
import com.group3.nutriapp.model.GainWeight;
import com.group3.nutriapp.model.MaintainWeight;
import com.group3.nutriapp.model.User;
import com.group3.nutriapp.model.Combination.Status;
import com.group3.nutriapp.persistence.UserFileDAO;

public class UserPersistence {
    public static void main(String[] args) {
        UserFileDAO fileDAO = new UserFileDAO();

        User user = fileDAO.getUser("pablo");
        if (user == null) {
            System.out.println("Creating user..");
            user = fileDAO.addUser("pablo", 0, 0, LocalDate.now(), "");
        }

        System.out.println(user.getGoal());
        
        // user.setGoal(new Combination(150.0, Status.gain));
        user.setGoal(new GainWeight());

        fileDAO.updateUser(user);
        
        System.out.println(fileDAO.getUserArray());
    }
}
