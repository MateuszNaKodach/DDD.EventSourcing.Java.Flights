package pl.zycienakodach.pragmaticflights.shared.application;

sealed class CommandResult permits CommandResult.Accepted, CommandResult.Rejected {
  static final class Accepted extends CommandResult {

  }

  static final class Rejected extends CommandResult {
  }
}
