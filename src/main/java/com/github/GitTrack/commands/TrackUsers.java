package com.github.GitTrack.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.github.GitTrack.GitHubService;
import com.github.GitTrack.GitHubUser;

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
      return " Error: Username has not been provided.\n Example Usage: gt show <username>";
    }

    // fetch user details
    Mono<GitHubUser> userMono = gitHubService.getUser(username);

    return userMono
        .map(user -> String.format("Hello %s\nName: %s\nFollowers: %d\nFollowing: %d\nPublic Repos: %d",
            user.login(),
            user.name(),
            user.followers(),
            user.following(),
            user.publicRepos()))
        .onErrorReturn("Error: Unable to fetch user details.")
        .block();
  }
}
