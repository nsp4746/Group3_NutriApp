package com.group3.nutriapp.model;

import java.util.LinkedList;
import java.util.List;

/*
 * Keeps track of all previous states through a list
 * Is able to keep tack of any variable through T type
 */
public class StateManager<T> {
    List<State<T>> stateList = new LinkedList<State<T>>();

    /*
     * Adds new state to the state list
     * @param newState, new state of system
     */
    public void addNewState(T newState) {
        T currentState = stateList.get(stateList.size() - 1).getState();
        stateList.add(new State<T>(currentState, newState));
    }

    /*
     * Returns current state
     * @return T type variable of current state
     */
    public T getCurrentState() {
        return stateList.get(stateList.size() - 1).getState();
    }

    /*
     * Removes current state, and returns new current state
     * @return returns new current state of system
     */
    public T undoMostRecentState() {
        T newCurrentState = stateList.get(stateList.size() - 1).getPreviousState();
        stateList.remove(stateList.size() - 1);
        return newCurrentState;
    }
}
