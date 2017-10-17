package com.jeff;

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

    private final DoubleBuffer<String[][][]> _bufferAB;
    private final DoubleBuffer<Object[][]> _bufferCD;

    /**
     * Constructor that accepts the two double buffers
     * B will use for shuttling data
     * @param bufferAB
     * @param bufferCD
     * @param console
     */
    public ProcessB(DoubleBuffer<String[][][]> bufferAB,
                    DoubleBuffer<Object[][]> bufferCD,
                    Console console) {
        super(console);
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

        String[][][] state;

        while (!_bufferAB.isShutdown()) {
            state = _bufferAB.pull();
            System.out.println("B pulled: " + state.length);
            Object[][] summary = SummarizeState(state);
            _bufferCD.push(summary);
        }
        System.out.println("BufferAB completed");
        _bufferCD.startShutdown();
    }

    /**
     * The data ProcessB takes from bufferAB is summarized from a 3D array of planes into
     * the format specified by the assignment:
     * TRAIN | ROW | COL
     * X     | 0   | 0
     * @param state
     * @return
     */
    private Object[][] SummarizeState(String[][][] state) {
        Object[][] summary = new Object[state.length][3];
        boolean brk = false;
        for (int plane = 0; plane < state.length; plane++) {
            for (int row = 0; row < Plane.rows; row++) {
                for (int col = 0; col < Plane.cols; col++) {
                    if(state[plane][row][col] != "") {
                        summary[plane][0] = state[plane][row][col];
                        summary[plane][1] = row;
                        summary[plane][2] = col;
                        System.out.println(state[plane][row][col] + " : " + row + " : " + col);
                        brk = true;
                        break;
                    }
                }
                if (brk) break;
            }
            brk = false;
        }
        return summary;
    }
}

