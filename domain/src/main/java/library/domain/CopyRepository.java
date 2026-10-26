package library.domain;

import java.util.List;

/**
 * The copies the library owns.
 */
public interface CopyRepository {

    List<Copy> all();

    List<Copy> ofBook(Book book);

    void save(Copy copy);
}
