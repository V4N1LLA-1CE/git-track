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
      return "Error: Username has not been provided.\nExample Usage: gt show <username>";
    }

    // fetch user details
    Mono<GitHubUser> userMono = gitHubService.getUser(username);

    return userMono
        .map(user -> String.format(
            "login: %s\n" +
                "id: %d\n" +
                "nodeId: %s\n" +
                "avatarUrl: %s\n" +
                "gravatarId: %s\n" +
                "url: %s\n" +
                "htmlUrl: %s\n" +
                "followersUrl: %s\n" +
                "followingUrl: %s\n" +
                "gistsUrl: %s\n" +
                "starredUrl: %s\n" +
                "subscriptionsUrl: %s\n" +
                "organizationsUrl: %s\n" +
                "reposUrl: %s\n" +
                "eventsUrl: %s\n" +
                "receivedEventsUrl: %s\n" +
                "type: %s\n" +
                "siteAdmin: %b\n" +
                "name: %s\n" +
                "company: %s\n" +
                "blog: %s\n" +
                "location: %s\n" +
                "email: %s\n" +
                "hireable: %s\n" +
                "bio: %s\n" +
                "twitterUsername: %s\n" +
                "publicRepos: %d\n" +
                "publicGists: %d\n" +
                "followers: %d\n" +
                "following: %d\n" +
                "createdAt: %s\n" +
                "updatedAt: %s",
            user.login(),
            user.id(),
            user.nodeId(),
            user.avatarUrl(),
            user.gravatarId(),
            user.url(),
            user.htmlUrl(),
            user.followersUrl(),
            user.followingUrl(),
            user.gistsUrl(),
            user.starredUrl(),
            user.subscriptionsUrl(),
            user.organizationsUrl(),
            user.reposUrl(),
            user.eventsUrl(),
            user.receivedEventsUrl(),
            user.type(),
            user.siteAdmin(),
            user.name(),
            user.company(),
            user.blog(),
            user.location(),
            user.email(),
            user.hireable(),
            user.bio(),
            user.twitterUsername(),
            user.publicRepos(),
            user.publicGists(),
            user.followers(),
            user.following(),
            user.createdAt(),
            user.updatedAt()))
        .onErrorReturn("Error: Unable to fetch user details.")
        .block();

  }
}
