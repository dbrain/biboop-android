package com.biboop.android.model;

public class BiboopUser {
    public String email;
    public String serverKey;

    @Override
    public String toString() {
        return "BiboopUser{" +
            "email='" + email + '\'' +
            ", serverKey='" + serverKey + '\'' +
            '}';
    }
}
