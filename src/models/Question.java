package models;

import java.util.List;

public class Question {
  private Long id;
  private String question;
  private List<Long> correctAnswers;
  private String answer1;
  private String answer2;
  private String answer3;
  private String answer4;
  private Long imageId;

  private boolean answer;

  // Getters

  public String getQuestion() {
    return question;
  }

  public List<Long> getCorrectAnswers() {
    return correctAnswers;
  }

  public String getAnswer1() {
    return answer1;
  }

  public String getAnswer2() {
    return answer2;
  }

  public String getAnswer3() {
    return answer3;
  }

  public String getAnswer4() {
    return answer4;
  }

  public Long getImageId() {
    return imageId;
  }

  public boolean getAnswer() {
    return answer;
  }

  public void setAnswer(boolean answer) {
    this.answer = answer;
  }

  public Question(
      Long id,
      String question,
      List<Long> correctAnswers,
      String answer1,
      String answer2,
      String answer3,
      String answer4,
      Long imageId
  ) {
    this.id = id;
    this.question = question;
    this.correctAnswers = correctAnswers;
    this.answer1 = answer1;
    this.answer2 = answer2;
    this.answer3 = answer3;
    this.answer4 = answer4;
    this.imageId = imageId;
  }

  public String toString() {
    return id + ". " + question + " ?" + getAnswer();
  }
}
