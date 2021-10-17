package edu.brown.cs.student.main.command;

import edu.brown.cs.student.recsys.RecommendationSystem;

public class RecSysGenGroupsCommand implements Command {
  @Override
  public void execute(String[] args) {
    //make sure that the input is actually valid
    if (args.length != 2) {
      System.out.println(
          "Error: Invalid number of arguments provided to the 'recsys_rec' command."
              + "Your REPL input should follow the following format:"
              + "recsys_rec <team_size>");
    }
    //parse teamSize argument
    int teamSize = Integer.parseInt(args[1]);
    //generate set of best matched teams for entire dataset
    String message = RecommendationSystem.getInstance().genBestMatchedTeams(teamSize);
    System.out.println(message);
  }
}
