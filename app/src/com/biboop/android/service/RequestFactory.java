package com.biboop.android.service;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;
import com.biboop.android.service.response.MeResponse;
import com.biboop.android.util.Preferences;

public final class RequestFactory {

    public static BiboopRequest<MeResponse> newMeRequest(Context ctx, Response.Listener<MeResponse> listener, Response.ErrorListener errorListener) {
        final Context context = ctx.getApplicationContext();
        return new BiboopRequest<MeResponse>(
            MeResponse.class,
            context,
            Request.Method.GET,
            Preferences.getPreferences(context).getApiHost() + "/api/me",
            listener,
            errorListener
        );
    }

}
