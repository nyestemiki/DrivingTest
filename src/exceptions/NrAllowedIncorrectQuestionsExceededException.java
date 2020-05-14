package exceptions;

public class NrAllowedIncorrectQuestionsExceededException extends Exception {
  public NrAllowedIncorrectQuestionsExceededException() {
    super("The number of incorrect questions has exceeded the maximum number of such questions allowed!");
  }
}
