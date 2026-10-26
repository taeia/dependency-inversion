package library.infrastructure;

import java.util.ArrayList;
import java.util.List;

import library.domain.Member;
import library.domain.Notifier;

/**
 * Sends messages to members by adding them to a list that the main window shows. Evans
 * puts sending messages in the infrastructure layer, next to persistence, and that is
 * where this class lives, as before. What is new is the {@code implements}: the domain
 * says what a notifier is, and this is one way to be it. A real channel would be another.
 */
public class InMemoryNotifier implements Notifier {

    private final List<String> sent = new ArrayList<>();

    @Override
    public void send(Member member, String message) {
        sent.add(member.name() + ": " + message);
    }

    public List<String> sent() {
        return new ArrayList<>(sent);
    }

    public void clear() {
        sent.clear();
    }
}
