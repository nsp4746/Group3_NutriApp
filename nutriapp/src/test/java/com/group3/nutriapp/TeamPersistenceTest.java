package com.group3.nutriapp;

import java.util.ArrayList;

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
