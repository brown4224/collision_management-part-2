package com.rtosProject2;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * CLASS ProcessBase - inherits from Thread
 *
 * Abstract class used by all Process threads that provide reusable methods
 * to write to the console and wait.
 */
abstract class ProcessBase extends Thread {

    private final String _name;
    private final Console _console;

    private final LinkedBlockingDeque<Message> _msgQueue = new LinkedBlockingDeque<>();
    protected ProcessBase(String name, Console console) {
        _name = name;
        _console = console;
    }

    /**
     * Writes an empty line to the console.
     */
    public void ConsoleWriteLine() {
        ConsoleWriteLine("");
    }

    /**
     * Write a line of output to the console.
     * @param output
     */
    public void ConsoleWriteLine(String output) {
        _console.WriteLine(output);
    }

    /**
     * Causes the console to wait for a key to be pressed. Used by single-step mode.
     */
    public void ConsoleWait() {
        _console.Wait();
    }

    /**
     * If there are messages for this process waiting to be processed, they will be returned here.
     * Returns null if no messages in queue. Queue processing is FIFO. Does NOT block.
     * @return
     */
    protected Message tryGetMessage() {
        return _msgQueue.pollFirst();
    }

    protected boolean hasMessages() {
        return _msgQueue.size() > 0;
    }

    protected int messageCount() {
        return _msgQueue.size();
    }

    /**
     * Puts a message for this process into a thread-safe queue for FIFO processing. Does NOT block.
     * @param msg
     */
    protected void sendMessage(Message msg) {
        _msgQueue.push(msg);
    }

    public String name() {
        return _name;
    }
}
