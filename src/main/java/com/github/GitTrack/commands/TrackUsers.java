package com.github.GitTrack.commands;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.github.GitTrack.GitHubService;

import reactor.core.publisher.Mono;

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

    if (username.trim().isEmpty() || username == null) {
      return "Error: Username has not been provided.\nExample Usage: gt show <username>";
    }

    // fetch user details
    String result = gitHubService.getUser(username);

    return result;
  }
}
