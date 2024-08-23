package com.github.GitTrack;

import java.time.Duration;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

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
    System.out.println("Retrieving user details...\n");
    return this.client.get()
        .uri("/users/{username}", username)
        .header("accept", "application/vnd.github+json")
        .retrieve()
        .bodyToMono(GitHubUser.class)
        .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)));
  }
}
