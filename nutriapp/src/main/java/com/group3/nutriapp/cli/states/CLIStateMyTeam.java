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

/**
 * @author Aidan Ruiz + Group 3
 * @description CLI state that shows the user information about their current team
 * @date 4/11/23
 */
public class CLIStateMyTeam extends CLIState {
    /**
     * The currently logged in user's team.
     */
    private Team team;

    /**
     * Persistent team storage.
     */
    private TeamFileDAO teamDAO;

    /**
     * Persistent user storage.
     */
    private UserFileDAO userDAO;

    /**
     * The currently logged in user.
     */
    private User user;

    public CLIStateMyTeam(CLI cli) { 
        super(cli, "My Team");

        // Makes code more readable by not having to call CLI each time
        // to get the database data.
        this.teamDAO = cli.getTeamDatabase();
        this.userDAO = cli.getUserDatabase();
        this.user = cli.getUser();
    }

    /**
     * Event that triggers when user opts to view workout history.
     * 
     * Prompts the user for a username of someone on their team
     * If the user either doesn't exist or isn't on their team, show an error
     * 
     * Otherwise push a history state with only workout history.
     */
    private void onViewWorkoutHistory() {
        String username = getInput("Enter team member username");
        if (username.isEmpty()) return;
        
        User user = userDAO.getUser(username);
        if (user == null) {
            showError("Could not find user!");
            return;
        }

        if (!team.getMembers().contains(user.getId())) {
            showError("User is not on your team!");
            return;
        }

        // Push the view history state, except only allow workouts to be visible.
        CLI cli = getOwner();
        cli.push(new CLIStateViewHistory(cli, user, true));
    }

    /**
     * Event that triggers when user opts to issue a challenge.
     * 
     * Checks if a challenge is already in progress, if there is one,
     * show an error and return.
     * 
     * Otherwise start a challenge, set it to the current datetime, and update the database.
     */
    private void onIssueChallenge() {
        // Don't issue a challenge if one is already in progress.
        if (team.checkChallenge()) {
            showError("A challenge is already in progress!");
            return;
        }

        if (team.challenge()) {
            getOwner().getTeamDatabase().updateTeam(team);
            showMessage("Successfully issued challenge for the team!");
        } 
        else showError("Failed to issue challenge!");
    }

    /**
     * Event that triggers when user opts to invite a member to their team.
     * 
     * Prompts the user for a user to invite, if they don't exist, print an error and return.
     * 
     * Otherwise, perform checks on the user, such as if they're already in a team or if it's yourself,
     * if all of these checks pass successfully, an invite is sent to the user.
     */
    private void onInviteMember() {
        String username = getInput("Enter username to invite");
        if (username.isEmpty()) return;
        User invitee = userDAO.getUser(username);
        if (invitee == null) {
            showError("User doesn't exist!");
            return;
        }

        if (invitee.getId() == user.getId()) {
            showError("Can't invite yourself to a team!");
            return;
        }

        if (teamDAO.checkMembership(invitee.getId())) {
            showError("User is already in a team!");
            return;
        }

        if (invitee.hasRequestFromUser(user.getId())) {
            showError("You've already invited this user to your team!");
            return;
        }

        // TODO: Invites need to be undoable?
        invitee.addRequest(user.getId());

        userDAO.updateUser(invitee);
        showMessage("Successfully sent invite to user!");
    }

    /**
     * Event that triggers when the user opts to leave their team.
     * 
     * Removes the user from their team and updates their state in the database.
     * 
     * If there's no longer any members in their team after they leave, dissolves the
     * team and deletes it from the database.
     */
    private void onLeaveTeam() {
        // This conditional shouldn't fail because we've
        // already checked that they're in a team, but just being safe.
        if (team.removeMember(user.getId())) {
            teamDAO.updateTeam(team);

            // If there are no members left in the team
            // dissolve it.
            if (team.getMembers().size() == 0)
                teamDAO.deleteTeam(team.getId());
            
            showMessage("Successfully left team!");
        } else {
            showError("Failed to leave team!");
        }
    }

    /**
     * Event that triggers when the user opts to form their own team.
     * 
     * Creates and persists a team to a database.
     * Displays an error if it fails.
     */
    private void onFormTeam() {
        ArrayList<Integer> memberIDs = new ArrayList<>();
        memberIDs.add(user.getId());
        if (teamDAO.addTeam(memberIDs) != null) {
            // Clear pending requests since we've formed our own team
            user.clearAllRequests();
            userDAO.updateUser(user);

            showMessage("Successfully formed team!");
        }
        else
            showError("Failed to form team!");
    }

    /**
     * Prints the received invites to the console for the user to select,
     * if there are any.
     */
    private void doInvitesMenu() {
        showLine("Pending Invites");

        for (int inviterID : user.getRequests()) {
            User inviter = userDAO.getUser(inviterID);
            addOption(inviter.getName(), () -> {
                Team inviterTeam = teamDAO.getUserTeam(inviterID);
                if (inviterTeam == null) {
                    showError("User's team no longer exists!");
                    return;
                }

                inviterTeam.addMember(user.getId());
                if (teamDAO.updateTeam(inviterTeam) != null) {
                    showMessage("Succesfully joined team!");
                    user.clearAllRequests();
                    userDAO.updateUser(user);
                }
                else
                    showError("Failed to join team!");

            });
        }

        addOptionDivider();
    }

    /**
     * Shows menu for when the user isn't currently on a team.
     * - Indicates that they aren't on a team
     * - Shows invites if any exist
     * - An option to form your own team
     */
    private void doSoloMenu() {
        // Inform the user that they're not currently in a team.
        showLine("You are not currently in a team!");
        showDivider(false);

        // If we have pending requests, print each user that has invited us as options.
        if (user.hasPendingRequests())
            doInvitesMenu();
        
        addOption("Form Team", this::onFormTeam);
    }

    /**
     * Shows menu for when the user is on a team.
     * - Shows the current day of a challenge if one was issued
     * - Shows a list of members that are on your team
     * - Options to invite a member, view workout history, issue a challenge, or leave the team.
     */
    private void doTeamMenu() {
        // Update the challenge status, as well as printing the current
        // day of the challenge if we happen to be in one.
        if (team.checkChallenge()) {
            // Should it be one based?
            long day = ChronoUnit.DAYS.between(team.getChallenge(), LocalDateTime.now());
            showLine("Challenge in Progress: Day " +  day);
            showDivider(false);
        }

        // Print member list
        // TODO: Sort users based on their rank if a challenge is in place?
        showLine("Members");
        for (int memberID : team.getMembers()) {
            User member = userDAO.getUser(memberID);
            if (member != null)
                showLine(String.format("- %s", member.getName()));
        }

        showDivider(false);

        addOption("View Member's Workout History", this::onViewWorkoutHistory);
        addOption("Invite Member", this::onInviteMember);
        addOption("Issue Challenge", this::onIssueChallenge);
        addOption("Leave Team", this::onLeaveTeam);
    }

    /**
     * Sets up all the options that the user
     * can select to see information about their own team.
     * 
     * - View team status
     * - Accept invites to join a team if applicable.
     * - Form their own team if they aren't in one.
     */
    @Override public void run() {
        CLI cli = getOwner();

        // Set team in update loop because we may have changed status.
        team = cli.getTeamDatabase().getUserTeam(user.getId());

        // Do different menus based on whether or not we're in a team.
        if (team != null) doTeamMenu();
        else doSoloMenu();

        // Default functionality for going back to previous page
        addOptionDivider();
        addBackOption();
    }
}
