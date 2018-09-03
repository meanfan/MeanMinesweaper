package com.mean.meanminesweeper.core;

import java.util.ArrayList;

public class ArrayList2Dim<E> extends ArrayList {
    private int col;
    public E get(int x,int y){
        return (E)this.get(x*col+y);
    }
}
