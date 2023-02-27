package com.group3.nutriapp;

import com.group3.nutriapp.persistence.FoodFileDAO;
import com.group3.nutriapp.persistence.HistoryFileDAO;
import com.group3.nutriapp.persistence.UserFileDAO;

public class App 
{
    public static void main(String[] args) {
        new CLI(
            new FoodFileDAO(),
            new UserFileDAO(),
            new HistoryFileDAO()
        ).run();
    }
}
