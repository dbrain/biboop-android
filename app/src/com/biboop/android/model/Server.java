package com.biboop.android.model;

import android.text.format.DateUtils;

public class Server {
    public long id;
    public long userId;
    public String serverId;
    public String name;
    public String description;
    public long lastPollTime;
    public int pendingCommands;

    @Override
    public String toString() {
        return "Server{" +
            "id=" + id +
            ", userId=" + userId +
            ", serverId='" + serverId + '\'' +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", lastPollTime=" + lastPollTime +
            ", pendingCommands=" + pendingCommands +
            '}';
    }

    public long getLastPollAge() {
        return System.currentTimeMillis() - lastPollTime * 1000;
    }

    public CharSequence getHumanLastPollAge() {
        return DateUtils.getRelativeTimeSpanString(lastPollTime * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }
}
