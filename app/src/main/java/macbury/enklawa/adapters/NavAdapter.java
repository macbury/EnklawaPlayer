package macbury.enklawa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shamanland.fonticon.FontIconTextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import macbury.enklawa.R;
import macbury.enklawa.adapters.navigation.NavBaseItem;
import macbury.enklawa.adapters.navigation.NavItemFragment;

/**
 * Created by macbury on 11.09.14.
 */
public class NavAdapter extends BaseAdapter{
  private final ArrayList<NavBaseItem> items;
  private final ArrayList<Class<? extends NavBaseItem>> viewTypes;
  private final Context context;

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
    if (NavItemFragment.class.isInstance(item)) {
      return getNavItemView((NavItemFragment)item, convertView, parent);
    }
    return null;
  }

  private View getNavItemView(NavItemFragment item, View convertView, ViewGroup parent) {
    SimpleNavItemHolder holder = null;

    if (convertView == null) {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      convertView             = inflater.inflate(R.layout.nav_item_simple, parent, false);

      holder           = new SimpleNavItemHolder();
      holder.iconView  = (FontIconTextView) convertView.findViewById(R.id.nav_item_icon_view);
      holder.titleView = (TextView) convertView.findViewById(R.id.nav_item_title_view);
      convertView.setTag(holder);
    } else {
      holder = (SimpleNavItemHolder) convertView.getTag();
    }

    holder.titleView.setText(item.getTitle());
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

  public class SimpleNavItemHolder {
    public FontIconTextView iconView;
    public TextView         titleView;
  }
}
