package edu.brown.cs.student.entity;

public class IdentityData {
  private int id;
  private String name;
  private String meeting;
  private String grade;
  private double years_of_experience;
  private String horoscope;
  private String meeting_times;
  private String preferred_language;
  private String marginalized_groups;
  private String prefer_group;

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

  public String getMeeting() {
    return meeting;
  }

  public void setMeeting(String meeting) {
    this.meeting = meeting;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    this.grade = grade;
  }

  public double getYears_of_experience() {
    return years_of_experience;
  }

  public void setYears_of_experience(double years_of_experience) {
    this.years_of_experience = years_of_experience;
  }

  public String getHoroscope() {
    return horoscope;
  }

  public void setHoroscope(String horoscope) {
    this.horoscope = horoscope;
  }

  public String getMeeting_times() {
    return meeting_times;
  }

  public void setMeeting_times(String meeting_times) {
    this.meeting_times = meeting_times;
  }

  public String getPreferred_language() {
    return preferred_language;
  }

  public void setPreferred_language(String preferred_language) {
    this.preferred_language = preferred_language;
  }

  public String getMarginalized_groups() {
    return marginalized_groups;
  }

  public void setMarginalized_groups(String marginalized_groups) {
    this.marginalized_groups = marginalized_groups;
  }

  public String getPrefer_group() {
    return prefer_group;
  }

  public void setPrefer_group(String prefer_group) {
    this.prefer_group = prefer_group;
  }
}
