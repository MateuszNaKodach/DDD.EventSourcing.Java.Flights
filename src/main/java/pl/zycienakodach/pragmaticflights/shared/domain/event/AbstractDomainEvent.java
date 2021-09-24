package pl.zycienakodach.pragmaticflights.shared.domain.event;

abstract class AbstractDomainEvent<DataType> {

  abstract DataType data();

}
