package com.biboop.android.auth;

public class TokenException extends Exception {
    public final TokenResult result;

    public TokenException(TokenResult result) {
        this.result = result;
    }
}
