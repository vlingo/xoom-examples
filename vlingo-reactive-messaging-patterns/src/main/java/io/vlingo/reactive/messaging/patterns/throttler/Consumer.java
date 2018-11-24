package io.vlingo.reactive.messaging.patterns.throttler;

public interface Consumer {
    void onReceiveMessage(final String message);
}
