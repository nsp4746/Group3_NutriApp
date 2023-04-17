package com.group3.nutriapp.model;

/*
 * Keeps track of one state of the system
 * Can store any type of variable
 */
public class State<T> {
    private T prevState;
    private T currentState;

    /*
     * Constructor, remembers previous state, and current state
     */
    State(T prevState, T currentState) {
        this.prevState = prevState;
        this.currentState = currentState;
    }

    /*
     * @return current state
     */
    public T getState() {
        return currentState;
    }
    /*
     * @return previous state
     */
    public T getPreviousState() {
        return prevState;
    }
}