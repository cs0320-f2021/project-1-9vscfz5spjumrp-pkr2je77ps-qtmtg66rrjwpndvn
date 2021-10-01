package edu.brown.cs.student.main;

public class Reviews {
  private int id;
  private String review_text;
  private String review_summary;
  private String review_date;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getReview_text() {
    return review_text;
  }

  public void setReview_text(String review_text) {
    this.review_text = review_text;
  }

  public String getReview_summary() {
    return review_summary;
  }

  public void setReview_summary(String review_summary) {
    this.review_summary = review_summary;
  }

  public String getReview_date() {
    return review_date;
  }

  public void setReview_date(String review_date) {
    this.review_date = review_date;
  }

  @Override
  public String toString() {
    return "Reviews{" +
        "id=" + id +
        ", review_text='" + review_text + '\'' +
        ", review_summary='" + review_summary + '\'' +
        ", review_date='" + review_date + '\'' +
        '}';
  }
}
