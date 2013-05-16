package com.biboop.android.util;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

public class DefaultRequestFilters {
    public static final RequestQueue.RequestFilter ALL_REQUEST_FILTER = new RequestQueue.RequestFilter() {
        @Override
        public boolean apply(Request<?> request) {
            return true;
        }
    };
}
