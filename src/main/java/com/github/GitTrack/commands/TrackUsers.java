package com.github.GitTrack.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * TrackUsers
 */
@ShellComponent
public class TrackUsers {

  @ShellMethod("List public user details of a GitHub user.")
  public String show(
      @ShellOption(value = { "--user",
          "-u" }, defaultValue = "", help = "The username of the GitHub user") String username) {

    if (username.trim().isEmpty()) {
      return " Error: Username has not been provided.\n Example Usage: gt show -u <username>";
    }

    String res = String.format(" Hello %s", username);
    return res;
  }
}
