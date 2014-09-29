package macbury.enklawa.extensions;

import android.os.Handler;

import macbury.enklawa.managers.Enklawa;

/**
 * Created by macbury on 23.09.14.
 */
public class SleepTimer extends Thread {

  private final SleepTimerListener listener;
  private final int delay;
  private final Handler mainHandler;
  private final Runnable listenerTickRunnable;
  private boolean running;

  public SleepTimer(int delay, SleepTimerListener listener) {
    this.mainHandler          = new Handler(Enklawa.current().getMainLooper());
    this.listener             = listener;
    this.delay                = delay;
    this.running              = true;
    this.listenerTickRunnable = new Runnable() {
      @Override
      public void run() {
        if (running)
          SleepTimer.this.listener.onSleepTimerTick(SleepTimer.this);
      }
    };
  }

  @Override
  public void run() {
    while (running) {
      try {
        Thread.sleep(delay);
        mainHandler.post(listenerTickRunnable);
      } catch (InterruptedException e) {
        e.printStackTrace();
        running = false;
      }
    }
  }

  public void kill() {
    this.running = false;
  }

  public interface SleepTimerListener {
    public void onSleepTimerTick(SleepTimer timer);
  }
}
