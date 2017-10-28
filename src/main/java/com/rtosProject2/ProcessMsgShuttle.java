package com.rtosProject2;

/**
 * CLASS ProcessMsgShuttle
 *
 * Beginnings of process to shuttle messages (type Message) between processes.
 * Can be used for control messages in Project 3. Working Stub is here to provide
 * foundation. Should be extended in Project 3 by team.
 * Takes a reference "from" process and a reference "to" process and moves messages
 * between them via a thread-safe queue on whatever frequency is defined in the contructor.
 * It is up to the sending and receiving processes to define how to communicate. The queue
 * structure is in ProcessBase and may need to be extended to include the address of the message.
 * Presently it's a single queue.
  */
public class ProcessMsgShuttle extends ProcessBase {

    private final ProcessBase _fromProcess;
    private final ProcessBase _toProcess;
    private final int _periodMs;
    private boolean _isShutdown = false;
    /**
     * Constructor that accepts the two double buffers
     * B will use for shuttling data
     * @param console
     * @param fromProcess is the process that will send messages via this msg shuttle
     * @param toProcess is the process that will receive messages from this msg shuttle
     * @param periodMs is how often messages will be moved from this process's buffer to the target process
     */
    public ProcessMsgShuttle(String name, ProcessBase fromProcess, ProcessBase toProcess, int periodMs, Console console) {
        super(name, console);
        _fromProcess = fromProcess;
        _toProcess = toProcess;
        _periodMs = periodMs;
    }

    /**
     * Starts ProcessMsgShuttle's thread.
     * Will automatically terminate when either the from or to process are no longer alive
     */
    @Override
    public void run() {

        Message message;
        System.out.println("ProcessMsgShuttle [" + name()  + "] started");

        while (_fromProcess != null && _fromProcess.isAlive() && _toProcess != null && _toProcess.isAlive()) {
            try {
                Thread.sleep(_periodMs);
                message = _fromProcess.tryGetMessage();
                if (message != null) {
                    _toProcess.sendMessage(message);
                    System.out.println("*** MAIL TRANSFER AGENT [" + name() + "] pulled a " + message.getPayloadType() +
                            " message from [" + _fromProcess.name() + "] and pushed it to [" + _toProcess.name() + "]");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //show warning on console if shutdown, but messages in [from] still waiting for movement
        if (_fromProcess != null && _fromProcess.hasMessages()) {
            System.out.println("*** WARNING FROM MAIL TRANSFER AGENT [" + name() + "] --- processing stopped, but ["
                    + _fromProcess.name() + "] still had [" + _fromProcess.messageCount() +
                    "] messages waiting to be delivered to [" + _toProcess.name() + "]");
        }

        //show warning on console if shutdown, but messages in [to] still waiting for processing
        if (_toProcess != null && _toProcess.hasMessages()) {
            System.out.println("*** WARNING FROM MAIL TRANSFER AGENT [" + name() + "] --- processing stopped, but ["
                    + _toProcess.name() + "] still had [" + _toProcess.messageCount() +
                    "] messages waiting to be processed");
        }

        System.out.println("ProcessMsgShuttle [" + name()  + "] completed");
    }
}

