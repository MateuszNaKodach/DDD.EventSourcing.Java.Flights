package pl.zycienakodach.pragmaticflights.sdk.infrastructure.idgenerator;

import pl.zycienakodach.pragmaticflights.sdk.application.idgenerator.IdGenerator;

import java.util.UUID;

class RandomUuidGenerator implements IdGenerator {

  @Override
  public String get() {
    return UUID.randomUUID().toString();
  }

}
