package com.webcheckers.appl;

import java.util.ArrayList;
import java.util.HashMap;

public class Row {

    private HashMap<Space, Integer> row;
    private int index;

    public Row(int rowNumber, int length) {
        this.row = new HashMap<>();
        this.index = rowNumber;
        for(int i = 0; i < length; i++) {
            if ((rowNumber + i) % 2 == 0) {
                row.put(new Space(rowNumber, i, Space.Color.BLACK), i);
            } else {
                row.put(new Space(rowNumber, i, Space.Color.WHITE), i);
            }
        }
    }

    public HashMap<Space, Integer> getRow() {
        return this.row;
    }

    public int index() {
        return this.index;
    }

    public ArrayList<Space> iterator() {
        ArrayList<Space> spaces = new ArrayList<>();
        for (Space s: row.keySet()){
            spaces.add(s);
        }
        return spaces;
    }
}
