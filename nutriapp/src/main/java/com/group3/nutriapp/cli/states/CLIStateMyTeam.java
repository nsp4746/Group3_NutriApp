package com.group3.nutriapp.cli.states;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.group3.nutriapp.cli.CLI;
import com.group3.nutriapp.cli.CLIState;
import com.group3.nutriapp.model.Team;
import com.group3.nutriapp.model.User;
import com.group3.nutriapp.persistence.TeamFileDAO;
import com.group3.nutriapp.persistence.UserFileDAO;

public class CLIStateMyTeam extends CLIState {
    /**
     * The currently logged in user's team.
     */
    private Team team;

    public CLIStateMyTeam(CLI cli) { 
        super(cli, "My Team");
    }

    /**
     * Event that triggers when user selects option to view workout history.
     */
    private void onViewWorkoutHistory() {
        this.showError("This is currently unimplemented!");
    }

    /**
     * Event that triggers when user selects option to issue challenge.
     */
    private void onIssueChallenge() {
        // Don't issue a challenge if one is already in progress.
        if (team.checkChallenge()) {
            this.showError("A challenge is already in progress!");
            return;
        }

        if (team.challenge()) {
            this.getOwner().getTeamDatabase().updateTeam(team);
            this.showMessage("Successfully issued challenge for the team!");
        } 
        else this.showError("Failed to issue challenge!");
    }

    /**
     * Event that triggers when user selects option to invite members.
     */
    private void onInviteMember() {
        CLI cli = this.getOwner();
        User user = cli.getUser();
        UserFileDAO userDatabase = cli.getUserDatabase();
        TeamFileDAO teamDatabase = cli.getTeamDatabase();

        String username = this.getInput("Enter username to invite");
        if (username.isEmpty()) return;
        User invitee = userDatabase.getUser(username);
        if (invitee == null) {
            this.showError("User doesn't exist!");
            return;
        }

        if (invitee.getId() == user.getId()) {
            this.showError("Can't invite yourself to a team!");
            return;
        }

        if (teamDatabase.checkMembership(invitee.getId())) {
            this.showError("User is already in a team!");
            return;
        }

        if (invitee.hasRequestFromUser(user.getId())) {
            this.showError("You've already invited this user to your team!");
            return;
        }

        // TODO: Invites need to be undoable?
        invitee.addRequest(user.getId());

        userDatabase.updateUser(invitee);
        this.showMessage("Successfully sent invite to user!");
    }

    /**
     * Options for when the user isn't currently on a team.
     */
    private void doIndividualOptions() {
        CLI cli = this.getOwner();
        User user = cli.getUser();
        UserFileDAO userDatabase = cli.getUserDatabase();
        TeamFileDAO teamDatabase = cli.getTeamDatabase();

        this.showLine("You are not currently in a team!");
        this.showDivider(false);

        // If we have pending requests, print each user that has invited us as options.
        if (user.hasPendingRequiests()) {
            this.showLine("Pending Invites");

            for (int inviterID : user.getRequests()) {
                User inviter = userDatabase.getUser(inviterID);
                this.addOption(inviter.getName(), () -> {
                    Team inviterTeam = teamDatabase.getUserTeam(inviterID);
                    if (inviterTeam == null) {
                        this.showError("User's team no longer exists!");
                        return;
                    }

                    inviterTeam.addMember(user.getId());
                    if (teamDatabase.updateTeam(inviterTeam) != null) {
                        this.showMessage("Succesfully joined team!");
                        user.clearAllRequests();
                        userDatabase.updateUser(user);
                    }
                    else
                        this.showError("Failed to join team!");

                });
            }

            this.addOptionDivider();
        }
        

        this.addOption("Form Team", () -> {
            ArrayList<Integer> memberIDs = new ArrayList<>();
            memberIDs.add(user.getId());
            if (teamDatabase.addTeam(memberIDs) != null) {
                // Clear pending requests since we've formed our own team
                user.clearAllRequests();
                userDatabase.updateUser(user);

                this.showMessage("Successfully formed team!");
            }
            else
                this.showError("Failed to form team!");
        });
    }

    /**
     * Options for when the user is on a team.
     */
    private void doTeamOptions() {
        CLI cli = this.getOwner();
        UserFileDAO userDatabase = cli.getUserDatabase();
        TeamFileDAO teamDatabase = cli.getTeamDatabase();

        // If there's a chellenge in progress,
        // print what day we're
        if (team.checkChallenge()) {
            // Should it be one based?
            long day = ChronoUnit.DAYS.between(team.getChallenge(), LocalDateTime.now());
            this.showLine("Challenge in Progress: Day " +  day);
            this.showDivider(false);
        }

        // Print member list
        // TODO: Sort users based on their rank if a challenge is in place?
        this.showLine("Members");
        for (int memberID : team.getMembers()) {
            User member = userDatabase.getUser(memberID);
            if (member != null)
                this.showLine(String.format("- %s", member.getName()));
        }

        this.showDivider(false);

        this.addOption("View Member's Workout History", this::onViewWorkoutHistory);
        this.addOption("Invite Member", this::onInviteMember);
        this.addOption("Issue Challenge", this::onIssueChallenge);

        this.addOption("Leave Team", () -> {
            User user = cli.getUser();
            if (team.removeMember(user.getId())) {
                teamDatabase.updateTeam(team);

                // If there are no members left in the team
                // dissolve it.
                if (team.getMembers().size() == 0)
                    teamDatabase.deleteTeam(team.getId());
                
                this.showMessage("Successfully left team!");
            } else {
                this.showError("Failed to leave team!");
            }
        });
    }

    @Override public void run() {
        CLI cli = this.getOwner();
        User user = cli.getUser();
        team = cli.getTeamDatabase().getUserTeam(user.getId());

        // Do different menus based on whether or not we're in a team.
        if (team != null) this.doTeamOptions();
        else this.doIndividualOptions();

        this.addOptionDivider();
        this.addBackOption();
    }
}
