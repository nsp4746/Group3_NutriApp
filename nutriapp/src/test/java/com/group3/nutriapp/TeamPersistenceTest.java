package com.group3.nutriapp;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.group3.nutriapp.model.Team;
import com.group3.nutriapp.persistence.TeamFileDAO;

public class TeamPersistenceTest {

    //@Test
    public void storeTeam(){

        ArrayList<Integer> members = new ArrayList<>();
        members.add(1);
        members.add(2);
        members.add(3);

        TeamFileDAO dao = new TeamFileDAO();

        dao.addTeam(members);
    }

    public static void main(String[] args) {
        TeamFileDAO dao = new TeamFileDAO();
        System.out.println(dao.getTeamArray());
    }
}
