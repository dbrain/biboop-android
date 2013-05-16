package com.biboop.android.auth;

import android.content.Intent;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

public final class TokenResult {
    public static enum GetTokenResultAction {
        OK,
        RETRY_WITH_INTENT,
        RETRY_LIMIT_HIT,
        SERVER_ERROR,
        AUTHENTICATION_FAILURE
    }

    public final Intent retryIntent;
    public final UserRecoverableAuthException retryLimitHitAuthException;
    public final IOException serverException;
    public final GoogleAuthException authFailureException;
    public final GetTokenResultAction action;
    public final String token;

    public TokenResult(final String token) {
        this.action = GetTokenResultAction.OK;
        this.token = token;
        this.retryIntent = null;
        this.retryLimitHitAuthException = null;
        this.serverException = null;
        this.authFailureException = null;
    }

    public TokenResult(final Intent retryIntent) {
        this.action = GetTokenResultAction.RETRY_WITH_INTENT;
        this.retryIntent = retryIntent;
        this.retryLimitHitAuthException = null;
        this.serverException = null;
        this.authFailureException = null;
        this.token = null;
    }

    public TokenResult(final UserRecoverableAuthException retryLimitHitAuthException) {
        this.action = GetTokenResultAction.RETRY_LIMIT_HIT;
        this.retryIntent = null;
        this.retryLimitHitAuthException = retryLimitHitAuthException;
        this.serverException = null;
        this.authFailureException = null;
        this.token = null;
    }

    public TokenResult(final IOException serverException) {
        this.action = GetTokenResultAction.SERVER_ERROR;
        this.retryIntent = null;
        this.retryLimitHitAuthException = null;
        this.serverException = serverException;
        this.authFailureException = null;
        this.token = null;
    }

    public TokenResult(final GoogleAuthException authFailureException) {
        this.action = GetTokenResultAction.AUTHENTICATION_FAILURE;
        this.retryIntent = null;
        this.retryLimitHitAuthException = null;
        this.serverException = null;
        this.authFailureException = authFailureException;
        this.token = null;
    }


}
