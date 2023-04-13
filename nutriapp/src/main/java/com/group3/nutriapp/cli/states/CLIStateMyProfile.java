package com.group3.nutriapp.cli.states;

import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;
import com.group3.nutriapp.model.Goal;
import com.group3.nutriapp.model.User;

/**
 * @author Aidan Ruiz + Group 3
 * @description CLI state that shows the user their current profile information
 * @date 4/11/23
 */
public class CLIStateMyProfile extends CLIState {
    public CLIStateMyProfile(CLI cli) { 
        super(cli, "My Profile");
        this.setTableWidth(40);
    }

    @Override public void run() {
        User user = this.getOwner().getUser();

        this.showHeader();

        this.showLine("Username: " + user.getName());
        this.showLine("Weight: " + user.getWeight());
        this.showLine("Height: " + user.getHeight());
        this.showLine("Age: " + user.getAge());

        this.showDivider(false);

        String goal = user.getGoal() == null ? "None" : user.getGoal().toString();
        this.showLine("Goal: " + goal);

        this.showDivider(false);

        final int OPTION_HEIGHT = 0, OPTION_WEIGHT = 1, OPTION_GOAL = 2, OPTION_BACK = 3;
        this.showMenu(new String[] { "Set Height", "Set Weight", "Set Goal", "$DIVIDER", "Back" });

        int command = this.getOptionIndex();
        if (command == -1) return;

        // TODO: Implement setting height/weight/goal
        switch (command) {
            case OPTION_BACK: {
                this.getOwner().pop();
                break;
            }
            default: {
                this.showError("Invalid command!");
                break;
            }
        }
    }
}
