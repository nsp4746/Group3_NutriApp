package com.group3.nutriapp.persistence;

import java.util.Scanner;

/**
 * @author Nikhil Patil + Group 3
 * @description This class will import a database specific to a user and their
 *              selected database type (xml, json, csv)
 * @date 4/10/2023
 */
public class ImportDB {

    private String dbType;
    private String dbPath = "/data";
    private String username;

    public ImportDB(String username) {
        this.username = username;
        this.dbType = "";
    }

    public String getUsername() {
        return username;
    }

    public void setDbType() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the type of database you would like to import (xml, json, csv): ");
        this.dbType = scanner.nextLine();
        scanner.close();
    }

    public String getDbType() {
        return dbType;
    }

    public static void main(String[] args) {
    }

}
