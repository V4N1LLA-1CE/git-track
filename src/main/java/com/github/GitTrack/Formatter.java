package com.github.GitTrack;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.*;
import java.util.ArrayList;

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
  private final String RED = "\u001B[31m";
  private final String GREEN = "\u001B[32m";
  private final String RESET = "\u001B[0m";
  private final String BOLD = "\u001B[1m";
  private final String BLUE = "\u001B[34m";
  private final String UNDERLINE = "\u001B[4m";

  /**
   * Formats a JSONObject containing GitHub user details into a human-readable
   * string.
   * This method takes a JSONObject with user details and formats them into a
   * structured
   * output for display.
   *
   * @param jsonObject The JSONObject containing GitHub user details
   * @return A formated string with the user's details that will be displayed on
   *         the CLI
   */
  public String formatUserDetails(JSONObject jsonObject) {
    // Format each field into a readable string
    String header = BOLD + BLUE + "Show User Details\n" + RESET;
    String separator = BOLD + BLUE
        + "================================================================================\n\n" + RESET;

    // Table rows
    String[] rows = {
        formatRow("Username", jsonObject.optString("login", "-")),
        formatRow("Account ID", String.valueOf(jsonObject.optInt("id", -1))),
        formatRow("Avatar Link", jsonObject.optString("avatar_url", "-")),
        formatRow("Profile Link", jsonObject.optString("html_url", "-")),
        formatRow("Name", jsonObject.optString("name", "-")),
        formatRow("Company", jsonObject.optString("company", "-")),
        formatRow("Blog", jsonObject.optString("blog", "").trim().isEmpty() ? "-" : jsonObject.optString("blog")),
        formatRow("Location", jsonObject.optString("location", "-")),
        formatRow("Email", jsonObject.optString("email", "-")),
        formatRow("Hireable", jsonObject.optBoolean("hireable", false) ? "true" : "false"),
        formatRow("Bio", jsonObject.optString("bio", "-")),
        formatRow("Twitter Username", jsonObject.optString("twitter_username", "-")),
        formatRow("Public Repos", String.valueOf(jsonObject.optInt("public_repos", 0))),
        formatRow("Public Gists", String.valueOf(jsonObject.optInt("public_gists", 0))),
        formatRow("Followers", String.valueOf(jsonObject.optInt("followers", 0))),
        formatRow("Following", String.valueOf(jsonObject.optInt("following", 0))),
        formatRow("Created At", formatDate(jsonObject.optString("created_at", "-"))),
        formatRow("Last Updated", formatDate(jsonObject.optString("updated_at", "-")))
    };

    /**
     * Use stringbuilder for better performance as I don't want to create new String
     * objects everytime I use + operator.
     */
    StringBuilder result = new StringBuilder();
    result.append(header).append(separator);

    for (String row : rows) {
      result.append(row).append("\n");
    }
    result.append("\n").append(separator);

    return result.toString();
  }

  /**
   * Formats a key-value pair into a table row.
   * 
   * @param key   - The key (e.g., "Public Repos")
   * @param value - The value (e.g., "1")
   * @return A formatted string with the key and value in a table row format
   */
  private String formatRow(String key, String value) {
    int keyWidth = 20; // Adjust the width as needed
    return String.format(BOLD + BLUE + "%-" + keyWidth + "s:" + RESET + " %s", key, value);
  }

  /**
   * Converts an ISO 8601 formatted date string to a more readable format (e.g.,
   * "1st February 2009, 12:04 PM ").
   *
   * @param dateString - The ISO 8601 date string to format (e.g.,
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

  /**
   * Gets suffix of a number i.e. 'st', 'nd' etc
   *
   * @param day - The day number in a month i.e. 1-31, 1-30, 1-28 (feb)
   * @return The suffix string i.e. 'th'
   */
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
