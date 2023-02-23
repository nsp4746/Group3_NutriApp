package persistance;

import nutriapp.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class CsvReader
{
    /*
     * Stub
     * Returns array of ingredients from filepath csv file
     */
    public static Ingredient[] readIngredients(String filepath) {
        BufferedReader reader;
        ArrayList<Ingredient> ingredients = new ArrayList<>();


        try{
            reader = new BufferedReader(new FileReader(filepath));
            String line = reader.readLine();
            Ingredient ingredient;

            while (line != null) {
                String[] foodDetails = line.split(",");
                ingredient = new Ingredient(foodDetails[1], Integer.parseInt(foodDetails[0]), Double.parseDouble(foodDetails[4]), Double.parseDouble(foodDetails[7]), Integer.parseInt(foodDetails[3]));
                ingredients.add(ingredient);
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Ingredient[] ingredientsArray = new Ingredient[ingredients.size()];
        System.arraycopy(ingredients.toArray(), 0, ingredientsArray, 0, ingredients.size());
        return ingredientsArray;
    }
}