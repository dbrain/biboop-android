package com.biboop.android.model;

public class GoogleUser {
    public String sub;
    public String name;
    public String givenName;
    public String familyName;
    public String profile;
    public String picture;
    public String email;
    public boolean emailVerified;
    public String gender;
    public String birthdate;
    public String locale;

    @Override
    public String toString() {
        return "GoogleUser{" +
            "sub='" + sub + '\'' +
            ", name='" + name + '\'' +
            ", givenName='" + givenName + '\'' +
            ", familyName='" + familyName + '\'' +
            ", profile='" + profile + '\'' +
            ", picture='" + picture + '\'' +
            ", email='" + email + '\'' +
            ", emailVerified=" + emailVerified +
            ", gender='" + gender + '\'' +
            ", birthdate='" + birthdate + '\'' +
            ", locale='" + locale + '\'' +
            '}';
    }
}
