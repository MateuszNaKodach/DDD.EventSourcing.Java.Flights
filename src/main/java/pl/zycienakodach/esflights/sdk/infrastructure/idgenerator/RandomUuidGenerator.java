package pl.zycienakodach.esflights.sdk.infrastructure.idgenerator;

import pl.zycienakodach.esflights.sdk.application.idgenerator.IdGenerator;

import java.util.UUID;

class RandomUuidGenerator implements IdGenerator {

  @Override
  public String get() {
    return UUID.randomUUID().toString();
  }

}
