package com.biboop.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.biboop.android.R;
import com.biboop.android.util.Preferences;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MenuAdapter extends BaseAdapter {
    public static final int MENU_ITEM_TYPE_USER = 0;
    public static final int MENU_ITEM_TYPE_NORMAL = 1;
    public static final MenuItem[] menuItems = MenuItem.values();
    private Context context;
    private Preferences prefs;

    public MenuAdapter(Context context) {
        this.context = context;
        this.prefs = Preferences.getPreferences(context);
    }

    protected static View newView(Context context, int viewId) {
        final View view;
        switch (viewId) {
            case R.layout.menu_user:
                UserViewHolder userViewHolder = new UserViewHolder();
                view = View.inflate(context, viewId, null);
                userViewHolder.avatar = (ImageView) view.findViewById(R.id.avatar);
                userViewHolder.name = (TextView) view.findViewById(R.id.user_full_name);
                view.setTag(userViewHolder);
                return view;
            case R.layout.menu_normal:
                NormalViewHolder viewHolder = new NormalViewHolder();
                view = View.inflate(context, viewId, null);
                viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
                viewHolder.title = (TextView) view.findViewById(R.id.title);
                view.setTag(viewHolder);
                return view;
        }
        return null;
    }

    protected static void bindNormalView(Context context, View view, MenuItem menuItem) {
        NormalViewHolder holder = (NormalViewHolder) view.getTag();
        holder.icon.setImageResource(menuItem.image);
        holder.title.setText(menuItem.title);
    }

    @Override
    public int getCount() {
        return menuItems.length;
    }

    @Override
    public MenuItem getItem(int i) {
        return menuItems[i];
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).ordinal();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MenuItem item = getItem(i);
        int viewId;
        switch (item.menuType) {
            case MENU_ITEM_TYPE_USER:
                viewId = R.layout.menu_user;
                break;
            default:
                viewId = R.layout.menu_normal;
                break;
        }

        if (view == null || view.getId() != viewId) {
            view = newView(context, viewId);
        }
        bindView(context, view, viewId, item);
        return view;
    }

    protected void bindView(Context context, View view, int viewId, MenuItem item) {
        switch (viewId) {
            case R.layout.menu_user:
                bindUserView(context, view);
                break;
            case R.layout.menu_normal:
                bindNormalView(context, view, item);
                break;
        }
    }

    protected void bindUserView(Context context, View view) {
        final UserViewHolder holder = (UserViewHolder) view.getTag();
        ImageLoader.getInstance().displayImage(prefs.getUserAvatarUrl(), holder.avatar);
        holder.name.setText(prefs.getUserFullName());
    }

    public static enum MenuItem {
        USER(MENU_ITEM_TYPE_USER, -1, R.string.account),
        DASHBOARD(MENU_ITEM_TYPE_NORMAL, R.drawable.ic_dashboard, R.string.dashboard),
        ALERTS(MENU_ITEM_TYPE_NORMAL, R.drawable.ic_alerts, R.string.alerts),
        SERVERS(MENU_ITEM_TYPE_NORMAL, R.drawable.ic_servers, R.string.servers),
        COMMANDS(MENU_ITEM_TYPE_NORMAL, R.drawable.ic_commands, R.string.commands),
        SIGN_OUT(MENU_ITEM_TYPE_NORMAL, R.drawable.ic_ragequit, R.string.sign_out);
        public int menuType;
        public int image;
        public int title;

        MenuItem(int menuType, int image, int title) {
            this.menuType = menuType;
            this.image = image;
            this.title = title;
        }
    }

    protected static class UserViewHolder {
        protected ImageView avatar;
        protected TextView name;
    }

    protected static class NormalViewHolder {
        protected ImageView icon;
        protected TextView title;
    }
}
