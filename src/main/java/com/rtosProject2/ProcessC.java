package com.rtosProject2;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;

/**
 * CLASS ProcessC
 * <p>
 * ProcessC takes summary data from bufferCD and determines if a collision
 * occurred. This implementation also creates a composite view of all
 * trains in the display grid to visually show all the trains in one plane and
 * show collisions in red (plus a beep).
 * All data from ProcessC is shown in the grid and logged to the console.
 */
public class ProcessC extends ProcessBase {

    private final DoubleBuffer<Message> _bufferCD;
    private final int _delayMs;
    private Plane _statusPlane;
    private Display _display;
    private int _second = 2;
    private int _collisions = 0;

//    private ExecutorService executor = Executors.newFixedThreadPool(4);

    /**
     * Constructor that accepts the the bufferCD summary data
     *
     * @param name
     * @param delayMs
     * @param bufferCD
     * @param display
     * @param console
     */
    public ProcessC(String name,
                    int delayMs,
                    DoubleBuffer<Message> bufferCD,
                    Display display,
                    Console console) {
        super(name, console);
        _delayMs = delayMs;
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

        Message message;

        while (!_bufferCD.isShutdown()) {
            message = _bufferCD.pull();
            Positions positions = (Positions) message.getPayload();
            System.out.println("C pulled " + positions.size() + " positions from buffer CD");
            ConsoleWriteLine("SECOND " + _second);
            OutputState(positions);
            ShowState(positions);

            SendTestMessageToA(_second);

            _second++;
        }
        String finMsg = "DONE. " + _collisions + " collisions occurred over " + _display.Seconds + " seconds.";
        _display.UpdateStatus(finMsg);
        ConsoleWriteLine(finMsg);
        System.out.println("BufferCD completed");
        ConsoleWait();
    }

    private void SendTestMessageToA(int second) {
        String testMsg = "Process " + name() + " is at second " + second;
        Message message = new Message<>(Message.PayloadType.Test, testMsg);

        System.out.println(
                "*** [" + message.getPayloadType() + "] MAIL SENT ON [" + name() + "] --- [" + testMsg + "]");

        sendMessage(message);
    }

    /**
     * Accepts the summary array and writes the data to the console.
     *
     * @param positions
     */
    private void OutputState(Positions positions) {
        for (int idx = 0; idx < positions.size(); idx++) {
            Position position = positions.get(idx);
            ConsoleWriteLine(position.train() + " | " + position.row() + " | " + position.col());
        }
        ConsoleWriteLine();
        try {
            _display.Refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Accepts the summary array and determines if a collision occurred.
     * If so, displays a red marker in the composite grid view and plays a short beep.
     * All information is written to the tonsole.
     *
     * @param positions
     */
    private void ShowState(Positions positions) {
        _statusPlane.ResetDisplay();
        boolean noCollision = true;
        _display.UpdateStatus("Process C Second " + _second);
        for (int pos = 0; pos < positions.size(); pos++) {
            Position position = positions.get(pos);
            String marker = position.train();
            int row = position.row();
            int col = position.col();
            String currentMarker = _statusPlane.GetMarker(row, col) + marker;

            //if marker is longer than 1 character, then two or more trains
            //are in one xy position.
            boolean collision = currentMarker.length() > 1;
            _statusPlane.ShowMarker(row, col, collision, currentMarker);

            //if collision occurred, play a sound and note the collision time/position/trains
            //in the log as directed by the assignment.
            if (collision) {
                try {
                    //sound class not written by team
                    //pulled from from https://stackoverflow.com/a/6700039
                    SoundUtils.tone(400, 50, 0.2);
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                }

                _display.UpdateStatus(", COLLISION " + currentMarker, true);
                ConsoleWriteLine("**** COLLISION between " + currentMarker.substring(0, 1) + " and " +
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

