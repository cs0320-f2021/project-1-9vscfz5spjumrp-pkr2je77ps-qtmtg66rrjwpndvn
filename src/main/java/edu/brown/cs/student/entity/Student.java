package edu.brown.cs.student.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * class Student, storing all information
 */
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
  private String meeting;
  private String grade;
  private double years_of_experience;
  private String horoscope;
  private String meeting_times;
  private String preferred_language;
  private String marginalized_groups;
  private String prefer_group;

  public String getMeeting() {
    return meeting;
  }

  public String getGrade() {
    return grade;
  }

  public double getYears_of_experience() {
    return years_of_experience;
  }

  public String getHoroscope() {
    return horoscope;
  }

  public String getMeeting_times() {
    return meeting_times;
  }

  public String getPreferred_language() {
    return preferred_language;
  }

  public String getMarginalized_groups() {
    return marginalized_groups;
  }

  public String getPrefer_group() {
    return prefer_group;
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

  public void setIdentityData(IdentityData identityData) {
    this.meeting = identityData.getMeeting();
    this.grade = identityData.getGrade();
    this.years_of_experience = identityData.getYears_of_experience();
    this.horoscope = identityData.getHoroscope();
    this.meeting_times = identityData.getMeeting_times();
    this.preferred_language = identityData.getPreferred_language();
    this.marginalized_groups = identityData.getMarginalized_groups();
    this.prefer_group = identityData.getPrefer_group();
  }

  @Override
  public String toString() {
    return "Student{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", commenting=" + commenting +
        ", testing=" + testing +
        ", OOP=" + OOP +
        ", algorithms=" + algorithms +
        ", teamwork=" + teamwork +
        ", frontend=" + frontend +
        ", negatives=" + negatives +
        ", positives=" + positives +
        ", interests=" + interests +
        ", meeting='" + meeting + '\'' +
        ", grade='" + grade + '\'' +
        ", years_of_experience=" + years_of_experience +
        ", horoscope='" + horoscope + '\'' +
        ", meeting_times='" + meeting_times + '\'' +
        ", preferred_language='" + preferred_language + '\'' +
        ", marginalized_groups='" + marginalized_groups + '\'' +
        ", prefer_group='" + prefer_group + '\'' +
        '}';
  }
}
