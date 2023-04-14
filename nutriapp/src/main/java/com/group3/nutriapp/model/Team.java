package com.group3.nutriapp.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Team {
    public static final String STRING_FORMAT = "Team [id=%d, size=%d, members=%s]";
    @JsonProperty("id") private int id;
    @JsonProperty("size") private int size;
    @JsonProperty("members") private ArrayList<Integer> teamMembers = new ArrayList<Integer>();

    public Team(@JsonProperty("id") int id, @JsonProperty("size") int size, @JsonProperty("members") ArrayList<Integer> members){
        this.id = id;
        this.size = size;
        this.teamMembers = members;
    }

    public int getId() {return id;}

    public int getSize() {return size;}

    public ArrayList<Integer> getMembers() {return teamMembers;}

    public boolean addMember(int member){
        teamMembers.add(member);
        size = teamMembers.size();
        return true;
    }

    public String toString(){
        return String.format(STRING_FORMAT, id, size, teamMembers);
    }
}
