package com.group3.nutriapp.persistence;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author Nikhil Patil + Group 3
 * @description This class will export a database specific to a user and their
 *              selected data type
 * @date 4-10-2023
 */
public class ExportDB {

    private String dbType;
    private String dbPath = "/data";
    private String username;

    // Constructor
    public ExportDB(String username) {
        this.username = username;
        this.dbType = "";
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public String getDbType() {
        return dbType;
    }

    public String getDbPath() {
        return dbPath;
    }

    public void setDbType() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the type of database you would like to export (xml, json, csv): ");
        this.dbType = scanner.nextLine();
        scanner.close();
    }

    // Export Methods

    public void exportCSV() {

    }

    public void exportJSON() throws org.json.simple.parser.ParseException, ParseException {
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader(".\\data\\food.json")) {
            Object obj = jsonParser.parse(reader);
            JSONObject food = (JSONObject) obj;
            System.out.println(food);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
    }
    }
    

    public void exportXML() {
        // TODO
    }

    public static void main(String[] args) throws org.json.simple.parser.ParseException, ParseException {
        //Test export json
        ExportDB export = new ExportDB("test");
        export.exportJSON();
    }
}
