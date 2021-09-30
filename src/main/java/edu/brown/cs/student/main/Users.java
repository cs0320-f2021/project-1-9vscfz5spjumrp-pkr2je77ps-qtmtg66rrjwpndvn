package edu.brown.cs.student.main;

public class Users {
  private String user_id;
  private String weight;
  private String bust_size;
  private String height;
  private String age;
  private String body_type;
  private String horoscope;

  public String getUser_id() {
    return user_id;
  }

  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }

  public String getWeight() {
    return weight;
  }

  public void setWeight(String weight) {
    this.weight = weight;
  }

  public String getBust_size() {
    return bust_size;
  }

  public void setBust_size(String bust_size) {
    this.bust_size = bust_size;
  }

  public String getHeight() {
    return height;
  }

  public void setHeight(String height) {
    this.height = height;
  }

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }

  public String getBody_type() {
    return body_type;
  }

  public void setBody_type(String body_type) {
    this.body_type = body_type;
  }

  public String getHoroscope() {
    return horoscope;
  }

  public void setHoroscope(String horoscope) {
    this.horoscope = horoscope;
  }

  @Override
  public String toString() {
    return "Users{" +
        "user_id='" + user_id + '\'' +
        ", weight='" + weight + '\'' +
        ", bust_size='" + bust_size + '\'' +
        ", height='" + height + '\'' +
        ", age='" + age + '\'' +
        ", body_type='" + body_type + '\'' +
        ", horoscope='" + horoscope + '\'' +
        '}';
  }
}
