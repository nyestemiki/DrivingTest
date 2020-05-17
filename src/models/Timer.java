package models;

import controllers.GameManager;

/**
 * Models a timer
 */
public class Timer extends Thread {
  private int currentTime; // in seconds
  private boolean active; // State of the timer
  private int initialTimeInterval;

  public Timer() {
    initialTimeInterval = GameManager.getMaxTimeAvailable();
    currentTime = initialTimeInterval;
    active = false;
  }

  /**
   * Returns the current time in seconds
   *
   * @return current time in seconds
   */
  public int getCurrentTime() {
    return currentTime;
  }

  /**
   * Restores the initial time interval value
   */
  public void restart() {
    currentTime = initialTimeInterval;
  }

  /**
   * Sets the state of the timer to inactive
   */
  public void stopTimer() {
    active = false;
  }

  /**
   * Activates the timer
   */
  public void setActive() {
    this.active = true;
  }

  @Override
  public void run() {
    // Timer is always active when it is being run
    active = true;

    // The timer was not stopped from the outside and there is still time
    while(active && currentTime != 0) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      currentTime--;
    }

    // If timer was not stopped from the outside and time is up
    if (active) {
      GameManager.getInstance().time_up();
    }

    // Timer is inactive ( terminated )
    active = false;
  }

  /**
   * Displays current time as min:sec
   *
   * @return current time in string format
   */
  public String toString() {
    return currentTime / 60 + ":" + currentTime % 60;
  }
}
