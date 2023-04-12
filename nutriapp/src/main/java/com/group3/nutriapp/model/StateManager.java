package com.group3.nutriapp.model;

import java.util.LinkedList;
import java.util.List;

/*
 * Purpose: class maintains a list of previous and current states
 * Useage:  addNewState method adds new state to list
 *          undoMostRecentState returns the value of the previous state, and removes the most recent state
 */
public class StateManager<T> {
    List<State<T>> stateList = new LinkedList<State<T>>();

    //Adds new state object to state list
    public void addNewState(T newState) {
        T currentState = stateList.get(stateList.size() - 1).getState();
        stateList.add(new State<T>(currentState, newState));
    }

    //Removes newest addition to stateList, and returns it's previous value
    public T undoMostRecentState() {
        T newCurrentState = stateList.get(stateList.size() - 1).getPreviousState();
        stateList.remove(stateList.size() - 1);
        return newCurrentState;
    }
}
