package com.group3.nutriapp.persistence;

import com.group3.nutriapp.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CSVReader {
    /**
     * Attempts to parse a double from a string, if it's empty, return 0
     * @param value Value that contains a double
     * @return Parsed double value
     */
    private static double parseDouble(String value) {
        if (value.isEmpty()) return 0.0;
        return Double.parseDouble(value);
    }

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
                    parseDouble(foodDetails[3]),
                    parseDouble(foodDetails[4]),
                    parseDouble(foodDetails[7]),
                    parseDouble(foodDetails[5]),
                    parseDouble(foodDetails[8]),
                    foodDetails[1].replace("\"", ""),
                    Integer.parseInt(foodDetails[0]),
                    0 // Docs say stock starts at 0
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
