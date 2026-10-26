# Dependency Inversion

Companion repository for Chapter 6 of [*Domain-Centric Architecture*](https://github.com/taeia/domain-centric-architecture): the library lending application with every dependency on the outside world pointing the same way. The domain declares what it needs, and the other modules provide it.

The four modules, the five screens, and the seventeen scenario tests are those of Chapter 5, and so are the aggregates, the repositories, and the domain services. What changed is the direction of the two dependencies that still pointed outward. `Clock` is an interface in the domain module with one method, `today()`, and `Loan` and `Member` are told the date instead of reading a static field. `Notifier` is an interface in the domain module with one method, `send`, and the application layer calls it without knowing what sending means. The infrastructure module implements both, `SettableClock` for the date picker and the tests and `InMemoryNotifier` for the list the window shows, next to the four in-memory repositories it already implemented. The domain module depends on nothing it did not define, the application module imports nothing from the infrastructure module, and the static clock with its setter is gone.

Each layer is a Maven module, and each module's `pom.xml` names the layers it depends on. `domain` depends on nothing. `infrastructure` depends on `domain`, because it implements the domain's interfaces: the repositories, the clock, and the notifier. `application` depends on `domain`, and on `infrastructure` in test scope only, because the scenario test wires the real pieces the way the main window does. `presentation` calls only `application`, and lists `domain` and `infrastructure` as well, because the entities travel to the screen and the entry point chooses the implementations. The chapter explains why this is no longer a layered architecture, whatever the module names say, and what picture replaces it.

The library it implements, with its rules and scenarios, is described in [Chapter 0](https://github.com/taeia/domain-centric-architecture/blob/main/chapters/00-the-library.md). The chapter about this code is [Chapter 6](https://github.com/taeia/domain-centric-architecture/blob/main/chapters/06-dependency-inversion.md).

## Running

You need Java 25 and Maven. JavaFX is a Maven dependency, so nothing else has to be installed.

```
mvn install
mvn -pl presentation javafx:run
```

The first command builds all four modules and runs the tests. The second starts the application from the presentation module, which is the only one with a main class. Repeat the first command after changing anything outside `presentation`.

A window opens with the library in its starting state. The date picker in the toolbar sets the library's idea of today, so you can walk through the scenarios of Chapter 0 and check the due dates and fines by hand. The *Reset library* button returns to the starting state.

To start it from an IDE, run `library.presentation.Launcher`. Running `MainWindow` directly fails with *JavaFX runtime components are missing*, because JavaFX is on the classpath and the Java launcher checks for the modules whenever the main class extends `Application`. `Launcher` does not extend it, so the check is skipped.

## Tests

```
mvn test
```

`ScenarioTest` lives in the `application` module and has one test per scenario of Chapter 0, with the same name. The tests call the three application services directly, with the in-memory repositories loaded with the starting state, and move a `SettableClock` from day to day. They do not start JavaFX.

The `domain` module has unit tests of single rules. `LoanTest` and `MemberTest` need no clock at all: each test says which day it is asking about, so whether a loan is overdue on 16 March is a question with an answer and no setup. `LendingTest` and `ReservingTest` test the operations that cross aggregates, and they use the fakes in the test directory, three repositories and a `FakeClock`, written against the interfaces the domain declares. The domain module is tested without the infrastructure module and without any static state.

## Layout

```
pom.xml                        the parent: lists the four modules, holds the versions
presentation/                  the screens: the main window and five dialogs
  pom.xml                      depends on application (and on domain and infrastructure, see above)
  src/main/java/library/presentation/
    MainWindow.java            wires the layers together in start(): the one place that chooses implementations
    Launcher.java              starts MainWindow from an IDE
    BorrowDialog.java
    ReturnDialog.java
    RenewDialog.java
    ReserveDialog.java
    PayDialog.java
application/                   the application layer: thin coordinators
  pom.xml                      depends on domain (and on infrastructure in test scope only)
  src/main/java/library/application/
    LoanService.java           borrow, return, renew: call Lending, notify through the domain's Notifier
    ReservationService.java    reserve: call Reserving, read the position back
    MemberService.java         pay: call the member, save
  src/test/java/library/application/
    ScenarioTest.java          one test per scenario
domain/                        the domain layer: aggregates, repositories, domain services, and the contracts
  pom.xml                      depends on nothing (JUnit in test scope)
  src/main/java/library/domain/
    Member.java                aggregate root: name, debt, loans; borrow, returnCopy, renew, pay
    Loan.java                  entity inside Member: due date, isOverdue, fine, each for a given day
    Queue.java                 aggregate root: the reservations of one book; join, leave, first
    Reservation.java           entity inside Queue
    Copy.java                  aggregate root: available, on loan, or on hold; lendTo, holdFor, makeAvailable
    Book.java                  aggregate root: ISBN and title
    Money.java                 value object: an amount in euros, never negative
    Fine.java                  value object: days of delay and what they cost
    MemberRepository.java      one repository interface per aggregate
    QueueRepository.java
    CopyRepository.java
    BookRepository.java
    Clock.java                 interface: the library's idea of today
    Notifier.java              interface: sends a message to a member
    Lending.java               domain service: borrow, return, renew, across aggregates; asks the clock
    Reserving.java             domain service: reserve
    LibraryException.java      thrown when the library refuses an operation
  src/test/java/library/domain/
    MemberTest.java            unit tests of single rules, one class per domain class
    LoanTest.java
    QueueTest.java
    CopyTest.java
    MoneyTest.java
    FineTest.java
    LendingTest.java
    ReservingTest.java
    FakeMemberRepository.java  the repositories and the clock the domain tests use
    FakeCopyRepository.java
    FakeQueueRepository.java
    FakeClock.java
infrastructure/                the implementations of the domain's contracts
  pom.xml                      depends on domain
  src/main/java/library/infrastructure/
    InMemoryMemberRepository.java   the repository interfaces, implemented with lists
    InMemoryQueueRepository.java
    InMemoryCopyRepository.java
    InMemoryBookRepository.java
    SettableClock.java         the clock: set by the date picker and by the scenario tests
    InMemoryNotifier.java      the notifier: adds each message to a list the window shows
    StartingState.java         the starting state of Chapter 0
```
