package com.github.GitTrack;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * GitHubService
 */
@Service
public class GitHubService {
  private final String baseUrl = "https://api.github.com";

  private final com.github.GitTrack.Formatter formatter;

  // Auto-inject formatter through constructor injection
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
      if (statusCode != 200) {
        return (String.format("User %s could not be found...", username));
      }

      // Parse JSON response for the body(String) and create a new JSONObject with it
      String responseBody = response.body();
      JSONObject responseJson = new JSONObject(responseBody);

      // Extract and format data from JSON
      return this.formatter.formatUserDetails(responseJson);

    } catch (Exception e) {
      e.printStackTrace();
      return "Error occurred.";
    }
  }

  public String getActivity(String username) {
    try {
      // Create request template for https://api.github.com/users/{username}/events
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(baseUrl + "/users/" + username + "/events?per_page=30"))
          .header("accept", "application/vnd.github+json")
          .build();

      // Receive response body after sending request
      HttpResponse<String> response = HttpClient.newHttpClient()
          .send(request, HttpResponse.BodyHandlers.ofString());

      // Include check for failed response
      int statusCode = response.statusCode();
      if (statusCode != 200) {
        return String.format("Events for %s could not be found...", username);
      }

      // Parse response body and turn it into a JSONArray
      String responseBody = response.body();
      JSONArray responseJsonArray = new JSONArray(responseBody);

      // Extract and format data from JSON
      return this.formatter.formatEvent(responseJsonArray);

    } catch (Exception e) {
      e.printStackTrace();
      return "Error occurred.";
    }
  }
}
