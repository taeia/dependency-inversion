package library.domain;

import java.util.List;

/**
 * The queues of the library, one per book, stored whole with their reservations.
 */
public interface QueueRepository {

    /**
     * The queue for the book. Every book has one; a book nobody has reserved has an
     * empty queue.
     */
    Queue of(Book book);

    List<Queue> all();

    void save(Queue queue);
}
