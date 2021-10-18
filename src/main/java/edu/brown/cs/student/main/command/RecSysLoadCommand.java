package edu.brown.cs.student.main.command;

import edu.brown.cs.student.recsys.RecommendationSystem;

/**
 * class to implement recsys_load command
 */
public class RecSysLoadCommand implements Command {
  @Override
  public void execute(String[] args) {
    try {
      System.out.println(RecommendationSystem.getInstance().loadData());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
