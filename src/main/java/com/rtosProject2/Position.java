package com.rtosProject2;

public class Position {
    private final String _train;
    private final int _row;
    private final int _col;

    public Position(String train, int row, int col) {
        _train = train;
        _row = row;
        _col = col;
    }

    public String train() {
        return _train;
    }

    public int row() {
        return _row;
    }

    public int col() {
        return _col;
    }
}
