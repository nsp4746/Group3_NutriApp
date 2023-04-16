package com.group3.nutriapp.persistence;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;

import org.json.simple.JSONObject;

public class JSONFileWriter {

    /**
     * 1) Instantiate a JSONFileWriter Object
     * 2) Checks if file is created, else create file
     * 3) Write the necessary fields to the file
     * 4) Close the file
     * 5) Close the file writer
     * 
     * <------------------->
     * Parameters
     * File Path, User Name, User ID
     * Everything else will be created and closed when the object is done being used
     */

    private File jsonFile;
    private String pathToFile;
    private String userName;
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
            this.pathToFile = "..\\Group3_NutriApp\\data\\" + userName + "_" + userID + "_history.json";
        }        
    }


    public void writeJSON(String key, String value){
        jsonMap.put(key, value);
        object = new JSONObject(jsonMap);
        try{
            File file = new File(pathToFile);
            FileWriter fileWriter = new FileWriter(file);
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
