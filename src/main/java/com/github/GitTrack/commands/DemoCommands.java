package com.github.GitTrack.commands;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * DemoCommands
 */
@ShellComponent
public class DemoCommands {

  @ShellMethod(key = "hello", value = "I will say hello")
  public String hello(@ShellOption(defaultValue = "World") String arg) {
    return "Hello " + arg + "!";
  }

  @ShellMethod(key = "bye", value = "I will say bye")
  public String bye() {
    return "bye...";
  }
}
