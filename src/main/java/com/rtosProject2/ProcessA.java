package com.rtosProject2;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * CLASS ProcessA
 *
 * ProcessA moves each train and submits their positions to bufferAB
 */
public class ProcessA extends ProcessBase {

    private final int _delayMs;
    private final DoubleBuffer<Message> _buffer;
    private final Display _display;
    private final ExecutorService pool = Executors.newFixedThreadPool(4);

    /**
     * Constructor that accepts the motion delay (0 for single step mode),
     * the double buffer to which it should submit a 3D array of planes containing each train's position,
     * the Display instance (grid view) and the Console view (log view)
     * @param delayMs
     * @param buffer
     * @param display
     * @param console
     */
    public ProcessA(String name, int delayMs, DoubleBuffer<Message> buffer, Display display, Console console) {
        super(name, console);
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
        Positions positions = new Positions(currentState);
        preventCollision(positions);
        _buffer.push(new Message<>(Message.PayloadType.Position, positions));

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
            Plane.haltY = false;
            Plane.haltZ = false;

            //push the current state of all planes to bufferAB.
            positions = new Positions(currentState);
            preventCollision(positions);
            _buffer.push(new Message<>(Message.PayloadType.Position, positions));

            //check for messages shuttled from C
            CheckForMessagesFromC();
        }

        //once time has expired, tell the buffer no more data is coming
        //this is necessary so processB will know when to stop trying to retrieve data.
        _buffer.startShutdown();

        while(CheckForMessagesFromC()) {
            //exhaust the test mail messages received from C
        }

        System.out.println("A FINISHED - NO MORE INBOUND VALUES");
    }

    /**
     * This should be rewritten in Project 3 to receive control messages from process C...
     * stub is here for proof of concept. Uncomment call to CheckForMessagesFromC to see how this works.
     */
    private boolean CheckForMessagesFromC() {
        Message message = tryGetMessage();
        if (message != null) {
            if (message.getPayloadType() == Message.PayloadType.Test) {
                String testMsgFromC = (String) message.getPayload();
                System.out.println(
                        "*** [" +
                        message.getPayloadType() + "] MAIL RECEIVED ON [" + name() + "] --- [" + testMsgFromC + "]");
            }
            return true;
        }
        return false;
    }

    private void preventCollision(Positions positions) {
        try {
            int offset = 2;  // difference from communication
            int look_ahead = Plane.rows + offset;

            Callable<Integer> base_line = new CollisionManagement(positions, -1, look_ahead);
            Callable<Integer> callable_x = new CollisionManagement(positions, 0, look_ahead);
            Callable<Integer> callable_y = new CollisionManagement(positions, 1, look_ahead);
            Callable<Integer> callable_z = new CollisionManagement(positions, 2, look_ahead);

            Future<Integer> future_baseline = pool.submit(base_line);
            Future<Integer> future_halt_x = pool.submit(callable_x);
            Future<Integer> future_halt_y = pool.submit(callable_y);
            Future<Integer> future_halt_z = pool.submit(callable_z);

            final int collision = future_baseline.get();
            if (collision >= look_ahead) {
                future_halt_x.cancel(true);
                future_halt_y.cancel(true);
                future_halt_z.cancel(true);
            } else {
                final int halt_x = future_halt_x.get();
                final int halt_y = future_halt_y.get();
                final int halt_z = future_halt_z.get();

                if (halt_x > collision && halt_x >= halt_y && halt_x >= halt_z)
                    Plane.haltX = true;
                if (halt_y > collision && halt_y > halt_x && halt_y >= halt_z) {
                    Plane.haltY = true;
                    System.out.println("C halts Y");
                }
                if (halt_z > collision && halt_z > halt_x && halt_z > halt_y)
                    Plane.haltZ = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

