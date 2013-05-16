package com.biboop.android.auth;

import android.content.Context;
import android.util.Log;
import com.biboop.android.util.Preferences;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

public class TokenHelper {
    private static final String TAG = "Biboop.TokenHelper";

    private static final String USER_INFO_SCOPE = "https://www.googleapis.com/auth/userinfo.profile";
    private static final String USER_EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
    private static final String SCOPES = "oauth2:" + USER_INFO_SCOPE + " " + USER_EMAIL_SCOPE;

    private static final int RETRY_DELAY = 1500;
    private static final int MAX_NETWORK_RETRIES = 6;
    private static final int MAX_ATTEMPTS = 2;

    private int networkRetries;
    private int attempts;

    private final Context ctx;
    private final Preferences prefs;

    public TokenHelper(Context ctx) {
        this.ctx = ctx;
        this.prefs = Preferences.getPreferences(ctx);
    }

    public TokenResult refreshToken(String token) {
        GoogleAuthUtil.invalidateToken(ctx, token);
        return getToken();
    }

    public TokenResult getToken() {
        TokenResult getTokenResult;
        try {
            getTokenResult = new TokenResult(GoogleAuthUtil.getToken(ctx, prefs.getAccountName(), SCOPES));
        } catch (UserRecoverableAuthException userAuthEx) {
            if (++attempts < MAX_ATTEMPTS) {
                getTokenResult = new TokenResult(userAuthEx.getIntent());
            } else {
                getTokenResult = new TokenResult(userAuthEx);
            }
        } catch (IOException ioEx) {
            if (++networkRetries < MAX_NETWORK_RETRIES) {
                try {
                    Log.d(TAG, String.format("tokenHelper network failure, retrying in %d", RETRY_DELAY));
                    Thread.sleep(RETRY_DELAY);
                } catch (InterruptedException ex) {
                    Log.d(TAG, "tokenHelper sleep interrupted");
                }
                getTokenResult = getToken();
            } else {
                getTokenResult = new TokenResult(ioEx);
            }
        } catch (GoogleAuthException fatalAuthEx) {
            getTokenResult = new TokenResult(fatalAuthEx);
        }

        return getTokenResult;
    }
}
