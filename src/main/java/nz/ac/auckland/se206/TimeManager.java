package nz.ac.auckland.se206;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import nz.ac.auckland.se206.SceneManager.AppUi;

public class TimeManager {
  private static TimeManager instance;
  private Timer timer;
  public static int timeRemaining = 121;
  private List<TimeUpdateListener> listeners = new ArrayList<>();

  /** Starts the game timer. */
  public void startTimer(int time) {
    timeRemaining = time;
    timer = new Timer();

    // update timer every second
    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public void run() {
            updateTimerText();

            // stop timer when time is up
            if (timeRemaining == 0) {
              if (!GameState.isWon) {
                App.setUi(AppUi.LOSE);
              }
              stopTimer();
              for (TimeUpdateListener listener : listeners) {
                listener.onTimerUpdate("00:00");
                // listener.getTimerLbl().setText("00:00");
              }
            }
          }
        },
        0,
        1000);
  }

  /** Gets the instance of the time manager. */
  public static TimeManager getInstance() {
    if (instance == null) {
      instance = new TimeManager();
    }
    return instance;
  }

  /** Updates the timer text for each second. */
  private void updateTimerText() {
    timeRemaining--;

    int minutes = timeRemaining / 60;
    int seconds = timeRemaining % 60;

    String formattedTime = String.format("%02d:%02d", minutes, seconds);
    for (TimeUpdateListener listener : listeners) {
      listener.onTimerUpdate(formattedTime);
    }
  }

  /** Registers a listener to the time manager. Used for each scene. */
  public void registerListener(TimeUpdateListener listener) {
    listeners.add(listener);
  }

  /** Interface for the time update listener. */
  public interface TimeUpdateListener {
    void onTimerUpdate(String formattedTime);
  }

  /** Stops the game timer. */
  public void stopTimer() {
    timer.cancel();
    timer.purge();
  }
}
