package com.rtosProject2;

import java.io.IOException;

/**
 * CLASS Gui
 * Main program that starts the two display windows (log output and plane views), creates two double buffers
 * and starts three threads called ProcessA, ProcessB, and ProcessC.
 * ProcessA updates the train positions and submits them to bufferAB.
 * ProcessB gets the data from bufferAB and pushes it to bufferCD.
 * ProcessC gets data from bufferCD and determines if a collision occurred.
 * When all three threads have stopped (joined), the program terminates.
 */
public class Gui {

    /**
     * main insertion point to start the application.
     *
     * TO SINGLE STEP, SET delayMs TO 0.
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        Console console = new Console();
        Display display = ConfigureDisplay();

        DoubleBuffer<Message> bufferAB = new DoubleBuffer<>(1);
        DoubleBuffer<Message> bufferCD = new DoubleBuffer<>(1);

        //TO SINGLE STEP, SET delayMs TO 0.
        int delayMs = 1000;
        //delayMs = 0; //set to 0 to single step

        //ProcessC takes the data from bufferCD as it is available
        //and determines if a collision occurred
        ProcessC processC = new ProcessC("ProcessC", delayMs, bufferCD, display, console);
        processC.start();

        //ProcessB takes the data from bufferAB as it is available
        //and pushes it to bufferCD
        ProcessB processB = new ProcessB("ProcessB", bufferAB, bufferCD, console);
        processB.start();

        //ProcessA moves each train and submits their positions to bufferAB
        ProcessA processA = new ProcessA("ProcessA", delayMs, bufferAB, display, console);
        processA.start();

        ProcessMsgShuttle processmsgShuttle = new ProcessMsgShuttle("Shuttle C to A", processC, processA, delayMs, console);
        processmsgShuttle.start();

        //start all process threads, then wait from them all to stop
        processA.join();
        processB.join();
        processC.join();
        processmsgShuttle.join();
        console.WriteLine("DONE");
    }

    /**
     * Creates a new Display instance and adds 4 planes - planes A, B, and C, plus a composite plane
     * used by Process C to show all 3 overlayed in the same position, plus collisions.
     * @return
     * @throws IOException
     */
    private static Display ConfigureDisplay() throws IOException {
        Display display = new Display(2);
        display.AddPlane("Plane for X", Plane.Movement.X, "X", 0, 0, false);
        display.AddPlane("Plane for Y", Plane.Movement.Y, "Y", 0, 2, false);
        display.AddPlane("Plane for Z", Plane.Movement.Z, "Z",3,6,false);
        display.AddPlane("Process C output for X, Y, Z", Plane.Movement.Set, "",-1,-1,true);
        display.Refresh(1000);

        return display;
    }
}

