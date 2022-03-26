package pl.zycienakodach.esflights.sdk.application.message.command;

import java.util.Optional;

sealed public class CommandResult permits CommandResult.Accepted, CommandResult.Rejected {
  public static final class Accepted extends CommandResult {

  }

  public static final class Rejected extends CommandResult {
    private final String reason;

    public Rejected(String reason) {
      this.reason = reason;
    }

    public String reason() {
      return reason;
    }
  }

  public Optional<String> failureReason() {
    if(this instanceof Rejected){
      return Optional.of(((Rejected) this).reason);
    }
    return Optional.empty();
  }

}
