package pl.zycienakodach.pragmaticflights.sdk.infrastructure.message.command;

import pl.zycienakodach.pragmaticflights.sdk.application.message.command.CommandResult;

public class CommandResultTestFixtures {

  public static boolean wasRejected(CommandResult commandResult) {
    return commandResult instanceof CommandResult.Rejected;
  }

  public static String rejectionReason(CommandResult commandResult) {
    return commandResult.failureReason().orElse(null);
  }

}
