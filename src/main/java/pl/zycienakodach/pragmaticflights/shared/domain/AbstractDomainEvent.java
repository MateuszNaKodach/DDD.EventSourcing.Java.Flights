package pl.zycienakodach.pragmaticflights.shared.domain;

abstract class AbstractDomainEvent<DataType> {

  abstract DataType data();

}
