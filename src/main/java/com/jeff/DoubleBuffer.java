package com.jeff;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * Thread-safe generic double buffer implementation
 *
 * To use:
 * new DoubleBuffer<T>(inboundSize);
 *
 * Automatically handles switching between the input and output buffer. When input is full, the input
 * becomes the output buffer. When the new input buffer is full, it switches to become the output buffer IF
 * the current output buffer is empty. This takes synchronization via semaphores.
 */
public class DoubleBuffer<T> {
    private final int _inboundSize;
    private final LinkedList<T> _queueA;
    private final LinkedList<T> _queueB;
    private final Semaphore _overallSem = new Semaphore(1);
    private final Semaphore _inboundSem = new Semaphore(1);
    private final Semaphore _outboundSem = new Semaphore(1);
    private boolean _startShutdown = false;
    private boolean _isShutdown = false;
    private LinkedList<T> _inboundQueue;
    private LinkedList<T> _outboundQueue;
    private boolean _aIsInbound = false;

    /**
     * Constructor that initializes queues and sets the size of the inbound queue
     * @param inboundSize
     */
    public DoubleBuffer(int inboundSize) {
        _inboundSize = inboundSize;
        _queueA = new LinkedList<>();
        _queueB = new LinkedList<>();
        toggleInboundQueue();
    }

    /**
     * Informs the buffer it should not accept any more data
     */
    public final void startShutdown() {
        _startShutdown = true;
    }

    /**
     * Private getter to tell the class if it is still accepting input
     * @return
     */
    private boolean isAcceptingInput() {
        return !_startShutdown;
    }

    /**
     * Getter to inform caller if queue is shutdown (no longer available for processing)
     * @return
     */
    public final boolean isShutdown() {
        if (_isShutdown) {
            return true;
        }
        int remaining = _outboundQueue.size() + _inboundQueue.size();
        return _isShutdown = _startShutdown && remaining == 0;
    }

    /**
     * Private method that handles switching queue A and queue B from input to output, or vice versa.
     */
    private void toggleInboundQueue() {
        try {
            _overallSem.acquire();
            _aIsInbound = !_aIsInbound;
            _inboundQueue = _aIsInbound ? _queueA : _queueB;
            _outboundQueue = !_aIsInbound ? _queueA : _queueB;
            _overallSem.release();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    /**
     * Pushes a value into the current input buffer - no need for caller to tracker whether that is A or B.
     * @param value
     */
    public final void push(T value) {
        if (value == null) {
            throw new IllegalArgumentException("You cannot submit null");
        }

        try {
            _inboundSem.acquire();
            if (_inboundQueue.size() >= _inboundSize) {
                while (isAcceptingInput() && !_outboundQueue.isEmpty()) {
                    Thread.sleep(0);
                }
                toggleInboundQueue();
            }

            if (isAcceptingInput()) {
                _inboundQueue.offer(value);
            } else {
                throw new IllegalStateException("Buffer is not accepting input");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } finally {
            _inboundSem.release();
        }
    }

    /**
     * Extracts a value from the buffer.
     * @return Returns null if output buffer is empty.
     */
    public final T pull() {
        T result = null;
        try {
            _outboundSem.acquire();
            if (_outboundQueue.isEmpty()) {
                //if outbound queue has nothing in it and active, we have to wait
                while (isAcceptingInput() && _outboundQueue.isEmpty()) {
                    Thread.sleep(0);
                }

                if (!isAcceptingInput() && !_inboundQueue.isEmpty()) {
                    //not accepting input, but we have stuff in the inbound queue that still needs processing
                    //switch to the inbound queue and get that stuff out to the callers
                    toggleInboundQueue();
                }
            }

            if (!_outboundQueue.isEmpty()) {
                result = _outboundQueue.poll();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        _outboundSem.release();
        return result;
    }
}

