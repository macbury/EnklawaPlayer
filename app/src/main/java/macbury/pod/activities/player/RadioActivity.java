package macbury.pod.activities.player;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.appbrain.AppBrain;
import com.negusoft.holoaccent.activity.AccentActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import macbury.pod.R;
import macbury.pod.managers.App;

public class RadioActivity extends AccentActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AppBrain.init(this);
    setContentView(R.layout.activity_radio);
    SystemBarTintManager tintManager = new SystemBarTintManager(this);
    tintManager.setStatusBarTintEnabled(true);
    tintManager.setStatusBarTintResource(R.color.statusbar_color);

    View mainContainer = findViewById(R.id.main_container);
    SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      mainContainer.setPadding(0, config.getPixelInsetTop(true), config.getPixelInsetRight(), config.getPixelInsetBottom());
    }
    WebView webView = (WebView) findViewById(R.id.chatWebView);
    webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.i("TEST", url);
        return super.shouldOverrideUrlLoading(view, url);
      }

      @Override
      public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Log.i("TEST", description);
        super.onReceivedError(view, errorCode, description, failingUrl);
      }

      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.i("TEST", "START");
        super.onPageStarted(view, url, favicon);
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.i("TEST", "END");
      }
    });
    webView.loadUrl("http://www7.cbox.ws/box/?boxid=470567&boxtag=4tj2gk&sec=main");

  }

  @Override
  protected void onResume() {
    super.onResume();
    App app = App.current();
    app.services.stopPlayer();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return true;
  }

}
