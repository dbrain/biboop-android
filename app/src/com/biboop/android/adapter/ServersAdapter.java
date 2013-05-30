package com.biboop.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.biboop.android.R;
import com.biboop.android.model.Server;
import com.biboop.android.util.Preferences;

import java.util.ArrayList;
import java.util.List;

public class ServersAdapter extends BaseAdapter {
    private final Object lock = new Object();
    private Context context;
    private Preferences prefs;
    private List<Server> servers;

    public ServersAdapter(Context context) {
        this.context = context;
        this.prefs = Preferences.getPreferences(context);
        this.servers = new ArrayList<Server>();
    }

    protected static View newView(Context context) {
        final View view = View.inflate(context, R.layout.server_item, null);
        ServerViewHolder holder = new ServerViewHolder();
        holder.serverState = view.findViewById(R.id.serverState);
        holder.serverName = (TextView) view.findViewById(R.id.serverName);
        holder.lastSeen = (TextView) view.findViewById(R.id.lastSeen);
        holder.serverDescription = (TextView) view.findViewById(R.id.serverDescription);
        view.setTag(holder);
        return view;
    }

    protected void bindView(Context context, View view, Server server) {
        ServerViewHolder holder = (ServerViewHolder) view.getTag();
        holder.serverName.setText(server.name);
        holder.lastSeen.setText(context.getString(R.string.last_seen, server.getHumanLastPollAge()));
        holder.serverDescription.setText(server.description);
        long lastPollAge = server.getLastPollAge();
        if (lastPollAge < prefs.getPollWarnAge()) {
            holder.serverState.setBackgroundResource(R.color.green_card_highlight);
        } else if (lastPollAge >= prefs.getPollErrorAge()) {
            holder.serverState.setBackgroundResource(R.color.red_card_highlight);
        } else {
            holder.serverState.setBackgroundResource(R.color.yellow_card_highlight);
        }
    }

    @Override
    public int getCount() {
        return servers.size();
    }

    @Override
    public Server getItem(int i) {
        return servers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Server server = getItem(i);
        if (view == null) {
            view = newView(context);
        }
        bindView(context, view, server);
        return view;
    }

    public void setServers(List<Server> servers) {
        synchronized (lock) {
            this.servers.clear();
            this.servers.addAll(servers);
        }
        notifyDataSetInvalidated();
    }

    protected static class ServerViewHolder {
        public View serverState;
        public TextView serverName;
        public TextView lastSeen;
        public TextView serverDescription;
    }
}
