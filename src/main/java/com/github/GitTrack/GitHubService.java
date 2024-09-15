package com.github.GitTrack;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * GitHubService
 */
@Service
public class GitHubService {
  private final String baseUrl = "https://api.github.com";

  private final com.github.GitTrack.Formatter formatter;

  public GitHubService(Formatter formatter) {
    this.formatter = formatter;
  }

  public String getUser(String username) {
    try {
      // Create request template for https://api.github.com/users/{username}
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/users/" + username))
          .header("accept", "application/vnd.github+json") // Set Accept header
          .build();

      // Receive response body after sending request
      HttpResponse<String> response = HttpClient.newHttpClient()
          .send(request, HttpResponse.BodyHandlers.ofString());

      // Include check for failed response status code
      int statusCode = response.statusCode();
      if (statusCode == 404) {
        return (String.format("User %s could not be found...", username));
      }

      // Parse JSON response for the body(String) and create a new JSONObject with it
      String responseBody = response.body();
      JSONObject jsonObject = new JSONObject(responseBody);

      // Extract and format data from JSON
      return this.formatter.formatUserDetails(jsonObject);

    } catch (Exception e) {
      e.printStackTrace();
      return "Error occurred";
    }
  }
}
