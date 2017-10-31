package com.rtosProject2;

public class Message <T> {

    public enum PayloadType {
        Test,
        Position
    }

    private final PayloadType _payloadType;
    private final T _payload;

    public Message (PayloadType type, T payload) {
        _payloadType = type;
        _payload = payload;
    }

    public PayloadType getPayloadType() {
        return _payloadType;
    }

    public T getPayload() {
        return _payload;
    }
}
