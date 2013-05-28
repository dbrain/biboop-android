package com.biboop.android.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import com.biboop.android.model.GoogleUser;

public class Preferences {
    private static final String DEFAULT_API_HOST = "http://192.168.43.248:8080";

    private static final String KEY_API_HOST = "apiHost";
    private static final String KEY_ACCOUNT_NAME = "accountName";
    private static final String KEY_SIGNED_IN = "signedIn";
    private static final String GOOGLE_USER_NAME = "fullName";
    private static final String GOOGLE_USER_AVATAR = "avatar";
    private static final String GOOGLE_USER_EMAIL = "email";

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

    public void setApiHost(String apiHost) {
        save(prefs.edit().putString(KEY_API_HOST, apiHost));
    }

    public void setGoogleUser(GoogleUser googleUser) {
        if (googleUser != null) {
            final SharedPreferences.Editor editor = prefs.edit();
            editor.putString(GOOGLE_USER_NAME, googleUser.name);
            editor.putString(GOOGLE_USER_AVATAR, googleUser.picture);
            editor.putString(GOOGLE_USER_EMAIL, googleUser.email);
            save(editor);
        }
    }

    public String getUserFullName() {
        return prefs.getString(GOOGLE_USER_NAME, "Biboop User");
    }

    public String getUserAvatarUrl() {
        return prefs.getString(GOOGLE_USER_AVATAR, null);
    }

    public String getUserEmail() {
        return prefs.getString(GOOGLE_USER_EMAIL, null);
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
