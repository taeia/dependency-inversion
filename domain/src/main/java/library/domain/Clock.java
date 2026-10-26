package library.domain;

import java.time.LocalDate;

/**
 * The library's idea of today. This is the one question the domain asks about the
 * outside world's time, and the domain only asks it; who answers is decided where the
 * library is wired. There is no static state and no setter here any more.
 */
public interface Clock {

    LocalDate today();
}
