package macbury.enklawa.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.w3c.dom.Text;

import macbury.enklawa.R;
import macbury.enklawa.db.models.Program;
import macbury.enklawa.managers.Enklawa;
import macbury.enklawa.managers.IntentManager;

/**
 * Created by macbury on 17.09.14.
 */
public class ProgramAboutDialog extends Dialog {
  private Program program;
  private TextView titleTextView;

  public ProgramAboutDialog(Context context, Program program) {
    super(context);
    this.program = program;
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.dialog_program_details);

    titleTextView   = (TextView)findViewById(R.id.program_title);


    titleTextView.setText(program.name);
    //int programId = getArguments().getInt(IntentManager.EXTRA_PROGRAM_ID);
    //program       = Enklawa.current().db.programs.find(programId);

  }

}
