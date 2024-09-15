package com.github.GitTrack;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.*;

/**
 * Custom formatter class that includes methods related to formatting output for
 * the command line interface (CLI).
 * 
 * This class provides utility methods to format JSON objects and date strings
 * before they are displayed on the CLI.
 * It ensures that the output is presented in a readable and well-structured
 * format.
 *
 */
@Component
public class Formatter {

  /**
   * Formats a JSONObject containing GitHub user details into a human-readable
   * string.
   * This method takes a JSONObject with user details and formats them into a
   * structured
   * output for display.
   *
   * @param jsonObject The JSONObject containing GitHub user details
   * @return A formatted string with the user's details that will be displayed on
   *         the CLI
   */
  public String formatUserDetails(JSONObject jsonObject) {
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

  /**
   * Converts an ISO 8601 formatted date string to a more readable format (e.g.,
   * "1st February 2009, 12:04 PM ").
   *
   * @param dateString The ISO 8601 date string to format (e.g.,
   *                   "2009-02-01T00:00:00Z")
   * @return The formatted date string (e.g., "1st February 2009, 12:00 AM")
   */
  private String formatDate(String dateString) {
    if (dateString.equals("-")) {
      return "-";
    }

    // Convert the date string to a more readable format
    // (e.g., 1st February 2009, 11:23 AM)
    try {

      // Formatter for taking in dateString (ISO Datetime String)
      DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_DATE_TIME;

      // Formatter for outputting Month Year, 12HR AM/PM
      DateTimeFormatter monthYearTimeFormatter = DateTimeFormatter
          .ofPattern(" MMMM yyyy, hh:mm a");

      // Formatter for outputting integer day of month
      DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("d");

      // Get date object
      LocalDateTime date = LocalDateTime.parse(dateString, inputFormatter);

      // Format output for Month Year and Time
      String monthYearTimeDateString = date.format(monthYearTimeFormatter);

      // Create day string with suffix i.e. '21st'
      String dayDateString = "";
      dayDateString += date.format(dayFormatter);

      // Get the integer value of day and get suffix
      int dayInt = Integer.parseInt(date.format(dayFormatter));
      dayDateString += getDateSuffix(dayInt);

      // Build the full formatted date (day+suffix MMMM yyyy, hh:mm a)
      String formattedDate = dayDateString + monthYearTimeDateString;

      // Replace 'am' with 'AM' and 'pm' with 'PM' using regex case sensitive flag
      return formattedDate.replaceAll("(?i)am", "AM").replaceAll("(?i)pm", "PM");

    } catch (Exception e) {
      return dateString;
    }
  }

  private String getDateSuffix(int day) {
    // Eleventh, Twelveth, Thirteenth
    if (day >= 11 && day <= 13) {
      return "th";
    }

    switch (day % 10) {
      case 1:
        return "st";

      case 2:
        return "nd";

      case 3:
        return "rd";

      default:
        return "th";
    }

  }
}
