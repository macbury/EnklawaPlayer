package macbury.pod.tutorial;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;

import macbury.pod.R;
import macbury.pod.activities.main.MainActivity;

/**
 * Created by macbury on 09.10.14.
 */
public class MainActivityTutorial {
  private final ShowcaseView sv;

  public MainActivityTutorial(MainActivity activity) {
    //App.current().settings.doneTutorial();
    this.sv = new ShowcaseView.Builder(activity)
                  .setStyle(R.style.AppShowcaseTheme)
                  .setTarget(new ActionViewTarget(activity, ActionViewTarget.Type.HOME))
                  .setContentTitle("ShowcaseView")
                  .setContentText("This is highlighting the Home button")
                  .hideOnTouchOutside()
                  .build();
  }
}
