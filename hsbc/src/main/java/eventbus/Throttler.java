package eventbus;

public interface Throttler {

    // check if we can proceed (poll)
    ThrottleResult shouldProceed();

    // subscribe to be told when we can proceed (Push)
    void notifyWhenCanProceed();

    enum ThrottleResult {
        PROCEED,
        DO_NOT_PROCEED
    }
}
