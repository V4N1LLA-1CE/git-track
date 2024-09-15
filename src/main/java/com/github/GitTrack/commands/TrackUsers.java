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

  public TrackUsers(GitHubService service) {
    this.gitHubService = service;
  }

  @ShellMethod("List public user details of a GitHub user.")
  public String show(
      @ShellOption(defaultValue = "", help = "The username of the GitHub user") String username) {

    // ANSI color codes
    String RED = "\u001B[31m";
    String GREEN = "\u001B[32m";
    String RESET = "\u001B[0m";
    String BOLD = "\u001B[1m";
    String BLUE = "\u001B[34m";

    if (username.trim().isEmpty() || username == null) {
      return BOLD + RED + "Error: Username has not been provided." + RESET + BOLD + GREEN + "\n\nExample Usage: "
          + RESET
          + "gt show " + BLUE + "<username>" + RESET + "\n";
    }

    // fetch user details
    String result = gitHubService.getUser(username);

    return result;
  }
}
