package com.biboop.android.activity;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.biboop.android.R;
import com.biboop.android.service.BiboopRequest;
import com.biboop.android.service.RequestFactory;
import com.biboop.android.service.response.MeResponse;
import com.biboop.android.util.DefaultRequestFilters;
import com.biboop.android.util.Preferences;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;

import static com.google.android.gms.common.GooglePlayServicesUtil.*;

public class Startup extends SherlockFragmentActivity implements View.OnClickListener {
    private static final String TAG = "Biboop.Startup";

    private static final int SWITCHER_LOADING_POSITION = 1;
    private static final int SWITCHER_STARTING_POSITION = 0;

    private static final int ACCOUNT_PICKER_REQ_CODE = 1;
    private static final int ACCOUNT_RETRY_REQ_CODE = 2;

    private Preferences prefs;
    private RequestQueue requestQueue;
    private BiboopRequest<MeResponse> meRequest;
    private ViewSwitcher loadingSwitcher;

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(DefaultRequestFilters.ALL_REQUEST_FILTER);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = Preferences.getPreferences(this);
        if (prefs.isSignedIn()) {
            startDashboard();
        } else {
            requestQueue = Volley.newRequestQueue(this);
            MeRequestListener requestListener = new MeRequestListener();
            meRequest = RequestFactory.newMeRequest(this, requestListener, requestListener);
            setContentView(R.layout.login);

            loadingSwitcher = (ViewSwitcher) findViewById(R.id.main_loading_switcher);

            SignInButton button = (SignInButton) findViewById(R.id.sign_in_button);
            button.setStyle(SignInButton.SIZE_WIDE, SignInButton.COLOR_DARK);
            button.setOnClickListener(this);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getSupportActionBar().setTitle(R.string.sign_in);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int playServicesAvailable = isGooglePlayServicesAvailable(getApplicationContext());
        if (playServicesAvailable != ConnectionResult.SUCCESS) {
            final Dialog errorDialog = getErrorDialog(playServicesAvailable, this, 0);
            if (errorDialog != null) {
                errorDialog.show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                String[] allowableAccounts = new String[] {GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE};
                Intent pickerIntent = AccountPicker.newChooseAccountIntent(null, null, allowableAccounts, true, null, null, null, null);
                startActivityForResult(pickerIntent, ACCOUNT_PICKER_REQ_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACCOUNT_PICKER_REQ_CODE:
                if (resultCode == RESULT_OK) {
                    startLoading();
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    Preferences.getPreferences(this).setAccountName(accountName);
                    attemptLogin();
                }
                break;
            case ACCOUNT_RETRY_REQ_CODE:
                if (resultCode == RESULT_OK) {
                    attemptLogin();
                } else {
                    stopLoading();
                }
                break;
        }
    }

    protected void startLoading() {
        if (loadingSwitcher.getDisplayedChild() != SWITCHER_LOADING_POSITION) {
            loadingSwitcher.setDisplayedChild(SWITCHER_LOADING_POSITION);
        }
    }

    protected void stopLoading() {
        if (loadingSwitcher.getDisplayedChild() != SWITCHER_STARTING_POSITION) {
            loadingSwitcher.setDisplayedChild(SWITCHER_STARTING_POSITION);
        }
    }

    protected void startDashboard() {
        startActivity(new Intent(this, Main.class));
        finish();
        overridePendingTransition(0, 0);
    }

    protected void attemptLogin() {
        requestQueue.add(meRequest);
    }

    class MeRequestListener implements Response.ErrorListener, Response.Listener<MeResponse> {

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if (volleyError instanceof AuthFailureError) {
                AuthFailureError authError = (AuthFailureError)volleyError;
                if (authError.getResolutionIntent() != null) {
                    startActivityForResult(authError.getResolutionIntent(), ACCOUNT_RETRY_REQ_CODE);
                    return;
                }
            }

            Log.e(TAG, "Something broke getting /api/me", volleyError);
            Toast.makeText(Startup.this, R.string.server_error, Toast.LENGTH_SHORT).show();
            stopLoading();
        }

        @Override
        public void onResponse(MeResponse meResponse) {
            Log.d(TAG, "meResponse was " + meResponse);
            Toast.makeText(Startup.this, getString(R.string.signed_in, meResponse.googleUser.email), Toast.LENGTH_SHORT).show();
            prefs.setSignedIn(true);
            prefs.setGoogleUser(meResponse.googleUser);
            startDashboard();
        }

    }
}
