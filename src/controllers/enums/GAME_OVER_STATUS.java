package controllers.enums;

/**
 * Game over statuses
 *    SUCCESSFUL      - game finished with time left and less than 5 incorrect answers
 *    TIME_UP         - game finished with no time left but with less than 5 incorrect answers
 *    FAILED_ANSWERS  - game finished with time left but with 5 incorrect answers
 */
public enum GAME_OVER_STATUS {
  SUCCESSFUL,
  TIME_UP,
  FAILED_ANSWERS
}
