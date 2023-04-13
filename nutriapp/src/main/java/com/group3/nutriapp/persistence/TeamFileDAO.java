package com.group3.nutriapp.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group3.nutriapp.model.Team;

public class TeamFileDAO {
    public Map<Integer,Team> teams;
    private ObjectMapper objectMapper = new ObjectMapper();
    private int nextId;


    public TeamFileDAO() {
        this.load();
    }

    private int getNextId() { return nextId; }

    private boolean save() {
        Team[] teamArray = getTeamArray();
        try {objectMapper.writeValue(new File("data/teams.json"), teamArray);}
        catch (Exception ex) { return false; }
        return true;
    }

    private boolean load() {
        teams = new HashMap<Integer, Team>();

        Team[] teams;
        try { teams = objectMapper.readValue(new File("data/teams.json"), Team[].class);}
        catch (Exception ex) {return false; }

        for (Team team: teams) {
            int id = team.getId();
            this.teams.put(id, team);
            if (id > this.nextId)
                this.nextId = id;
        }

        this.nextId++;

        return true;
    }

    public Team[] getTeamArray() {
        Collection<Team> teams = this.teams.values();
        return teams.toArray(new Team[teams.size()]);
    }

    public Team addTeam(ArrayList<Integer> members){
        Team team = new Team(this.getNextId(), members.size(), members);
        this.teams.put(team.getId(), team);
        this.save();
        return team;
    }

    public Team updateTeam(Team team){
        if(!teams.containsKey(team.getId())){
            return null;
        }
        else{
            teams.put(team.getId(), team);
            save();
            return team;
        }
    }

    public boolean deleteTeam(int id){
        if(teams.containsKey(id)){
            teams.remove(id);
            return save();
        }
        else{
            return false;
        }
    }
}
