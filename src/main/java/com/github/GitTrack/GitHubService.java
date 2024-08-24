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
  private final String baseUrl = "https://api.github.com";
  private WebClient client;
  private String token;

  public GitHubService() {
    this.client = this.createClient();
  }

  public WebClient createClient() {
    // build client
    WebClient.Builder builder = WebClient.builder()
        .baseUrl(this.baseUrl)
        .defaultHeader("accept", "application/vnd.github+json");

    if (token != null && !token.isEmpty()) {
      builder.defaultHeader("authorization", "Bearer " + token);
    }

    return builder.build();

  }

  public void setToken(String token) {
    // set token and set
    this.token = token;
    this.client = createClient();
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
