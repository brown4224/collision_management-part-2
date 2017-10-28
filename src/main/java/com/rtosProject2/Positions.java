package com.rtosProject2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Positions {

    private final ArrayList<Position> _positions = new ArrayList<>();

    public Positions(String[][][] trainPositions) {
        summarizePositions(trainPositions);
    }

    public List<Position> getPositions() {
        return Collections.unmodifiableList(_positions);
    }

    public Position get(int idx) {
        return _positions.get(idx);
    }

    public int size() {
        return _positions.size();
    }

    public static Message convertToMessage(String[][][] trainPositions) {
        Positions positions = new Positions(trainPositions);
        Message message = new Message<>(Message.PayloadType.Position, positions);
        return message;
    }

    /**
     * The data ProcessB takes from bufferAB is summarized from a 3D array of planes into
     * the format specified by the assignment:
     * TRAIN | ROW | COL
     * X     | 0   | 0
     * @param state
     * @return
     */
    private void summarizePositions(String[][][] state) {
//        Object[][] summary = new Object[state.length][3];
        boolean brk = false;
        for (int plane = 0; plane < state.length; plane++) {
            for (int row = 0; row < Plane.rows; row++) {
                for (int col = 0; col < Plane.cols; col++) {
                    if(!Objects.equals(state[plane][row][col], "")) {
                        _positions.add(new Position(state[plane][row][col], row, col));
//                        summary[plane][0] = state[plane][row][col];
//                        summary[plane][1] = row;
//                        summary[plane][2] = col;
                        System.out.println(state[plane][row][col] + " : " + row + " : " + col);
                        brk = true;
                        break;
                    }
                }
                if (brk) break;
            }
            brk = false;
        }
    }
}
