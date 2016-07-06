package com.example.gili.fishingnet;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Gili on 06/07/2016.
 */
public class UserAccount {

    private static GoogleSignInAccount account = null;
    private static GoogleApiClient apiClient = null;

    public UserAccount(){}

    public static void setUserAccount(GoogleSignInAccount acct) {
        account = acct;
    }

    public static GoogleSignInAccount getUserAccount(){
        return account;
    }

    public static void setGoogleApiClient(GoogleApiClient googleApiClient) {
        apiClient = googleApiClient;
    }

    public static GoogleApiClient getGoogleApiClient(){
        return apiClient;
    }
}
