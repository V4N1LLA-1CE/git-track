package com.github.GitTrack;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Service;

/**
 * GitHubService
 */
@Service
public class GitHubService {
  private final String baseUrl = "https://api.github.com";

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
      return formatUserDetails(jsonObject);

    } catch (Exception e) {
      e.printStackTrace();
      return "Error occurred";
    }
  }

  private String formatUserDetails(JSONObject jsonObject) {
    // Format each field into a readable string
    String result = "Show GitHub User Details\n" +
        "===================\n\n" +
        "Username: " + jsonObject.optString("login", "-") + "\n" +
        "Account ID: " + jsonObject.optInt("id", -1) + "\n" +
        "Avatar Link: " + jsonObject.optString("avatar_url", "-") + "\n" +
        "Profile Link: " + jsonObject.optString("html_url", "-") + "\n\n" +

        // "Type: " + jsonObject.optString("type", "-") + "\n" +
        // "Site Admin: " + (jsonObject.optBoolean("site_admin", false) ? "Yes" : "No")
        // + "\n\n" +

        "Name: " + jsonObject.optString("name", "-") + "\n" +
        "Company: " + jsonObject.optString("company", "-") + "\n" +
        "Blog: " + (jsonObject.optString("blog", "").trim().isEmpty() ? "-" : jsonObject.optString("blog")) + "\n" +
        "Location: " + jsonObject.optString("location", "-") + "\n" +
        "Email: " + jsonObject.optString("email", "-") + "\n" +
        "Hireable: " + (jsonObject.optBoolean("hireable", false) ? "true" : "false") + "\n" +
        "Bio: " + jsonObject.optString("bio", "-") + "\n\n" +

        "Twitter Username: " + jsonObject.optString("twitter_username", "-") + "\n\n" +

        "Public Repos: " + jsonObject.optInt("public_repos", 0) + "\n" +
        "Public Gists: " + jsonObject.optInt("public_gists", 0) + "\n" +
        "Followers: " + jsonObject.optInt("followers", 0) + "\n" +
        "Following: " + jsonObject.optInt("following", 0) + "\n\n" +

        "Created At: " + formatDate(jsonObject.optString("created_at", "-")) + "\n" +
        "Last Updated: " + formatDate(jsonObject.optString("updated_at", "-")) + "\n";

    return result;
  }

  private String formatDate(String dateString) {
    if (dateString.equals("-")) {
      return "-";
    }

    // Convert the date string to a more readable format (e.g., 1st February 2009)
    try {
      // Formatter for taking in dateString (Standard is ISO Datetime)
      java.time.format.DateTimeFormatter inputFormatter = java.time.format.DateTimeFormatter.ISO_DATE_TIME;
      // Formatter for outputting dateString with custom pattern
      java.time.format.DateTimeFormatter outputFormatter = java.time.format.DateTimeFormatter
          .ofPattern("d'th' MMMM yyyy, HH:mm");
      java.time.LocalDateTime date = java.time.LocalDateTime.parse(dateString, inputFormatter);
      return date.format(outputFormatter);
    } catch (Exception e) {
      return dateString;
    }
  }
}
