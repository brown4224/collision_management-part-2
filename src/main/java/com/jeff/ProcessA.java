package com.jeff;

import java.io.IOException;

/**
 * CLASS ProcessA
 *
 * ProcessA moves each train and submits their positions to bufferAB
 */
public class ProcessA extends ProcessBase {

    private final int _delayMs;
    private final DoubleBuffer<String[][][]> _buffer;
    private final Display _display;

    /**
     * Constructor that accepts the motion delay (0 for single step mode),
     * the double buffer to which it should submit a 3D array of planes containing each train's position,
     * the Display instance (grid view) and the Console view (log view)
     * @param delayMs
     * @param buffer
     * @param display
     * @param console
     */
    public ProcessA(int delayMs, DoubleBuffer<String[][][]> buffer, Display display, Console console) {
        super(console);
        _buffer = buffer;
        _display = display;
        _delayMs = delayMs;
    }

    /**
     * Starts ProcessA thread and continues moving trains until the number of seconds set in Display.Seconds
     * is exhausted. Each time a second passes, the positions of all trains are put into a 3D array
     * and pushed to bufferAB.
     */
    @Override
    public void run() {
        if (_delayMs == 0) ConsoleWriteLine("SINGLE STEP MODE - PRESS ENTER TO PROGRESS\r\n");

        int counter = 0;
        int totalPlanes =  _display.GetPlanes().size();

        //position of all trains is held in the display instance (grid view)
        String[][][] currentState = _display.GetCurrentState();
        _buffer.push(currentState.clone());

        //continue moving trains for _display.seconds
        while (counter < _display.Seconds) {
            System.out.println("A PUSHED: " + counter);
            if (_delayMs > 0) {
                try {
                    Thread.sleep(_delayMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                //if delay is 0, then single step
                _display.AwaitKeypress();
            }
            counter++;

            //move each train in the array, update the display
            //and update current state
            for (int i = 0; i < totalPlanes; i++) {
                currentState[i] = _display.GetPlanes().get(i).MoveOne();
            }
            try {
                _display.Refresh();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Plane.haltX = false;
            Plane.halty = false;
            Plane.haltz = false;

            //push the current state of all planes to bufferAB.
            _buffer.push(currentState.clone());
        }

        //once time has expired, tell the buffer no more data is coming
        //this is necessary so processB will know when to stop trying to retrieve data.
        _buffer.startShutdown();

        System.out.println("A FINISHED - NO MORE INBOUND VALUES");
    }
}

