package com.group3.nutriapp.cli.states;

import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;

public class CLIStateMyTeam extends CLIState {
    public CLIStateMyTeam(CLI cli) { 
        super(cli, "My Team");
    }

    @Override public void run() {
        showLine("Not implemented!");

        this.addOptionDivider();
        this.addBackOption();
    }
}
