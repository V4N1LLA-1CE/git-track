package com.github.GitTrack;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

/**
 * GitHubService
 */
@Service
public class GitHubService {
  private final String baseUrl = "https://api.github.com";
  private WebClient client;

  public GitHubService() {
    // create a client
    this.client = WebClient.create(baseUrl);
  }

  public Mono<GitHubUser> getUser(String username) {
    Mono<GitHubUser> result = this.client.get()
        .uri("/users/{username}", username)
        .header("accept", "application/vnd.github+json")
        .retrieve()
        .bodyToMono(GitHubUser.class)
        .retry(3);

    return result;
  }
}
