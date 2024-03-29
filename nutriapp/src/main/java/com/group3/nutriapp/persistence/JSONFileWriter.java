package com.group3.nutriapp.persistence;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

import org.json.simple.JSONObject;

public class JSONFileWriter {


    @SuppressWarnings("unused")
    private File jsonFile;
    private String pathToFile;
    @SuppressWarnings("unused")
    private String userName;
    @SuppressWarnings("unused")
    private String userID;
    private JSONObject object = new JSONObject();
    private HashMap<String, String> jsonMap;

    public JSONFileWriter(String pathToFile, String userName, String userID) {

        this.userName = userName;
        this.userID = userID;
        this.jsonMap = new HashMap<String, String>();
        //Checks if File exists
        File tempFile = new File(pathToFile);
        boolean fileExists = tempFile.exists();

        if (fileExists) {
            this.pathToFile = pathToFile;
        } else {
            this.pathToFile = "..\\Group3_NutriApp\\nutriapp\\data\\" + userName + "_" + userID + "_history.json";
        }        
    }


    /**
     * Function to write to a json file. 
     * @param key the key of the history
     * @param value the value of the history
     */
    public void writeJSON(String key, String value){
        jsonMap.put(key, value); // Adds the key and value to the HashMap
        object = new JSONObject(jsonMap); // Converts the HashMap to a JSONObject
        try{
            File file = new File(pathToFile); // Converts the path to a file
            FileWriter fileWriter = new FileWriter(file); // Creates a file writer
            fileWriter.append(object.toJSONString());
            fileWriter.flush();
            fileWriter.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        JSONFileWriter test = new JSONFileWriter("..\\Group3_NutriApp\\data\\test.json", "testUser","3");
        test.writeJSON("A", "B");
    }

}
