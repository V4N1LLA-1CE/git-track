package com.github.GitTrack.commands;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

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
      return "Error: Username has not been provided.\nExample Usage: gt show <username>";
    }

    // fetch user details
    Mono<GitHubUser> userMono = gitHubService.getUser(username);

    String result = userMono
        .map(user -> String.format(
            "GitHub User Details%n" +
                "===================%n%n" +
                "Username: %s%n" +
                "Account ID: %d%n" +
                "Avatar Link: %s%n" +
                "Profile Link: %s%n%n" +
                "Type: %s%n" +
                "Site Admin: %s%n%n" +
                "Name: %s%n" +
                "Company: %s%n" +
                "Blog: %s%n" +
                "Location: %s%n" +
                "Email: %s%n" +
                "Hireable: %s%n" +
                "Bio: %s%n" +
                "Twitter Username: %s%n%n" +
                "Public Repos: %d%n" +
                "Public Gists: %d%n" +
                "Followers: %d%n" +
                "Following: %d%n%n" +
                "Created At: %s%n" +
                "Last Updated: %s%n",
            user.login() != null ? user.login() : "-",
            user.id(),
            user.avatarUrl() != null ? user.avatarUrl() : "-",
            user.htmlUrl() != null ? user.htmlUrl() : "-",
            user.type() != null ? user.type() : "-",
            user.siteAdmin() ? "Yes" : "No",
            user.name() != null ? user.name() : "-",
            user.company() != null ? user.company() : "-",
            user.blog() != null ? user.blog() : "-",
            user.location() != null ? user.location() : "-",
            user.email() != null ? user.email() : "-",
            user.hireable() != null ? user.hireable() : "-",
            user.bio() != null ? user.bio() : "-",
            user.twitterUsername() != null ? user.twitterUsername() : "-",
            user.publicRepos(),
            user.publicGists(),
            user.followers(),
            user.following(),
            user.createdAt() != null ? formatDate(user.createdAt()) : "-",
            user.updatedAt() != null ? formatDate(user.updatedAt()) : "-"))
        .onErrorReturn("Error: Unable to fetch user details.")
        .block();

    return result;

  }

  public String formatDate(LocalDateTime date) {
    // get data from datetime object
    int day = date.getDayOfMonth();
    String month = date.getMonth().getDisplayName(TextStyle.FULL,
        Locale.ENGLISH);
    int year = date.getYear();

    // get suffix for day
    String suffix;
    if (day >= 11 && day <= 13) {
      suffix = "th";
    } else {
      switch (day % 10) {

        case 1:
          suffix = "st";
          break;

        case 2:
          suffix = "nd";
          break;

        case 3:
          suffix = "rd";
          break;

        default:
          suffix = "th";
          break;
      }
    }

    // format date
    return (String.format("%d%s %s %d", day, suffix, month, year));

  }
}
