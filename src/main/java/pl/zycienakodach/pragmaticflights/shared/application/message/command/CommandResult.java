package pl.zycienakodach.pragmaticflights.shared.application.message.command;

sealed public class CommandResult permits CommandResult.Accepted, CommandResult.Rejected {
  public static final class Accepted extends CommandResult {

  }

  public static final class Rejected extends CommandResult {
  }
}
