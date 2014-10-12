package macbury.pod.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import macbury.pod.R;
import macbury.pod.db.models.Program;

/**
 * Created by macbury on 17.09.14.
 */
public class ProgramAboutDialog extends Dialog {
  private Program program;
  private TextView titleTextView;
  private HtmlTextView descriptionTextView;

  public ProgramAboutDialog(Context context, Program program) {
    super(context);
    this.program = program;
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.dialog_program_details);

    titleTextView        = (TextView)findViewById(R.id.program_title);
    descriptionTextView  = (HtmlTextView)findViewById(R.id.program_description);

    titleTextView.setText(program.name);
    descriptionTextView.setHtmlFromString(program.description, true);
    //int programId = getArguments().getInt(IntentManager.EXTRA_PROGRAM_ID);
    //program       = Enklawa.current().db.programs.find(programId);

  }

}
