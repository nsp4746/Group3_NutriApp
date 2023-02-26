package nutriapp;

public class Food {
    private double calories;
    private double protein;
    private double carbs;
    private String name;
    private int id;

    public Food(double calories, double protein, double carbs, String name, int id) {
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.name = name;
        this.id = id;
    }

    // Getters
    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getCarbs() {
        return carbs;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    // Setters
    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public void setCarbs(double carbs) {
        this.carbs = carbs;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    //////////////////////////////

    @Override
    public String toString() {
        return "[Name=" + name + ", Calories=" + calories + ", Protein=" + protein + ", Carbs=" + carbs +"]";
    }

    
}
