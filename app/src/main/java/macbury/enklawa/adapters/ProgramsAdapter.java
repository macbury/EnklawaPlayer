package macbury.enklawa.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.bitmap.BitmapInfo;
import com.koushikdutta.ion.bitmap.PostProcess;


import java.util.ArrayList;
import java.util.List;

import macbury.enklawa.R;
import macbury.enklawa.db.models.Program;
import macbury.enklawa.fragments.main.AllProgramsFragment;
/**
 * Created by macbury on 12.09.14.
 */
public class ProgramsAdapter extends BaseAdapter {
  private static final String TAG = "ProgramsAdapter";
  private final Context context;
  private final ArrayList<Program> programs;

  public ProgramsAdapter(Context context, List<Program> programs) {
    this.context = context;
    this.programs = new ArrayList<Program>();
    set(programs);
  }


  public void set(List<Program> nprogs) {
    this.programs.clear();
    this.programs.addAll(nprogs);
    this.notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return programs.size();
  }

  @Override
  public Program getItem(int position) {
    return programs.get(position);
  }

  @Override
  public long getItemId(int position) {
    return getItem(position).id;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    ProgramHolder holder;

    if (convertView == null) {
      holder                      = new ProgramHolder();
      LayoutInflater inflater     = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView                 = inflater.inflate(R.layout.program_grid_item, parent, false);
      holder.programPreview       = (ImageView)convertView.findViewById(R.id.program_preview);
      holder.programTitle         = (TextView)convertView.findViewById(R.id.program_title);
      convertView.setTag(holder);
    } else {
      holder                      = (ProgramHolder)convertView.getTag();
    }

    Program program = getItem(position);

    Ion.with(context).load(program.image).withBitmap().intoImageView(holder.programPreview);
    holder.programTitle.setText(program.name);
    return convertView;
  }

  public class ProgramHolder {
    public ImageView programPreview;
    public TextView programTitle;
  }
}
