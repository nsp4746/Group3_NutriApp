package com.group3.nutriapp.persistence;

import com.group3.nutriapp.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReader {
    /*
     * Stub
     * Returns array of ingredients from filepath csv file
     */
    public static Ingredient[] readIngredients(String filepath) {
        BufferedReader reader;
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        try {
            reader = new BufferedReader(new FileReader(filepath));
            String line = reader.readLine();
            line = reader.readLine(); // Skip header
            
            while (line != null) {
                // Regex taken from:
                // https://stackoverflow.com/questions/1757065/java-splitting-a-comma-separated-string-but-ignoring-commas-in-quotes
                
                String[] foodDetails = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                
                ingredients.add(new Ingredient(
                    Double.parseDouble(foodDetails[3]),
                    Double.parseDouble(foodDetails[4]),
                    Double.parseDouble(foodDetails[7]),
                    foodDetails[1],
                    Integer.parseInt(foodDetails[0]),
                    10 // Placeholder?
                ));

                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ingredients.toArray(new Ingredient[ingredients.size()]);
    }
}
