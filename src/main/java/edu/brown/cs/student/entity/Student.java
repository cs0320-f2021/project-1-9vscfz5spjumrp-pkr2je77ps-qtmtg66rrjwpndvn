package edu.brown.cs.student.entity;

import java.util.ArrayList;
import java.util.List;

public class Student {
  private Skills skills;
  private List<String> negatives = new ArrayList<>();
  private List<String> positives = new ArrayList<>();
  private List<String> interests = new ArrayList<>();

  public Skills getSkills() {
    return skills;
  }

  public void setSkills(Skills skills) {
    this.skills = skills;
  }

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
}
