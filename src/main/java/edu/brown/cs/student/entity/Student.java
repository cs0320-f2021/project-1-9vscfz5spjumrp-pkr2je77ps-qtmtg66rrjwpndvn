package edu.brown.cs.student.entity;

import java.util.ArrayList;
import java.util.List;

public class Student {
  private int id;
  private String name;
  private int commenting;
  private int testing;
  private int OOP;
  private int algorithms;
  private int teamwork;
  private int frontend;
  private List<String> negatives = new ArrayList<>();
  private List<String> positives = new ArrayList<>();
  private List<String> interests = new ArrayList<>();

  public List<String> getNegatives() {
    return negatives;
  }

  public void addNegative(String negative) {
    this.negatives.add(negative);
  }

  public List<String> getPositives() {
    return positives;
  }

  public void addPositive(String positive) {
    this.positives.add(positive);
  }

  public List<String> getInterests() {
    return interests;
  }

  public void addInterest(String interest) {
    this.interests.add(interest);
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getCommenting() {
    return commenting;
  }

  public int getTesting() {
    return testing;
  }

  public int getOOP() {
    return OOP;
  }

  public int getAlgorithms() {
    return algorithms;
  }

  public int getTeamwork() {
    return teamwork;
  }

  public int getFrontend() {
    return frontend;
  }

  public void setSkills(Skills skill) {
    this.id = skill.getId();
    this.name = skill.getName();
    this.commenting = skill.getCommenting();
    this.testing = skill.getTesting();
    this.OOP = skill.getOOP();
    this.algorithms = skill.getAlgorithms();
    this.teamwork = skill.getTeamwork();
    this.frontend = skill.getFrontend();
  }
}
