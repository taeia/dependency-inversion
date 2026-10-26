package library.domain;

/**
 * Sends a notification to a member: the message of Chapter 0 that tells someone a copy
 * is waiting for them. The domain says what sending means, in its own words, and the
 * infrastructure decides how a message travels.
 */
public interface Notifier {

    void send(Member member, String message);
}
