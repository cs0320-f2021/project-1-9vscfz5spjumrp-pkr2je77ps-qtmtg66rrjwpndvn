package edu.brown.cs.student.entity;

/**
 * class for skills
 */
public class Skills {
  private int id;
  private String name;
  private int commenting;
  private int testing;
  private int OOP;
  private int algorithms;
  private int teamwork;
  private int frontend;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCommenting() {
    return commenting;
  }

  public void setCommenting(int commenting) {
    this.commenting = commenting;
  }

  public int getTesting() {
    return testing;
  }

  public void setTesting(int testing) {
    this.testing = testing;
  }

  public int getOOP() {
    return OOP;
  }

  public void setOOP(int OOP) {
    this.OOP = OOP;
  }

  public int getAlgorithms() {
    return algorithms;
  }

  public void setAlgorithms(int algorithms) {
    this.algorithms = algorithms;
  }

  public int getTeamwork() {
    return teamwork;
  }

  public void setTeamwork(int teamwork) {
    this.teamwork = teamwork;
  }

  public int getFrontend() {
    return frontend;
  }

  public void setFrontend(int frontend) {
    this.frontend = frontend;
  }
}
