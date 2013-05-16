package com.biboop.android.service.response;

import com.biboop.android.model.BiboopUser;
import com.biboop.android.model.GoogleUser;

public class MeResponse {
    public GoogleUser googleUser;
    public BiboopUser user;

    @Override
    public String toString() {
        return "MeResponse{" +
            "googleUser=" + googleUser +
            ", user=" + user +
            '}';
    }
}
