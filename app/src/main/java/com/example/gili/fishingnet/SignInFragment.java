package com.example.gili.fishingnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by Gili on 06/07/2016.
 */
public class SignInFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    SignInButton signInButton;
    Button signOutButton;
    TextView statusTextView;
    GoogleApiClient googleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout signInLayout = (RelativeLayout) inflater.inflate(R.layout.sign_in, container, false);

        statusTextView = (TextView) signInLayout.findViewById(R.id.status_textview);
        signInButton = (SignInButton) signInLayout.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        signOutButton = (Button) signInLayout.findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(this);

        googleApiClient = UserAccount.getGoogleApiClient();
        GoogleSignInAccount lastUser = UserAccount.getUserAccount();
        if(lastUser!=null){
            statusTextView.setText("Hey "+lastUser.getDisplayName());
        }

        return signInLayout;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.signOutButton:
                signOut();
                break;
        }
    }

    private void signIn(){
        if(googleApiClient == null){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity()/*fregment activity*/, this/**/)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
            UserAccount.setGoogleApiClient(googleApiClient);
        }
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            UserAccount.setUserAccount(acct);
            statusTextView.setText("Hey "+acct.getDisplayName());
        }
        else{
            statusTextView.setText("no");
        }
    }

    private void signOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                UserAccount.setUserAccount(null);
                statusTextView.setText("bye bye");
            }
        });
    }
}
