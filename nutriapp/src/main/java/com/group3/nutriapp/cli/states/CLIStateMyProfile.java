package com.group3.nutriapp.cli.states;

import com.group3.nutriapp.Control.DayObserver;
import com.group3.nutriapp.Control.Observer;
import com.group3.nutriapp.Control.TimeManager;
import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;
import com.group3.nutriapp.model.Combination;
import com.group3.nutriapp.model.GainWeight;
import com.group3.nutriapp.model.Goal;
import com.group3.nutriapp.model.LoseWeight;
import com.group3.nutriapp.model.MaintainWeight;
import com.group3.nutriapp.model.User;
import com.group3.nutriapp.model.Combination.Status;
import com.group3.nutriapp.persistence.HistoryFileDAO;
import com.group3.nutriapp.persistence.UserFileDAO;
import com.group3.nutriapp.util.Crypto;

/**
 * @author Aidan Ruiz + Group 3
 * @description CLI state that shows the user their current profile information
 * @date 4/11/23
 */
public class CLIStateMyProfile extends CLIState {
    public CLIStateMyProfile(CLI cli) { 
        super(cli, "My Profile");
    }

    /**
     * Event that triggers when user selections set goal option.
     */
    private void onSetGoal() {
        CLI cli = getOwner();
        User user = cli.getUser();
        UserFileDAO userDatabase = cli.getUserDatabase();
        final int   MAINTAIN_WEIGHT = 0, LOSE_WEIGHT = 1, GAIN_WEIGHT = 2,
                    COMBINATION_MAINTAIN = 3, COMBINATION_LOSE = 4, COMBINATION_GAIN = 5;

        // Reset the console because I figure it would add more readability
        cli.clear();

        // Just going to manually setup the options here
        System.out.println("You may choose one of the following goals:");
        this.showDivider(false);
        this.showLine("[0] Maintain Weight");
        this.showLine("[1] Lose Weight");
        this.showLine("[2] Gain Weight");
        this.showLine("[3] Fitness + Maintain Weight");
        this.showLine("[4] Fitness + Lose Weight");
        this.showLine("[5] Fitness + Gain Weight");
        this.showDivider(false);

        // Standard choice selection logic
        int choice = this.getOptionIndex();
        if (choice == -1) return;
        if (choice > 5 || choice < 0) {
            this.showError("Invalid choice!");
            return;
        }

        double targetWeight = user.getWeight();
        // If the user doesn't want to maintain their current weight,
        // we should probably prompt them for what their target weight actually is.
        if (choice != COMBINATION_MAINTAIN && choice != MAINTAIN_WEIGHT)
            targetWeight = this.getInputDouble("Enter target weight");

        Goal goal = null;
        switch (choice) {
            case MAINTAIN_WEIGHT: goal = new MaintainWeight(targetWeight); break;
            case LOSE_WEIGHT: goal = new LoseWeight(targetWeight); break;
            case GAIN_WEIGHT: goal = new GainWeight(targetWeight); break;
            case COMBINATION_MAINTAIN: goal = new Combination(targetWeight, Status.maintain); break;
            case COMBINATION_LOSE: goal = new Combination(targetWeight, Status.lose); break;
            case COMBINATION_GAIN: goal = new Combination(targetWeight, Status.gain); break;
        }

        // TODO: Allow undoing operations
        user.setGoal(goal);
        userDatabase.updateUser(user);
        
        this.showMessage("Successfully updated goal!");
    }

    @Override public void run() {
        CLI cli = getOwner();
        User user = cli.getUser();
        UserFileDAO userDatabase = cli.getUserDatabase();


    
        // User details section
        {
            showLine("Username: " + user.getName());
            showLine("Weight: " + user.getWeight());
            showLine("Height: " + user.getHeight());
            showLine("Age: " + user.getAge());
    
            showDivider(false);
        }
        
        // Goal data section
        {
            Goal goal = user.getGoal();
            showLine("Goal: " + (goal == null ? "None" : goal.toString()));

            if (goal != null) {
                this.showLine("Target Weight: " + goal.getTargetWeight());
                this.showLine("Target Calories: " + goal.getTargetCalories());
            }

            showDivider(false);
        }

        addOption("Set Height", () -> {
            double height = this.getInputDouble("Enter height");
            user.setHeight(height);

            userDatabase.updateUser(user);
            this.showMessage("Successfully updated height!");
        });

        addOption("Set Weight", () -> {
            double weight = this.getInputDouble("Enter weight");
            user.setWeight(weight);

            userDatabase.updateUser(user);
            this.showMessage("Successfully updated weight!");
        });

        addOption("Set Goal", this::onSetGoal);

        addOptionDivider();

        addOption("Change Password", () -> {
            String old = Crypto.makeSHA1(getInput("Confirm old password"));
            if (!old.equals(user.getPasswordHash())) {
                this.showError("Incorrect password!");
                return;
            }

            String newPassword = getInput("Enter new password");
            user.setPassword(Crypto.makeSHA1(newPassword));

            if (cli.getUserDatabase().updateUser(user) != null)
                this.showMessage("Successfully updated password!");
            else
                this.showError("Failed to update password!");
        });

        addOptionDivider();
        addBackOption();
    }
}
