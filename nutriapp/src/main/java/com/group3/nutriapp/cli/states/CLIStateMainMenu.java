package com.group3.nutriapp.cli.states;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;
import com.group3.nutriapp.cli.states.CLIStateCreateFood.FoodType;
import com.group3.nutriapp.model.Day;
import com.group3.nutriapp.model.Goal;
import com.group3.nutriapp.model.Ingredient;
import com.group3.nutriapp.model.MaintainWeight;
import com.group3.nutriapp.model.Meal;
import com.group3.nutriapp.model.Recipe;
import com.group3.nutriapp.model.User;
import com.group3.nutriapp.model.Workout;
import com.group3.nutriapp.persistence.FoodFileDAO;
import com.group3.nutriapp.persistence.UserFileDAO;
import com.group3.nutriapp.util.Crypto;

/**
 * @author Aidan Ruiz + Group 3
 * @description CLI state that handles the main interaction with the application
 * @date 4/11/23
 */
public class CLIStateMainMenu extends CLIState {
    public CLIStateMainMenu(CLI cli) { super(cli, "NutriApp"); }

    /**
     * The last meal that the current user has prepared.
     */
    private Meal lastPreparedMeal = null;
    
    /**
     * The day the user prepared their last meal.
     */
    private Day dayLastMealWasPrepared = null;

    /**
     * Event that triggers when user opts to login.
     * 
     * Prompts the user for their username, if it exists, prompt for the password,
     * otherwise print error and return.
     * 
     * If password matches, log in and setup user object listeners,
     * otherwise print an error and return.
     */
    private void login() {
        CLI cli = getOwner();
        String username = getInput("Enter your username");
        UserFileDAO dao = cli.getUserDatabase();

        User user = dao.getUser(username);
        if (user == null) {
            showError("Username doesn't exist! Did you mean to register?");
            return;
        }

        String password = getInput("Enter your password");

        // We're sticking with simple SHA1 for password authentication.
        String hash = Crypto.makeSHA1(password);

        if (hash.equals(user.getPasswordHash())) {
            cli.setUser(user);
            showMessage("Successfully logged in!");
            return;
        }
    
        showError("Failed to login, password is incorrect!");
    }

    /**
     * Event that triggers when the user opts to register an account.
     * 
     * Prompts the user for a username to register with, if it exists, print an error and return.
     * If it doesn't, prompt the user for a password.
     * 
     * Once authentication details are setup, prompt the user for basic information about themselves,
     * including height, weight, and birthdate.
     * 
     * After gathering all details, persist the user to the database and login.
     * 
     * Starts the user with a default goal of maintaining their weight.
     */
    private void register() {
        CLI cli = getOwner();
        String username = getInput("Enter your username");
        User user = cli.getUserDatabase().getUser(username);
        if (user != null) {
            showError("Username already exists! Did you mean to login?");
            return;
        }

        String password = getInput("Enter your password");
        
        // Normally I'd do bcrypt or something similar, but I imagine
        // for this project, just a simple SHA1 is fine.
        String hash = Crypto.makeSHA1(password);

        // Now that we've gathered credentials, we need to get some basic account information
        // before we can actually create the user object.

        double height = getInputDouble("Enter your height");
        double weight = getInputDouble("Enter your weight");

        // Keep looping until the user provides a valid birthdate
        LocalDate birth = null;
        while (birth == null) {
            String input = getInput("Enter your birthdate (YYYY-MM-DD)");
            try { birth = LocalDate.parse(input); } 
            catch (DateTimeParseException ex) {
                showError("Not a valid date!");
                continue;
            }
        }

        // Create the user
        user = cli.getUserDatabase().addUser(username, height, weight, birth, hash);
        // Use maintain weight as a default goal
        user.setGoal(new MaintainWeight(weight));
        cli.getUserDatabase().updateUser(user);
        // Set the current user in the CLI
        cli.setUser(user);

        showMessage("Succesfully logged in!");
    }

