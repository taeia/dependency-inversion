package library.domain;

import java.util.List;

/**
 * The books the library holds.
 */
public interface BookRepository {

    List<Book> all();

    void save(Book book);
}
