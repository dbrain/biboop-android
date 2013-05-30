package com.biboop.android.service;

import android.content.Context;
import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.biboop.android.auth.TokenHelper;
import com.biboop.android.auth.TokenResult;
import com.biboop.android.util.JSONMapper;
import com.biboop.android.util.Preferences;

import java.util.HashMap;
import java.util.Map;

public class BiboopRequest<T> extends Request<T> {
    private static final String TAG = "Biboop.BiboopRequest";

    protected final TokenHelper tokenHelper;
    protected final Preferences prefs;
    protected final Context context;
    protected final Response.Listener<T> listener;
    protected final Class<T> typeClass;

    public BiboopRequest(Class<T> typeClass, Context ctx, int method, String url, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.typeClass = typeClass;
        this.context = ctx.getApplicationContext();
        this.prefs = Preferences.getPreferences(context);
        this.tokenHelper = new TokenHelper(context);
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        final HashMap<String, String> headers = new HashMap<String, String>(1);
        TokenResult tokenResult = tokenHelper.getToken();
        switch (tokenResult.action) {
            case OK:
                headers.put("Authorization", "Bearer " + tokenResult.token);
                break;
            case RETRY_WITH_INTENT:
                throw new AuthFailureError(tokenResult.retryIntent);
            case AUTHENTICATION_FAILURE:
                throw new AuthFailureError("Authentication failure", tokenResult.authFailureException);
            case SERVER_ERROR:
                throw new AuthFailureError("Authentication server error", tokenResult.serverException);
            case RETRY_LIMIT_HIT:
                throw new AuthFailureError("Ran out of retries", tokenResult.retryLimitHitAuthException);
        }
        return headers;
    }

    @Override
    protected void deliverResponse(T t) {
        this.listener.onResponse(t);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            T body = JSONMapper.getInstance().readValue(response.data, typeClass);
            return Response.success(body, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }
}