    /**
     * Event that triggers when the user opts to logout.
     * 
     * Destroys the user session and stops the time manager.
     */
    private void logout() {
        getOwner().setUser(null);

        // Make sure next user that logs in doesn't share this state.
        lastPreparedMeal = null;
        dayLastMealWasPrepared = null;

        showMessage("Successfully logged out!");
    }

    /**
     * Event that triggers when the user opts to track a workout.
     * 
     * Prompts the user for various details about the workout, then 
     * saves it to the current day, as well as adjusting the goal calories.
     */
    private void onTrackWorkout() {
        int minutes = (int) getInputDouble("Enter number of minutes");
        if (minutes < 0) {
            showError("Can't log workout less than or equal to 0 minutes.");
            return;
        }

        String intensity = getInput("Enter intensity (high/medium/low)");
        double cpm = 0.0; // calories per minute
        switch (intensity.toUpperCase()) {
            case "HIGH": cpm = 10.0; break;
            case "MEDIUM": cpm = 7.5; break;
            case "LOW": cpm = 5.0; break;
            default: {
                showError("Invalid intensity entered!");
                return;
            }
        }

        CLI cli = getOwner();
        User user = cli.getUser();
        Goal goal = user.getGoal();
        UserFileDAO dao = cli.getUserDatabase();

        // Construct and add workout to current day
        Workout workout = new Workout(minutes, cpm, LocalDateTime.now());
        user.getDay().getWorkouts().add(workout);

        // I'm fairly sure workout is supposed to subtract from current
        // calories on our goal?
        if (goal != null)
            goal.subtractCurrentCalories((int) workout.getCaloriesBurned());

        dao.updateUser(user);

        showMessage("Succesfully logged workout!");
    }



    /**
     * Event that triggers when the user chooses to add an ingredient to the stock.
     * 
     * Pushes a searchable/selectable table state onto the stack,
     */
    private void onAddStock() {
        // Push a searchable table that can also be selected from.
        CLIStateSearchableTable.pushFoodSearchState(
            getOwner(), 
            "Ingredients", 
            getOwner().getFoodDatabase().getIngredientArray(), 
            true,
            this::onSelectIngredientForStock
        );
    }

    /**
     * Callback for when the user selects an item to add to their stock.
     * Prompts the user with how many of a certain ingredient they want to add to stock,
     * then persists the food item in the database.
     * @param index Index of the ingredient that was selected
     */
    private void onSelectIngredientForStock(int index) {
        CLI cli = getOwner();
        FoodFileDAO dao = cli.getFoodDatabase();
        Ingredient food = dao.getIngredientArray()[index];
        
        double count = getInputDouble("How many do you want to add to the stock?");
        if (count > 0) {
            food.setStockCount(food.getStockCount() + (int)count);
            dao.updateIngredient(food);
            showMessage("Successfully increased stock!");
        }
    }

    /**
     * Event that triggers when the user chooses to prepare a meal.
     */
    private void onPrepareMeal() {
        // Push a searchable table that can also be selected from.
        CLIStateSearchableTable.pushFoodSearchState(
            getOwner(), 
            "Meals", 
            getOwner().getFoodDatabase().getMealArray(), 
            false,
            this::onSelectMealForPreparation
        );
    }


    /**
     * Event that triggers when the user chooses to create a shopping list.
     */
    private void onCreateShoppingList() {
        // Push a searchable table that can also be selected from.
        CLIStateSearchableTable.pushFoodSearchState(
            getOwner(), 
            "Recipes", 
            getOwner().getFoodDatabase().getRecipeArray(), 
            false,
            this::onSelectRecipeForShoppingList
        );
    }

