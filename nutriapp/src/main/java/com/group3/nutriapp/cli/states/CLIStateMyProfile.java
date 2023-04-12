package com.group3.nutriapp.cli.states;

import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;
import com.group3.nutriapp.model.User;

/**
 * @author Aidan Ruiz + Group 3
 * @description CLI state that shows the user their current profile information
 * @date 4/11/23
 */
public class CLIStateMyProfile extends CLIState {
    public CLIStateMyProfile(CLI cli) { super(cli, "My Profile"); }

    @Override public void run() {
        User user = this.getOwner().getUser();

        this.showHeader();

        this.showLine("Name: " + user.getName());
        this.showLine("Weight: " + user.getWeight());
        this.showLine("Height: " + user.getHeight());
        this.showLine("Age: " + user.getAge());
        
        this.showDivider(false);

        final int OPTION_BACK = 0;
        this.showMenu(new String[] { "Back" });

        int command = this.getOptionIndex();
        if (command == -1) return;

        if (command == OPTION_BACK)
            this.getOwner().pop();
        else
            this.showError("Invalid command!");
    }
}
