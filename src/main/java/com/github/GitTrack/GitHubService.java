package com.github.GitTrack;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

/**
 * GitHubService
 */
@Service
public class GitHubService {
  private final WebClient client;

  public GitHubService() {
    this.client = WebClient.create("https://api.github.com");
  }

  public Mono<GitHubUser> getUser(String username) {
    return this.client.get()
        .uri("/users/{username}", username)
        .header("accept", "application/vnd.github+json")
        .retrieve()
        .bodyToMono(GitHubUser.class);
  }
}