    /**
     * Callback for when the user selects a recipe to create a shopping list for
     * @param index The index of the recipe that was selected.
     */
    private void onSelectRecipeForShoppingList(int index) {
        CLI cli = getOwner();
        FoodFileDAO dao = cli.getFoodDatabase();
        Recipe recipe = dao.getRecipeArray()[index];

        // Compute the ingredient counts needed
        HashMap<Ingredient, Integer> usages = recipe.generateStockMap();
        // Store what ingredients are missing and their counts
        HashMap<Ingredient, Integer> missing = new HashMap<>();

        System.out.println("[*] Generating shopping list for " + recipe.getName());
        int ingredientsNeeded = 0;
        for (Ingredient ingredient : usages.keySet()) {
            int count = usages.get(ingredient);
            // Update ingredient with up to date one from database.
            ingredient = dao.getIngredient(ingredient.getId());

            // Print out the ingredients and the counts that we need
            // if they're low/out of stock.
            if (ingredient.getStockCount() < count) {
                ingredientsNeeded++;
                int diff = count - ingredient.getStockCount();
                missing.put(ingredient, diff);
                System.out.println(String.format("[*] x%d %s", diff, ingredient.getName()));
            }
        }

        // If there's no ingredients needed, just print a message saying so
        if (ingredientsNeeded == 0) {
            showMessage("It appears that you have everything needed to prepare this recipe!");
            return;
        }

        // Otherwise get confirmation if we want to just add everything to our stock.
        if (getConfirmation("Do you want to add all of these items to the stock?")) {
            // Add all missing stock to the database and flush
            for (Ingredient ingredient : missing.keySet()) {
                int diff = missing.get(ingredient);
                ingredient.setStockCount(ingredient.getStockCount() + diff);
                dao.updateIngredient(ingredient);
            }

            showMessage("Successfully added ingredients to the stock!");
        }
    }
    /**
     * Callback for when the user selects a meal to prepare.
     * @param index The index of the meal that was selected
     */
    private void onSelectMealForPreparation(int index) {
        CLI cli = getOwner();
        User user = cli.getUser();
        FoodFileDAO dao = cli.getFoodDatabase();
        Meal meal = dao.getMealArray()[index];
        Goal goal = user.getGoal();

        // Compute the ingredient counts needed
        HashMap<Ingredient, Integer> usages = meal.generateStockMap();

        // Check for each ingredient if we have the stock available
        for (Ingredient ingredient : usages.keySet()) {
            int count = usages.get(ingredient);
            // Get the updated ingredient from the database
            if (dao.getIngredient(ingredient.getId()).getStockCount() < count) {
                // If we don't have the necessary stock, warn the user and quit
                showMessage(String.format("Can't prepare meal because %s doesn't have necessary stock", ingredient.getName()));
                return;
            }
        }

        boolean isOverTarget = goal.getCurrentCalories() + meal.getCalories() > goal.getTargetCalories();

        // Warn the user if we're going to go over our target calorie count.
        if (isOverTarget) {
            boolean confirm = getConfirmation("Consuming this meal will put you over your target calorie count. Do you want to continue?");
            if (!confirm) {
                showMessage("Cancelling preparation of meal...");
                return;
            }
        }

        // Print out the instructions for each recipe
        for (Recipe recipe : meal.getRecipes()) {
            System.out.println("[*] To prepare " + recipe.getName());
            System.out.println("[*] " + recipe.getInstructions());
            System.out.println("[*]");
        }

        dayLastMealWasPrepared = user.getDay();
        lastPreparedMeal = meal;

        // Add current calories to both goal and day storage
        goal.addCurrentCalories((int) meal.getCalories());
        user.getDay().addCalorieIntake((int) meal.getCalories());
        user.getDay().getMeals().add(meal);

        // Deduct the ingredients from the stock
        for (Ingredient ingredient : usages.keySet()) {
            int count = usages.get(ingredient);
            // Update the ingredient with the real definition from the database
            ingredient = dao.getIngredient(ingredient.getId());
            // Deduct stock
            ingredient.setStockCount(ingredient.getStockCount() - count);
        }

        showMessage("Successfully prepared meal!");

        if (isOverTarget) {
            goal.setExcercises(); // Update recommended exercises
            Workout[] exercises = goal.getExcercises();
            if (exercises == null || exercises.length == 0) return;
            Workout workout = exercises[0];
            
            System.out.println("[*] However, it seems you're over your target goal, might I suggest the following exercise?");
            showMessage(String.format("%d minutes @ %.2fcpm", workout.getMinutes(), workout.getIntensity()));
        }
    }

