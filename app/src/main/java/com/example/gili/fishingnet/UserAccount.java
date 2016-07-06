package com.example.gili.fishingnet;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by Gili on 06/07/2016.
 */
public class UserAccount {

    private static GoogleSignInAccount account = null;
    private static Boolean flag = false;

    private UserAccount(GoogleSignInAccount acct){
        account = acct;
    }

    public static void setUserAccount(GoogleSignInAccount acct) {
        if (!flag){
            new UserAccount(acct);
        }
    }

    public GoogleSignInAccount getUserAccount(){
        return account;
    }
}
