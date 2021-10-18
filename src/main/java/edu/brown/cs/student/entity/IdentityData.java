package edu.brown.cs.student.entity;

/**
 * class for the identity data from api
 */
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

  /**
   * getter for id
   * @return id
   */
  public int getId() {
    return id;
  }

  /**
   * setter for id
   * @param id
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * getter for name
   * @return String name
   */
  public String getName() {
    return name;
  }

  /**
   * setter for name
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * getter for meeting
   * @return String meeting
   */
  public String getMeeting() {
    return meeting;
  }

  /**
   * setter for meeting
   * @param meeting
   */
  public void setMeeting(String meeting) {
    this.meeting = meeting;
  }

  /**
   * getter for grade
   * @return String grade
   */
  public String getGrade() {
    return grade;
  }

  /**
   * setter for grade
   * @param grade
   */
  public void setGrade(String grade) {
    this.grade = grade;
  }

  /**
   * getter for yoe
   * @return double yoe
   */
  public double getYears_of_experience() {
    return years_of_experience;
  }

  /**
   * setter for yoe
   * @param years_of_experience
   */
  public void setYears_of_experience(double years_of_experience) {
    this.years_of_experience = years_of_experience;
  }

  /**
   * getter for horoscope
   * @return String horoscope
   */
  public String getHoroscope() {
    return horoscope;
  }

  /**
   * setter for horoscope
   * @param horoscope
   */
  public void setHoroscope(String horoscope) {
    this.horoscope = horoscope;
  }

  /**
   * getter for meeting times
   * @return String meeting times
   */
  public String getMeeting_times() {
    return meeting_times;
  }

  /**
   * setter for meeting times
   * @param meeting_times
   */
  public void setMeeting_times(String meeting_times) {
    this.meeting_times = meeting_times;
  }

  /**
   * getter for preferred language
   * @return String preferred language
   */
  public String getPreferred_language() {
    return preferred_language;
  }

  /**
   * setter for preferred language
   * @param preferred_language
   */
  public void setPreferred_language(String preferred_language) {
    this.preferred_language = preferred_language;
  }

  /**
   * getter for marginalized groups
   * @return String marginalized groups
   */
  public String getMarginalized_groups() {
    return marginalized_groups;
  }

  /**
   * setter for marginalized groups
   * @param marginalized_groups
   */
  public void setMarginalized_groups(String marginalized_groups) {
    this.marginalized_groups = marginalized_groups;
  }

  /**
   * getter for preferred groups
   * @return String preferred groups
   */
  public String getPrefer_group() {
    return prefer_group;
  }

  /**
   * setter for preferred groups
   * @param prefer_group
   */
  public void setPrefer_group(String prefer_group) {
    this.prefer_group = prefer_group;
  }
}
