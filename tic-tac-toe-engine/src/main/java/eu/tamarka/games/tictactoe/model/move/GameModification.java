package eu.tamarka.games.tictactoe.model.move;

import java.util.Arrays;

public class GameModification {

  private final ModificationType type;

  private final int priority;

  private final String[] parameters;

  public GameModification(ModificationType type, int priority, String[] parameters) {
    this.type = type;
    this.priority = priority;
    this.parameters = parameters;
  }

  public ModificationType getType() {
    return type;
  }

  public int getPriority() {
    return priority;
  }

  public String[] getParameters() {
    return parameters;
  }

  @Override
  public String toString() {
    return "{" +
        "\"type\":" + (type == null ? "null" : type) + ", " +
        "\"priority\":\"" + priority + "\"" + ", " +
        "\"parameters\":" + Arrays.toString(parameters) +
        "}";
  }
}