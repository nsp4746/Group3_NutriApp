package com.group3.nutriapp.cli.states;

import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;
import com.group3.nutriapp.model.Combination;
import com.group3.nutriapp.model.GainWeight;
import com.group3.nutriapp.model.Goal;
import com.group3.nutriapp.model.LoseWeight;
import com.group3.nutriapp.model.MaintainWeight;
import com.group3.nutriapp.model.User;
import com.group3.nutriapp.model.Combination.Status;
import com.group3.nutriapp.persistence.UserFileDAO;
import com.group3.nutriapp.util.Crypto;

/**
 * @author Aidan Ruiz + Group 3
 * @description CLI state that shows the user their current profile information
 * @date 4/11/23
 */
public class CLIStateMyProfile extends CLIState {
    /**
     * Persistent user storage.
     */
    private UserFileDAO dao;

    /**
     * The currently logged in user.
     */
    private User user;

    public CLIStateMyProfile(CLI cli) { 
        super(cli, "My Profile");
        dao = cli.getUserDatabase();
        user = cli.getUser();
    }

    /**
     * Event that triggers when user selections set goal option.
     * Gives the user the option to choose between different goal types,
     * and updates their profile accordingly.
     */
    private void onSetGoal() {
        CLI cli = getOwner();
        final int   MAINTAIN_WEIGHT = 0, LOSE_WEIGHT = 1, GAIN_WEIGHT = 2,
                    COMBINATION_MAINTAIN = 3, COMBINATION_LOSE = 4, COMBINATION_GAIN = 5;

        // Reset the console because I figure it would add more readability
        cli.clear();

        // Just going to manually setup the options here
        System.out.println("You may choose one of the following goals:");
        showDivider(false);
        showLine("[0] Maintain Weight");
        showLine("[1] Lose Weight");
        showLine("[2] Gain Weight");
        showLine("[3] Fitness + Maintain Weight");
        showLine("[4] Fitness + Lose Weight");
        showLine("[5] Fitness + Gain Weight");
        showDivider(false);

        // Standard choice selection logic
        int choice = getOptionIndex();
        if (choice == -1) return;
        if (choice > 5 || choice < 0) {
            showError("Invalid choice!");
            return;
        }

        double targetWeight = user.getWeight();
        // If the user doesn't want to maintain their current weight,
        // we should probably prompt them for what their target weight actually is.
        if (choice != COMBINATION_MAINTAIN && choice != MAINTAIN_WEIGHT)
            targetWeight = getInputDouble("Enter target weight");

        Goal goal = null;
        switch (choice) {
            case MAINTAIN_WEIGHT: goal = new MaintainWeight(targetWeight); break;
            case LOSE_WEIGHT: goal = new LoseWeight(targetWeight); break;
            case GAIN_WEIGHT: goal = new GainWeight(targetWeight); break;
            case COMBINATION_MAINTAIN: goal = new Combination(targetWeight, Status.maintain); break;
            case COMBINATION_LOSE: goal = new Combination(targetWeight, Status.lose); break;
            case COMBINATION_GAIN: goal = new Combination(targetWeight, Status.gain); break;
        }

        // Copy current calories from the old goal if it exists
        if (user.getGoal() != null)
            goal.addCurrentCalories(user.getGoal().getCurrentCalories());

        // TODO: Allow undoing operations
        user.setGoal(goal);
        dao.updateUser(user);
        
        showMessage("Successfully updated goal!");
    }

    /**
     * Event that triggers when the user opts to change their password.
     * Prompts to confirm old password, if it's correct, ask for and
     * set a new password.
     */
    public void onChangePassword() {
        CLI cli = getOwner();

        String old = Crypto.makeSHA1(getInput("Confirm old password"));
        if (!old.equals(user.getPasswordHash())) {
            showError("Incorrect password!");
            return;
        }

        String newPassword = getInput("Enter new password");
        user.setPassword(Crypto.makeSHA1(newPassword));

        if (cli.getUserDatabase().updateUser(user) != null)
            showMessage("Successfully updated password!");
        else
            showError("Failed to update password!");
    }

    /**
     * Event that triggers when the user opts to change their height.
     * Prompts to give a new height and updates it in the database.
     */
    public void onSetHeight() {
        double height = getInputDouble("Enter height");
        user.setHeight(height);

        dao.updateUser(user);
        showMessage("Successfully updated height!");
    }

    /**
     * Event that triggers when the user opts to change their weight.
     * Prompts to give a new weight and updates it in the database.
     */
    public void onSetWeight() {
        double weight = getInputDouble("Enter weight");
        user.setWeight(weight);

        dao.updateUser(user);
        showMessage("Successfully updated weight!");
    }

    /**
     * Sets up all the options that the user
     * can select to manipulate their own profile.
     */
    @Override public void run() {
        // This section shows typical details about the user.
        showLine("Username: " + user.getName());
        showLine("Weight: " + user.getWeight());
        showLine("Height: " + user.getHeight());
        showLine("Age: " + user.getAge());
        showDivider(false);

        // This section shows information about the user's current goal.
        Goal goal = user.getGoal();
        showLine("Goal: " + (goal == null ? "None" : goal.toString()));
        if (goal != null) {
            showLine("Target Weight: " + goal.getTargetWeight());
            showLine("Target Calories: " + goal.getTargetCalories());
        }
        showDivider(false);

        // Give option to view history
        addOption("View History", () -> getOwner().push(new CLIStateViewHistory(getOwner(), user, false)));
        addOptionDivider();
        // Give the options to actually modify the user's profile
        addOption("Set Height", this::onSetHeight);
        addOption("Set Weight", this::onSetWeight);
        addOption("Set Goal", this::onSetGoal);
        addOptionDivider();
        addOption("Change Password", this::onChangePassword);

        // Default functionality for going back to previous page
        addOptionDivider();
        addBackOption();
    }
}
