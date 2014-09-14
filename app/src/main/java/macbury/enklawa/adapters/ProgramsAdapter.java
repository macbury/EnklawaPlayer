package macbury.enklawa.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import macbury.enklawa.db.models.Program;
import macbury.enklawa.fragments.main.AllProgramsFragment;

/**
 * Created by macbury on 12.09.14.
 */
public class ProgramsAdapter extends BaseAdapter {
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
    return null;
  }
}
