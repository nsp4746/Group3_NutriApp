package com.group3.nutriapp.cli.states;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;
import com.group3.nutriapp.model.Day;
import com.group3.nutriapp.model.Meal;
import com.group3.nutriapp.model.User;
import com.group3.nutriapp.model.Workout;
import com.group3.nutriapp.persistence.HistoryFileDAO;

public class CLIStateViewHistory extends CLIState {
    /**
     * Persistent history storage.
     */
    private HistoryFileDAO dao;

    /**
     * The currently authenticated user.
     */
    private User user;

    /**
     * Cached history data.
     */
    private Day[] days;

    /**
     * The index into the array that we're currently viewing.
     */
    private int index = 0;
    
    public CLIStateViewHistory(CLI cli) {
        super(cli, "My History");
        this.dao = cli.getHistoryDatabase();
        this.user = cli.getUser();

        // Reverse the days array, so most recent is shown first
        List<Day> days = Arrays.asList(this.dao.getUserDayArray(this.user.getId()));
        Collections.reverse(days);
        this.days = days.toArray(Day[]::new);
    }

    /**
     * Prints the currently selected day to the console.
     */
    public void showHistoryData() {
        Day day = days[index];

        // User statistics for the current day
        showLine(String.format("Day: %s (%d/%d)", day.getDate().format(DateTimeFormatter.ISO_DATE), index + 1, days.length));
        showLine("Weight: " + day.getWeight());
        showLine("Calories Consumed: " + day.getCalorieIntake());
        showLine("Calorie Goal: " + day.getCalorieGoal());
        showDivider(false);

        // Meals consumed this day
        showLine("Meals");
        showDivider(true);
        int mealCount = 0;
        for (Meal meal : day.getMeals()) {
            showLine(meal.getName());
            mealCount++;
        }
        if (mealCount == 0)
            showLine("No meals prepared");
        showDivider(false);

        // Workouts performed this day
        showLine("Workouts");
        showDivider(true);
        int workoutCount = 0;
        for (Workout workout : day.getWorkouts()) {
            showLine(workout.toString());
            workoutCount++;
        }
        if (workoutCount == 0)
            showLine("No workouts performed");
        showDivider(false);

        // If we can, allow going to the next page
        if (index + 1 < days.length)
            addOption("Next", () -> index = index + 1);

        // If we can, allow going to the previous page
        if (index - 1 >= 0)
            addOption("Previous", () -> index = index - 1);
    }

    /**
     * Fixup the size of the table before rendering,
     * to make sure it fits all entries.
     */
    @Override public void prerun() {
        Day day = days[index];
        // No need to format if there's no day
        if (day == null) return;
        
        // Fixup size based on workout strings
        for (Workout workout : day.getWorkouts()) fit(workout.toString());
        // Fixup size based on meal strings
        for (Meal meal : day.getMeals()) fit(meal.getName());
    }

    /**
     * Shows the user their history, with
     * options to go back and forth between entries.
     */
    @Override public void run() {
        if (days.length == 0) {
            // If there's no days, we can't show any history
            showLine("There is no history to show");
        } else showHistoryData();

        // Default functionality for going back to previous page
        addOptionDivider();
        addBackOption();
    }
    
}
