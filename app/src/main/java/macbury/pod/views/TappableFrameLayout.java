package macbury.pod.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by macbury on 15.09.14.
 */
public class TappableFrameLayout extends ImageView implements View.OnTouchListener {

  public TappableFrameLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    setOnTouchListener(this);
  }

  public TappableFrameLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    setOnTouchListener(this);
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_DOWN) {
      setAlpha(0.5f);
    } else {
      setAlpha(1f);
    }
    return true;
  }
}
