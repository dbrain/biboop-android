package com.biboop.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import com.actionbarsherlock.app.SherlockFragment;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.biboop.android.R;
import com.biboop.android.adapter.ServersAdapter;
import com.biboop.android.service.BiboopRequest;
import com.biboop.android.service.RequestFactory;
import com.biboop.android.service.response.ServersResponse;
import com.biboop.android.util.RequestQueueHolder;

public class Servers extends SherlockFragment {
    private static final String SERVERS_REQUEST_TAG = "servers";
    private static final int ACCOUNT_RETRY_REQ_CODE = 1;
    private static final int SWITCHER_LOADING_POSITION = 0;
    private static final int SWITCHER_SERVERS_POSITION = 1;

    private static final String TAG = "ServersFragment";
    private RequestQueue requestQueue;
    private BiboopRequest<ServersResponse> serversRequest;
    private ViewSwitcher loadingSwitcher;
    private ServersAdapter serversAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestQueue = RequestQueueHolder.getInstance(getSherlockActivity());
        ServersRequestListener requestListener = new ServersRequestListener();
        serversRequest= RequestFactory.newServersRequest(getSherlockActivity(), requestListener, requestListener);
        serversRequest.setTag(SERVERS_REQUEST_TAG);
        requestQueue.add(serversRequest);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(SERVERS_REQUEST_TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        loadingSwitcher = (ViewSwitcher)inflater.inflate(R.layout.servers, container, false);
        serversAdapter = new ServersAdapter(getSherlockActivity());
        ((ListView) loadingSwitcher.findViewById(R.id.server_list)).setAdapter(serversAdapter);

        return loadingSwitcher;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACCOUNT_RETRY_REQ_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    retryServersRequest();
                } else {
                    stopLoading();
                }
                break;
        }
    }

    private void retryServersRequest() {
        requestQueue.add(serversRequest);
    }

    protected void startLoading() {
        if (loadingSwitcher.getDisplayedChild() != SWITCHER_LOADING_POSITION) {
            loadingSwitcher.setDisplayedChild(SWITCHER_LOADING_POSITION);
        }
    }

    protected void stopLoading() {
        if (loadingSwitcher.getDisplayedChild() != SWITCHER_SERVERS_POSITION) {
            loadingSwitcher.setDisplayedChild(SWITCHER_SERVERS_POSITION);
        }
    }

    private class ServersRequestListener implements Response.ErrorListener, Response.Listener<ServersResponse> {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            if (volleyError instanceof AuthFailureError) {
                AuthFailureError authError = (AuthFailureError)volleyError;
                if (authError.getResolutionIntent() != null) {
                    startActivityForResult(authError.getResolutionIntent(), ACCOUNT_RETRY_REQ_CODE);
                    return;
                }
            }

            Log.e(TAG, "Something broke getting /api/servers", volleyError);
            Toast.makeText(getSherlockActivity(), R.string.server_error, Toast.LENGTH_SHORT).show();
            stopLoading();
        }

        @Override
        public void onResponse(ServersResponse serversResponse) {
            Log.d(TAG, "Got response" + serversResponse);
            serversAdapter.setServers(serversResponse.servers);
            stopLoading();
        }
    }
}
