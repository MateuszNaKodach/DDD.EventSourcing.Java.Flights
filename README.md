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
