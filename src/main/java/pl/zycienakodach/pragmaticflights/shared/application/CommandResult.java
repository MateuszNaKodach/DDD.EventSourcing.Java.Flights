package pl.zycienakodach.pragmaticflights.shared.application;

sealed public class CommandResult permits CommandResult.Accepted, CommandResult.Rejected {
  static final class Accepted extends CommandResult {

  }

  static final class Rejected extends CommandResult {
  }
}
