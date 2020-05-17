package controllers.enums;

public enum GAME_OVER_STATUS {
  SUCCESSFUL,     // game finished with time left and less than the maximum number of incorrect answers
  TIME_UP,        // game finished with no time left but with less than the maximum number of incorrect answers
  FAILED_ANSWERS  // game finished with time left but with more incorrect answers than the maximum number allowed
}
