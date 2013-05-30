package com.biboop.android.service.response;

import com.biboop.android.model.Server;

import java.util.List;

public class ServersResponse {
    public List<Server> servers;

    @Override
    public String toString() {
        return "ServersResponse{" +
            "servers=" + servers +
            '}';
    }
}
