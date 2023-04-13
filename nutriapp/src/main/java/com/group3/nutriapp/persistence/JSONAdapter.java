package com.group3.nutriapp.persistence;

import java.io.File;
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author Nikhil Patil + Group 3
 * @version 1.0
 * @description This class is used to read and write JSON files.
 * @dateCreated 4/10/2023
 */
public class JSONAdapter {

    // Intialize Variables
    private File JsonFile;
    private JSONParser parser;
    private String key; // Key to search for in JSON file

    private final static String FILEPATH = "..\\Group3_NutriApp\\data\\ingredients.json";

    public JSONAdapter(String Filepath,String key) {
        this.JsonFile = new File(Filepath);
        this.parser = new JSONParser();
        this.key = key;
    }

    // Getters and Setters
    public File getJsonFile() {
        return JsonFile;
    }

    public void setJsonFile(File jsonFile) {
        JsonFile = jsonFile;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Method to parse a JSON file and return all the values when given a key.
     * {@link} <br></br>
     * {@link} https://www.geeksforgeeks.org/parse-json-java/
     * @return empty string for now. Will most likely be changed
     */
    public String parseJSON() {
        try {
            Object obj = parser.parse(new FileReader(JsonFile));
            JSONArray jsonObject = (JSONArray) obj;

            for (int i = 0; i < jsonObject.size(); i++) {
                JSONObject ingredient = (JSONObject) jsonObject.get(i);
                System.out.println(ingredient.get(key));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void main(String[] args) {
        JSONAdapter adapter = new JSONAdapter(FILEPATH,"name");
        adapter.parseJSON();
    }
}
