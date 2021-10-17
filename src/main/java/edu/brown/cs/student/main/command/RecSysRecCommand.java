package edu.brown.cs.student.main.command;

import edu.brown.cs.student.recsys.RecommendationSystem;

public class RecSysRecCommand implements Command {
  @Override
  public void execute(String[] args) {
    //make sure that the input is actually valid
    if (args.length != 3) {
      System.out.println(
          "Error: Too many arguments provided to the 'recsys_rec' command."
              + "Your REPL input should follow the following format:"
              + "recsys_rec <num_recs> <student_id>");
    }
    //parse rest of args
    int numRecs = Integer.parseInt(args[1]);
    int studentId = Integer.parseInt(args[2]);
    //get recs
    String message = RecommendationSystem.getInstance().genRecsForTeam(numRecs, studentId);
    System.out.println(message);
  }
}
