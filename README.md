# Pragmatic Flights

## Summary Notes

### Assumptions and trade-offs
 - I've introduced FlightCourse - as flight which have departure date and time. It contains flightId + departureAt.
 - ScheduleFlightCourses command accepts fromDate and toDate parameters. It's a range in which I want to schedule flights. It's similar to like it really works (you cannot buy ticket in advance for flight in 10 years). This assumption is enabler for FlightCourse concept.
 - I was focused on business logic and application do not have things like failre recovery / timeouts / retries / inbox and outbox / logging / observability etc. It's not production ready.
 - Domain logic is almost pure functional. Business Logic is implemented in functions like `(pastEvents, command) -> newEvents` I've used Exceptions instead of monads like Eithers. It's easier to deal
   with it in ValueObjects instead of processing functional pipeline. I also used Event Sourcing as persistence mechanism, for faster development witch such functional approach. I don't care about
   storage entities.
 - Requirment with no saving applied discount criteria for selected tenant group was very specific. I think in most cases such differences may take palce in business logic. If then best option would
   be to enable/disable event handlers based on Tenant group.

### Possible improvements

- Use Java Modules. Currently, there are some public classes / methods which should be accessible just in certain modules / packages. Require review, but lack of time.
- Split Application class to increase cohesion.
- Discounts module have many dependencies. We can split every criteria to own module and "register" possible criteria. When CalculateDiscountValue is requested, then every discount criteria module may
  do the calculation on their own. Currently, it's just single module and calculating each discount it's in single operation - all discounts or none. For better resilience and easier Open-Closed on
  architecture level I would split it.

## Application Architecture

Application architecture is Modular Monolith.
I've developed simple infrastructure / application logic to support commands and events. 
It's placed in sdk package.

### Modules
Every module is independent of each other. Module API is defined by commands and events.
Most of the modules' logic is tested on high-level API and do not focus on implementation details.
It gives possibility to easily refactor in the future.

Every module may be extracted as separated Microservice if we replace EventBus / CommandBus with solutions like Kafka / RabbitMQ.

### Processes
Processes are managers which coordinate in an orchestration manner how modules interacts with each other.
It's implementation of more complex business processes which span many modules.


## Out-of-scope features

- More sophisticated solution like PublicEvent / ApplicationEvent instead of DomainEvent (without ValueObjects right now). Now same events are used for domain logic / cross-modules communication and for storage.
