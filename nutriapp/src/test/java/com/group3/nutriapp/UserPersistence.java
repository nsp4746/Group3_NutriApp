package com.group3.nutriapp;

import com.group3.nutriapp.persistence.UserFileDAO;

public class UserPersistence {
    public static void main(String[] args) {
        UserFileDAO fileDAO = new UserFileDAO();
        System.out.println(fileDAO.getUserArray());
    }
}
