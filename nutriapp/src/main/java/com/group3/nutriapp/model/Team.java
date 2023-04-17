package com.group3.nutriapp.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.group3.nutriapp.io.LocalDateTimeSerializer;
import com.group3.nutriapp.io.LocalDateTimeDeserializer;

public class Team {
    public static final String STRING_FORMAT = "Team [id=%d, size=%d, members=%s, challenge=%s]";
    
    @JsonProperty("id") private int id;
    @JsonProperty("members") private ArrayList<Integer> teamMembers = new ArrayList<>();

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonProperty("challenge") 
    private LocalDateTime challenge;

    public Team(@JsonProperty("id") int id, @JsonProperty("members") ArrayList<Integer> members){
        this.id = id;
        this.teamMembers = members;
        this.challenge = null;
    }

    public int getId() {return id;}
    public ArrayList<Integer> getMembers() {return teamMembers;}
    public LocalDateTime getChallenge() { return this.challenge; }

    public boolean addMember(int member){
        teamMembers.add(member);
        return true;
    }

    public boolean removeMember(int member) {
        return teamMembers.remove((Integer) member);
    }

    public String toString(){
        return String.format(STRING_FORMAT, id, teamMembers.size(), teamMembers, challenge);
    }

    public boolean challenge() {
        if (challenge == null){
            challenge = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public boolean checkChallenge(){
        if (challenge == null) return false;

        LocalDateTime now = LocalDateTime.now();
        if (ChronoUnit.DAYS.between(challenge, now) > 7){
            challenge = null;
            return false;
        }
        
        return true;
    }
}
