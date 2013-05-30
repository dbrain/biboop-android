package com.biboop.android.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import com.biboop.android.model.GoogleUser;

public class Preferences {
    private static final String DEFAULT_API_HOST = "https://biboop-web.appspot.com";
    private static final long DEFAULT_POLL_AGE_ERROR = 600000;
    private static final long DEFAULT_POLL_AGE_WARN = 300000;

    private static final String KEY_API_HOST = "apiHost";
    private static final String KEY_ACCOUNT_NAME = "accountName";
    private static final String KEY_SIGNED_IN = "signedIn";
    private static final String KEY_GOOGLE_USER_NAME = "fullName";
    private static final String KEY_GOOGLE_USER_AVATAR = "avatar";
    private static final String KEY_GOOGLE_USER_EMAIL = "email";
    private static final String KEY_POLL_AGE_ERROR = "pollAgeError";
    private static final String KEY_POLL_AGE_WARN = "pollAgeWarn";

    private final SharedPreferences prefs;
    private static Preferences instance;

    private Preferences(final SharedPreferences prefs) {
        this.prefs = prefs;
    }

    public static Preferences getPreferences(Context context) {
        if (instance == null) {
            final Context applicationContext = context.getApplicationContext();
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
            instance = new Preferences(prefs);
        }
        return instance;
    }

    public String getAccountName() {
        return prefs.getString(KEY_ACCOUNT_NAME, null);
    }

    public void setAccountName(String accountName) {
        save(prefs.edit().putString(KEY_ACCOUNT_NAME, accountName));
    }

    public boolean isSignedIn() {
        return prefs.getBoolean(KEY_SIGNED_IN, false);
    }

    public void setSignedIn(boolean signedIn) {
        save(prefs.edit().putBoolean(KEY_SIGNED_IN, signedIn));
    }

    public String getApiHost() {
        return prefs.getString(KEY_API_HOST, DEFAULT_API_HOST);
    }

    public long getPollErrorAge() {
        return prefs.getLong(KEY_POLL_AGE_ERROR, DEFAULT_POLL_AGE_ERROR);
    }

    public long getPollWarnAge() {
        return prefs.getLong(KEY_POLL_AGE_WARN, DEFAULT_POLL_AGE_WARN);
    }

    public void setApiHost(String apiHost) {
        save(prefs.edit().putString(KEY_API_HOST, apiHost));
    }

    public void setGoogleUser(GoogleUser googleUser) {
        if (googleUser != null) {
            final SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_GOOGLE_USER_NAME, googleUser.name);
            editor.putString(KEY_GOOGLE_USER_AVATAR, googleUser.picture);
            editor.putString(KEY_GOOGLE_USER_EMAIL, googleUser.email);
            save(editor);
        }
    }

    public String getUserFullName() {
        return prefs.getString(KEY_GOOGLE_USER_NAME, "Biboop User");
    }

    public String getUserAvatarUrl() {
        return prefs.getString(KEY_GOOGLE_USER_AVATAR, null);
    }

    public String getUserEmail() {
        return prefs.getString(KEY_GOOGLE_USER_EMAIL, null);
    }

    @SuppressLint("NewApi")
    private void save(SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }
}
