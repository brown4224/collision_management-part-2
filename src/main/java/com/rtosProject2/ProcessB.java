package com.rtosProject2;

/**
 * CLASS ProcessB
 *
 * ProcessB takes data from bufferAB and moves it to bufferCD.
 * The data ProcessB takes from bufferAB is summarized from a 3D array of planes into
 * the format specified by the assignment:
 * TRAIN | ROW | COL
 * X     | 0   | 0
 * The summary data is submitted to bufferCD where it is picked up by ProcessC.
  */
public class ProcessB extends ProcessBase {

    private final DoubleBuffer<Message> _bufferAB;
    private final DoubleBuffer<Message> _bufferCD;

    /**
     * Constructor that accepts the two double buffers
     * B will use for shuttling data
     * @param bufferAB
     * @param bufferCD
     * @param console
     */
    public ProcessB(String name, DoubleBuffer<Message> bufferAB,
                    DoubleBuffer<Message> bufferCD,
                    Console console) {
        super(name, console);
        _bufferAB = bufferAB;
        _bufferCD = bufferCD;
    }

    /**
     * Starts ProcessB's thread.
     * Continues working while _bufferAB is not shutdown (which is indicated by ProcessA)
     * Once shutdown, the thread will tell bufferCD to start its shutdown
     * so ProcessC will ultimately conclude.
     */
    @Override
    public void run() {

        Message message;

        while (!_bufferAB.isShutdown()) {
            message = _bufferAB.pull();
            System.out.println("Process B pulled a " + message.getPayloadType() + " message from AB, then pushed to CD");
            _bufferCD.push(message);
        }
        System.out.println("BufferAB completed");
        _bufferCD.startShutdown();
    }
}

