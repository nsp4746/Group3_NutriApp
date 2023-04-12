package com.group3.nutriapp.model;

/*
 * Keeps track of one state of the system
 * Can store any type of variable
 */
public class State<T> {
    private T prevState;
    private T currentState;

    State(T prevState, T currentState) {
        this.prevState = prevState;
        this.currentState = currentState;
    }

    public T getState() {
        return currentState;
    }

    public T getPreviousState() {
        return prevState;
    }
}