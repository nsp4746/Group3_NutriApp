package com.group3.nutriapp.cli.states;

import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;
import com.group3.nutriapp.model.User;
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

    @Override public void run() {
        User user = this.getOwner().getUser();

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
            // TODO: Implement
        });

        addOption("Set Weight", () -> {
            // TODO: Implement
        });

        addOption("Set Goal", () -> {
            // TODO: Implement
        });

        addOptionDivider();

        addOption("Change Password", () -> {
            String old = Crypto.makeSHA1(getInput("Confirm old password"));
            if (!old.equals(user.getPasswordHash())) {
                this.showError("Incorrect password!");
                return;
            }

            String newPassword = getInput("Enter new password");
            user.setPassword(Crypto.makeSHA1(newPassword));

            if (getOwner().getUserDatabase().updateUser(user) != null)
                this.showMessage("Successfully updated password!");
            else
                this.showError("Failed to update password!");
        });

        addOptionDivider();
        addBackOption();
    }
}
