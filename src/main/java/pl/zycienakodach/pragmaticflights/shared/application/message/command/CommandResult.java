package pl.zycienakodach.pragmaticflights.shared.application.message.command;

import java.util.Optional;

sealed public class CommandResult permits CommandResult.Accepted, CommandResult.Rejected {
  public static final class Accepted extends CommandResult {

  }

  public static final class Rejected extends CommandResult {
    private final String reason;

    public Rejected() {
      this.reason = "Unknown reason.";
    }

    public Rejected(String reason) {
      this.reason = reason;
    }


    public String reason() {
      return reason;
    }
  }

  public Optional<String> failureReason() {
    return switch (this) {
      case Rejected r -> Optional.of(r.reason);
      case Accepted __ -> Optional.empty();
      default -> throw new IllegalStateException("Unexpected value: " + this);
    };
  }

}
