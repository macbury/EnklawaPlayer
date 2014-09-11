package macbury.enklawa.views;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;
import macbury.enklawa.R;

/**
 * Created by macbury on 11.09.14.
 */
public class CoolProgress extends SmoothProgressBar {
  public CoolProgress(Activity context) {
    super(context, null, R.attr.spbStyle);

    this.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 25));
    final FrameLayout decorView = (FrameLayout) context.getWindow().getDecorView();
    decorView.addView(this);


    ViewTreeObserver observer = this.getViewTreeObserver();
    observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        View contentView = decorView.findViewById(android.R.id.content);
        CoolProgress.this.setY(contentView.getY() - CoolProgress.this.getHeight() / 2);
        ViewTreeObserver observer = CoolProgress.this.getViewTreeObserver();
        observer.removeGlobalOnLayoutListener(this);
      }
    });
    //this.setIndeterminate(true);
    //this.progressiveStart();
    this.progressiveStop();
  }
}
