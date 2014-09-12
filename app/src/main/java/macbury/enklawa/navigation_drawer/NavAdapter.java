package macbury.enklawa.navigation_drawer;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;

import be.webelite.ion.IconView;
import macbury.enklawa.R;
import macbury.enklawa.navigation_drawer.items.NavItemFragment;

/**
 * Created by macbury on 11.09.14.
 */
public class NavAdapter extends BaseAdapter{
  private final ArrayList<NavBaseItem> items;
  private final ArrayList<Class<? extends NavBaseItem>> viewTypes;
  private final Context context;
  private int selected;

  public NavAdapter(Context context) {
    this.context    = context;
    this.items      = new ArrayList<NavBaseItem>();
    this.viewTypes  = new ArrayList<Class<? extends NavBaseItem>>();
  }

  public void add(NavBaseItem item) {
    items.add(item);
    if (!viewTypes.contains(item.getClass())) {
      viewTypes.add(item.getClass());
    }
  }

  public void clear() {
    viewTypes.clear();
    items.clear();
  }

  @Override
  public int getCount() {
    return items.size();
  }

  @Override
  public NavBaseItem getItem(int position) {
    return items.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    NavBaseItem item = getItem(position);
    if (isNavItemFragment(position)) {
      return getNavItemView((NavItemFragment)item, convertView, parent, position);
    } else if(isDivider(position)) {
      return getDividerItemView((NavDivider)item, convertView, parent);
    }
    return null;
  }

  public boolean isNavItemFragment(int position) {
    return NavItemFragment.class.isInstance(getItem(position));
  }

  private View getDividerItemView(NavDivider item, View convertView, ViewGroup parent) {
    DividerItemHolder holder = null;
    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView             = inflater.inflate(R.layout.nav_divider_item, parent, false);
      holder                  = new DividerItemHolder();
      holder.titleView        = (TextView)convertView.findViewById(R.id.nav_item_title_view);
      convertView.setTag(holder);
    } else {
      holder                  = (DividerItemHolder) convertView.getTag();
    }

    holder.titleView.setText(item.getTitle());

    return convertView;
  }

  private View getNavItemView(NavItemFragment item, View convertView, ViewGroup parent, int position) {
    SimpleNavItemHolder holder = null;

    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView             = inflater.inflate(R.layout.nav_item_simple, parent, false);
      holder                  = new SimpleNavItemHolder();
      holder.iconView         = (IconView) convertView.findViewById(R.id.nav_item_icon_view);
      holder.titleView        = (TextView) convertView.findViewById(R.id.nav_item_title_view);

      convertView.setTag(holder);
    } else {
      holder = (SimpleNavItemHolder) convertView.getTag();
    }

    if (position == selected) {
      holder.titleView.setTypeface(null, Typeface.BOLD);
    } else {
      holder.titleView.setTypeface(null, Typeface.NORMAL);
    }

    holder.titleView.setText(item.getTitle());
    holder.iconView.setIcon(item.getIcon());
    return convertView;
  }

  @Override
  public int getViewTypeCount() {
    return viewTypes.size();
  }

  @Override
  public int getItemViewType(int position) {
    return getItem(position).getType();
  }

  public boolean isDivider(int position) {
    return NavDivider.class.isInstance(getItem(position));
  }

  public void setSelected(int selected) {
    this.selected = selected;
    this.notifyDataSetChanged();
  }

  public int getSelected() {
    return selected;
  }

  public class SimpleNavItemHolder {
    public IconView         iconView;
    public TextView         titleView;
  }

  public class DividerItemHolder {
    public TextView         titleView;
  }
}
