package library.infrastructure;

import java.time.LocalDate;

import library.domain.Clock;

/**
 * A clock that is set by hand. The date picker in the window sets it, and so do the
 * scenario tests, so that the scenarios of Chapter 0 can be walked through day by day.
 * It starts on 1 March 2026, the day before the first scenario. A library that ran for
 * real would wire a clock that reads the system date instead, and nothing in the
 * domain would notice.
 */
public class SettableClock implements Clock {

    private LocalDate today = LocalDate.of(2026, 3, 1);

    @Override
    public LocalDate today() {
        return today;
    }

    public void set(LocalDate date) {
        today = date;
    }
}