    /**
     * Callback triggered when the user opts to export the database.
     * 
     * Prompts the user for a location to write the database, then does so.
     */
    private void onExportDatabase() {
        CLI cli = getOwner();

        Day[] days = cli.getHistoryDatabase().getDayArray();
        Ingredient[] ingredients = cli.getFoodDatabase().getIngredientArray();
        Meal[] meals = cli.getFoodDatabase().getMealArray();
        Recipe[] recipes = cli.getFoodDatabase().getRecipeArray();

        String path = getInput("Enter directory to export database");
        if (path.isEmpty()) return;
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                mapper.writeValue(new File(path, "ingredients.json"), ingredients);
                mapper.writeValue(new File(path, "meals.json"), meals);
                mapper.writeValue(new File(path, "recipes.json"), recipes);
                mapper.writeValue(new File(path, "history.json"), days);

                showMessage("Successfully exported database!");
            } catch (Exception ex) { showError("Failed to write files!"); }

            return;
        }

        showError("Directory doesn't exist!");
    }

    /**
     * Callback triggered when the user opts to import a database
     * 
     * Asks the user for the folder where they exported the database,
     * then reads it in.
     */
    private void onImportDatabase() {
        CLI cli = getOwner();

        String path = getInput("Enter directory where database files are");
        if (path.isEmpty()) return;
        File directory = new File(path);
        if (directory.exists() && directory.isDirectory()) {

            // Check that the files exist before we consider importing them.
            String[] files = {
                "history.json",
                "recipes.json",
                "meals.json",
                "ingredients.json"
            };

            for (String file : files) {
                if (!new File(directory, file).exists()) {
                    showError(file + " is missing from database folder!");
                    return;
                }
            }

            // Load the new databases specified
            if (!cli.getFoodDatabase().load(directory.getAbsolutePath())) {
                showError("Failed to load food databases!");
                return;
            }

            if (!cli.getHistoryDatabase().load(directory.getAbsolutePath())) {
                showError("Failed to load history database!");
                return;
            }

            showMessage("Successfully imported database!");

            return;
        }

        showError("Path either is not a directory or doesn't exist!");
    }

    /**
     * Event called when user undoes last meal.
     * Re-adds stock, and deducts calories.
     */
    private void onUndoLastMeal() {
        CLI cli = getOwner();
        User user = cli.getUser();
        FoodFileDAO dao = cli.getFoodDatabase();
        Goal goal = user.getGoal();

        HashMap<Ingredient, Integer> usages = lastPreparedMeal.generateStockMap();

        // Add current calories to both goal and day storage
        goal.subtractCurrentCalories((int) lastPreparedMeal.getCalories());
        user.getDay().addCalorieIntake(-((int) lastPreparedMeal.getCalories()));
        user.getDay().getMeals().remove(lastPreparedMeal);

        // Deduct the ingredients from the stock
        for (Ingredient ingredient : usages.keySet()) {
            int count = usages.get(ingredient);
            // Update the ingredient with the real definition from the database
            ingredient = dao.getIngredient(ingredient.getId());
            // Restore stock
            ingredient.setStockCount(ingredient.getStockCount() + count);
        }

        // Reset state of meals
        lastPreparedMeal = null;
        dayLastMealWasPrepared = null;

        showMessage("Successfully undone last meal!");
    }

    /**
     * Shows any notifications that the user may currently have.
     * This may include pending invites to a team.
     */
    private void displayNotifications() {
        User user = getOwner().getUser();
        boolean hasNotification = false;

        if (user.hasPendingRequests()) {
            showLine("*  You've been invited to a team!");
            hasNotification = true;
        }

        // Only print an extra UI divider if we had a notification.
        if (hasNotification)
            showDivider(false);
    }

    /**
     * Sets up all the options that the user or guest
     * can select to use the NutriApp client.
     */
    @Override public void run() {
        // Privileged and guest users have different options,
        // so show each accordingly.
        boolean isGuest = !isAuthenticated();
        boolean isUser = !isGuest;

        CLI cli = getOwner();

        // Show any notifications that may exist
        if (isUser) displayNotifications();

        // If we're a guest, provide options to login/register
        if (isGuest) {
            addOption("Login", this::login);
            addOption("Register", this::register);
        } else {
            // Otherwise, if we're logged in, allow pushing profile and team menus
            addOption("My Profile", () ->  cli.push(new CLIStateMyProfile(cli)));
            addOption("My Team", () -> cli.push(new CLIStateMyTeam(cli)));
        }

        // Section for browsing foodstuffs
        addOptionDivider();
        {
            addOption("Ingredients", () -> {
                Ingredient[] ingredients = cli.getFoodDatabase().getIngredientArray();
                CLIStateSearchableTable.pushFoodSearchState(cli, "Ingredients", ingredients, true, null);
            });
    
            addOption("Recipes", () -> {
                Recipe[] recipes = cli.getFoodDatabase().getRecipeArray();
                CLIStateSearchableTable.pushFoodSearchState(cli, "Recipes", recipes, false, null);
            });
    
            addOption("Meals", () -> {
                Meal[] meals = cli.getFoodDatabase().getMealArray();
                CLIStateSearchableTable.pushFoodSearchState(cli, "Meals", meals, false, null);
            });
        }
        addOptionDivider();

        if (isUser) {
            addOption("Create Recipe", () -> {
                String name = getInput("Enter name for recipe meal");
                if (name.isEmpty()) {
                    showError("Recipe name cannot be empty");
                    return;
                }
                
                if (cli.getFoodDatabase().findRecipe(name) != null) {
                    this.showError("Recipe already exists!");
                    return;
                }

                cli.push(new CLIStateCreateFood(cli, name, FoodType.RECIPE));
            });

            addOption("Create Meal", () -> {
                String name = getInput("Enter name for your meal");
                if (name.isEmpty()) {
                    showError("Meal name cannot be empty");
                    return;
                }

                if (cli.getFoodDatabase().findMeal(name) != null) {
                    this.showError("Meal already exists!");
                    return;
                }

                cli.push(new CLIStateCreateFood(cli, name, FoodType.MEAL));
            });
            
            addOptionDivider();
        }

        // Preparing meals, shopping lists, workout!
        if (isUser) {
            addOption("Add Stock", this::onAddStock);
            addOption("Track Workout", this::onTrackWorkout);
            addOption("Prepare Meal", this::onPrepareMeal);
            
            if (lastPreparedMeal != null) {
                // Only allow undoing on the same day,
                // just because we don't know the day ID
                // until it's over.
                Day day = cli.getUser().getDay();
                if (dayLastMealWasPrepared == day) {
                    addOption("Undo Last Meal", this::onUndoLastMeal);
                } else {
                    // Reset the state if the day has passed
                    dayLastMealWasPrepared = null;
                    lastPreparedMeal = null;
                }

            }

            addOption("Create Shopping List", this::onCreateShoppingList);
            addOptionDivider();
        }

        // For debugging purposes, allow users to set the length of a day.
        if (isUser) {
            addOption("Set Day Length", () -> {
                double seconds = getInputDouble("Enter day length in seconds");
                int ms = (int) Math.round(seconds * 1000.0);
                cli.getTimeManager().setDayLength(ms);
                showMessage("Successfully set day length!");
            });
            addOptionDivider();


            // Importing/exporting the database
            addOption("Export Database", this::onExportDatabase);
            addOption("Import Database", this::onImportDatabase);
            addOptionDivider();
        }

        if (isUser) 
            addOption("Logout", this::logout);

        addOption("Quit", () -> cli.quit());
    }
}
