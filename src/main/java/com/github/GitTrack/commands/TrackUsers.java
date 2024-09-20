package com.github.GitTrack.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.github.GitTrack.GitHubService;

/**
 * TrackUsers
 */
@ShellComponent
public class TrackUsers {

  private final GitHubService gitHubService;

  // ANSI color codes
  private final String RED = "\u001B[31m";
  private final String GREEN = "\u001B[32m";
  private final String RESET = "\u001B[0m";
  private final String BOLD = "\u001B[1m";
  private final String BLUE = "\u001B[34m";

  // auto inject service through constructor
  public TrackUsers(GitHubService service) {
    this.gitHubService = service;
  }

  @ShellMethod("List public user details of a GitHub user.")
  public String show(
      @ShellOption(defaultValue = "", help = "The username of the GitHub user") String username) {

    if (username.trim().isEmpty() || username == null) {
      return BOLD + RED + "Error: Username has not been provided." + RESET + BOLD + GREEN + "\n\nExample Usage: "
          + RESET
          + "gt show " + BLUE + "<username>" + RESET + "\n";
    }

    // fetch user details
    String result = gitHubService.getUser(username);

    return result;
  }

  @ShellMethod("List recent github activities of a Github user.")
  public String activity(@ShellOption(defaultValue = "", help = "The username of the GitHub user") String username) {
    if (username.trim().isEmpty() || username == null) {
      return BOLD + RED + "Error: Username has not been provided." + RESET + BOLD + GREEN + "\n\nExample Usage: "
          + RESET + "gt activity" + BLUE + " <username>" + RESET + "\n";
    }

    // fetch activity details
    return gitHubService.getActivity(username);
  }

}
