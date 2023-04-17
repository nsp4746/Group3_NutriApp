package com.group3.nutriapp.cli.states;

import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;
import com.group3.nutriapp.model.User;
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

        // TODO: Implement logic and allow undoing operations

        this.showError("Unimplemented!");
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
            String goal = user.getGoal() == null ? "None" : user.getGoal().toString();
            showLine("Goal: " + goal);
    
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
