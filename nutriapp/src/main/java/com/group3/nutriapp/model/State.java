package com.group3.nutriapp.model;

/*
 * How it works:
 * Just stores state of a <T> variable
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