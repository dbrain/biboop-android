package com.biboop.android.util;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public final class RequestQueueHolder {
    private static RequestQueue instance;

    public static RequestQueue getInstance(Context ctx) {
        if (instance == null) {
            instance = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return instance;
    }

}
