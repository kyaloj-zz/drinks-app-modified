package com.example.teeshirt.drinksapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class Login extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ageConfirm();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    public void ageConfirm(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setMessage("Are you at least 18 years old?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.setTitle("Age Confirmation");
        alert.show();
    }




    public static class PlaceholderFragment extends Fragment {
        private AccessTokenTracker myTokenTracker;
        private ProfileTracker myProfileTracker;
        private TextView textView;
        private CallbackManager cbm;
        private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                displayWelcome(profile);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        };

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
            cbm = CallbackManager.Factory.create();
            myTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken old, AccessToken newToken) {

                }
            };

            myProfileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                    displayWelcome(newProfile);
                }
            };
            myTokenTracker.startTracking();
            myProfileTracker.startTracking();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            LoginButton loginButton = (LoginButton)view.findViewById(R.id.login_button);
            loginButton.setReadPermissions("email");
            loginButton.setFragment(this);
            loginButton.registerCallback(cbm,mCallback);
            textView= (TextView)view.findViewById(R.id.text_details);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            cbm.onActivityResult(requestCode,resultCode,data);
        }

        private void displayWelcome(Profile profile){

            if (profile != null){
                textView.setText("Welcome "+ profile.getName());
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            Profile profile = Profile.getCurrentProfile();
            displayWelcome(profile);
        }

        @Override
        public void onStop() {
            super.onStop();
            myTokenTracker.stopTracking();
            myProfileTracker.stopTracking();
        }
    }
}
