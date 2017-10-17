package com.jeff;

/**
 * CLASS ProcessBase - inherits from Thread
 *
 * Abstract class used by all Process threads that provide reusable methods
 * to write to the console and wait.
 */
abstract class ProcessBase extends Thread {

    private final Console _console;

    protected ProcessBase(Console console) {
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
}
