package com.jeff;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

/**
 * CLASS ProcessC
 *
 * ProcessC takes summary data from bufferCD and determines if a collision
 * occurred. This implementation also creates a composite view of all
 * trains in the display grid to visually show all the trains in one plane and
 * show collisions in red (plus a beep).
 * All data from ProcessC is shown in the grid and logged to the console.
 */
public class ProcessC extends ProcessBase {

    private final DoubleBuffer<Object[][]> _bufferCD;
    private Plane _statusPlane;
    private Display _display;
    private int _second = 2;
    private int _collisions = 0;

    /**
     * Constructor that accepts the the bufferCD summary data
     * @param bufferCD
     * @param display
     * @param console
     */
    public ProcessC(DoubleBuffer<Object[][]> bufferCD,
                    Display display,
                    Console console) {
        super(console);
        _bufferCD = bufferCD;
        _display = display;
        _statusPlane = display.GetStatusPlane();
    }

    /**
     * Starts the ProcessA thread. Runs until bufferCD is shutdown by ProcessB.
     * Each time a value is pulled, updates the state of the composite view
     * and notes whether a collision occurred.
     */
    @Override
    public void run() {

        ConsoleWriteLine("PROCESS C STARTED\r\n");

        Object[][] state;

        while (!_bufferCD.isShutdown()) {
            state = _bufferCD.pull();

            collision_management(clone_array(state));

            System.out.println("C pulled: " + state.length);
            ConsoleWriteLine("SECOND " + _second);
            OutputState(state);
            ShowState(state);
            _second++;
        }
        String finMsg = "DONE. " + _collisions + " collisions occurred over " + _display.Seconds + " seconds.";
        _display.UpdateStatus(finMsg);
        ConsoleWriteLine(finMsg);
        System.out.println("BufferCD completed");
        ConsoleWait();
    }

    /**
     * Accepts the summary array and writes the data to the console.
     * @param state
     */
    private void OutputState(Object[][] state) {
        for (int plane = 0; plane < state.length; plane++) {
            ConsoleWriteLine(state[plane][0] + " | " + state[plane][1] + " | " + state[plane][2]);
        }
        ConsoleWriteLine();
        try {
            _display.Refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void collision_management(Object[][] state){
        //~ Brings state to current value of Process A
        simulate_movement(state, -1, 0, 1);

        System.out.println("process C pulling state");

        int look_ahead = Plane.rows;
        int collision = simulate_movement( clone_array(state), -1, 0, look_ahead);
        int halt_x = simulate_movement(clone_array(state), 0, 0, look_ahead);
        int halt_y = simulate_movement(clone_array(state), 1, 0, look_ahead);
        int halt_z = simulate_movement(clone_array(state), 2, 0, look_ahead);
        int min = collision;
        if (halt_x > collision && halt_x >= halt_y && halt_x >= halt_z)
            Plane.haltX = true;
        if (halt_y > collision && halt_y > halt_x && halt_y >= halt_z) {
            Plane.halty = true;
            System.out.println("C halts Y");
        }
        if (halt_z > collision && halt_z > halt_x && halt_z > halt_y)
            Plane.haltz = true;



    }

    private int simulate_movement(Object[][] state, int halt_plane, int counter, int target){

        if(     (state[0][1] == state[1][1] && state[0][2] == state[1][2]) ||
                (state[0][1] == state[2][1] && state[0][2] == state[2][2]) ||
                (state[1][1] == state[2][1] && state[1][2] == state[2][2]) ||
                counter >= target  // Target stops X many moves ahead
                )
            return counter;

        counter++;
        if(halt_plane != 0) {
            state[0][1] = ((int) state[0][1] + 1) % Plane.rows;
            state[0][2] = ((int) state[0][2] + 1) % Plane.cols;
        }
        if(halt_plane != 1) {
            state[1][1] = ((int) state[1][1] + 1) % Plane.rows;
        }
        if(halt_plane != 2) {
            state[2][2] = ((int) state[2][2] + 1) % Plane.cols;
        }
        return simulate_movement(state, halt_plane, counter, target);
    }
    private Object[][] clone_array(Object[][] state){
        Object[][] clone = new Object[state.length][state[0].length];
        for (int i = 0; i < state.length; i++ )
            for (int j = 0; j < state[0].length; j++)
                clone[i][j] = state[i][j];

        return clone;
    }

    /**
     * Accepts the summary array and determines if a collision occurred.
     * If so, displays a red marker in the composite grid view and plays a short beep.
     * All information is written to the tonsole.
     * @param state
     */
    private void ShowState(Object[][] state) {
        _statusPlane.ResetDisplay();
        boolean noCollision = true;
        _display.UpdateStatus("Process C Second " + _second);
        for (int plane = 0; plane < state.length; plane++) {
            String marker = (String)state[plane][0];
            int row = (int)state[plane][1];
            int col = (int)state[plane][2];
            String currentMarker = _statusPlane.GetMarker(row, col) + marker;

            //if marker is longer than 1 character, then two or more trains
            //are in one xy position.
            boolean collision = currentMarker.length() > 1;
            _statusPlane.ShowMarker(row, col, collision, currentMarker);

            //if collision occured, play a sound and note the collision time/position/trains
            //in the log as directed by the assignment.
            if (collision) {
                try {
                    //sound class not written by team
                    //pulled from from https://stackoverflow.com/a/6700039
                    SoundUtils.tone(400,50, 0.2);
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }

                _display.UpdateStatus(", COLLISION " + currentMarker, true);
                ConsoleWriteLine("**** COLLISION between " + currentMarker.substring(0,1) + " and " +
                        currentMarker.substring(1) + " ****" +
                        "\nDetected at second " + _second +
                        "\nOccurred at second " + (_second - 1) + ")" +
                        "\nLocation (" + row + ", " + col + ")\r\n");
                _collisions++;
                noCollision = false;
            }
        }

        if (noCollision) ConsoleWriteLine("No collision at second " + _second + "\r\n");

        try {
            _display.Refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

