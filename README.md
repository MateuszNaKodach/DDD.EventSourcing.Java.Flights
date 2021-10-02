# Pragmatic Flights

## Domain Exploration

## Application Architecture

## Patterns

### Modules

#### Flight Schedule
I introduced date lik fromDate / toDate - range in which I want to schedule flights.

#### Discounts
Encapsulated discount calculation. 
Currently it's just single module and calculating each discount it's in single operation - all discounts or none.
For better resilence and easier Open-Closed on architecture level I would split it. 
Every discount (or with more traffic) may be independent. 
I imagine something like event -> DiscountCalculationStarted from Discount module.
Then each discount specific service will calculate discount and publish event like 
SpecificDiscountCalculated(discountName, discountValue), which will be handled by discount module.
And then if all discounts were caculated or on timeout, discount module will sum up all discount and publish
DiscountCalculationCompleted.

### Processes

### ReadModels

### High-level view

### Trade-offs (simplifications)


### Dates
I assumed flight dates, so I use LocalTime and LocalDate. I treat is as UTC dates.

## Testing strategy

## Out-of-scope features

TenantId - imporant concept, explicit


More sophisticated solution like PublicEvent / ApplicationEvent instead of DomainEvent.
Causation and correlationId propagation from Event to Command.


Should API of module containt domain value objects? 
No - it's simplification. Require on client to create them.

Ordering and Pricing are such generic domains.
It may be possible to use langugage and make them usable in another contexts.


### Pricing
Może być użyty do cen różnych produktów.
Możnaby z niego wyjąć generyczną część.
Aktualnie bardziej dostosowany jednak do lotów.
Lepiej go dostosować / rozszerzyć. 
Discounts - mogą dotyczyć np. jakie inne loty jeszcze zamawia.
Wtedy trzeba wprowadzić abstrakcję koszyka.


### Out of scope / disclaimers
I've developed simple framework, which was needed to synchronize different bounded context implementation.

For production ready application some improvements:
- inbox / outbox for modules
I assumed that it's part or move complex infrastrucutre / IO which was mocked by InMemory implementations.

Płatność za zamówienie? 
Jedynie składanie zamówienia.

On top of ordering we can build busket feature.

Modules - business logic modules, totally independent from others.
Processes - orchestrate business logic and delegate job to different business modules.
Read Models - views

Domain model is pure functional

Many simplifications, no retries etc. Processing events out of order and duplication, payment integration.
Communications are by events, so may be extract as microservice with different transport layer. 
Tracking processes progress.

Everything was based on Events, so fastests for me was to incorporate EventSourcing.
But of course I can use State-Based persistence.
Best option would be to enable/disable event handlers based on Tenant group.
With TenantId in EventMetadata it should be straightforward.

Domain logic is almost pure functional. I've used Exceptions instead of monads like Eithers.
It's easier to deal with it in ValueObjects instead of processing functional pipeline.


## Discounting
