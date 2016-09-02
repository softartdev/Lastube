package com.softartdev.lastube.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.softartdev.lastube.LastfmAPI;
import com.softartdev.lastube.R;
import com.softartdev.lastube.SessionManager;
import com.softartdev.lastube.ui.MainActivity;

import de.umass.lastfm.Authenticator;
import de.umass.lastfm.Session;

public class LoginActivity extends Activity {

    // Email, password EditText
    EditText txtUsername, txtPassword;
    // login button
    Button btnLogin;
    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    // Session Manager Class
    SessionManager session;
    // Username, password strings
    String username, password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //make hyperlinks clickable
        TextView linkLostPass = (TextView) findViewById(R.id.linkLostPass);
        linkLostPass.setMovementMethod(LinkMovementMethod.getInstance());
        TextView linkJoin = (TextView) findViewById(R.id.linkJoin);
        linkJoin.setMovementMethod(LinkMovementMethod.getInstance());
        // Session Manager
        session = new SessionManager(getApplicationContext());
        // Email, Password input text
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

        // Login button
        btnLogin = (Button) findViewById(R.id.btnLogin);

        // Login button click event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get username, password from EditText
                username = txtUsername.getText().toString();
                password = txtPassword.getText().toString();

                // Check if username, password is filled
                if(username.trim().length() > 0 && password.trim().length() > 0){
                    SessionTask sessionTask = new SessionTask();
                    sessionTask.execute();
                }else{
                    // user didn't entered username or password
                    // Show alert asking him to enter the details
                    alert.showAlertDialog(LoginActivity.this, "Login failed..", "Please enter username and password", false);
                }

            }
        });
    }

    private class SessionTask extends AsyncTask<Void, Void, Void> {
        Session sessionScrobble;
        String sessionString;
        String key;
        String secret;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            key = LastfmAPI.key;
            secret = LastfmAPI.secret;
        }

        @Override
        protected Void doInBackground(Void... params){
            if (Authenticator.getMobileSession(username, password, key, secret) != null){
                sessionScrobble = Authenticator.getMobileSession(username, password, key, secret);
            } else {
                sessionString = "session not available, but you typed login: " + username + ", & pass: " + password;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (sessionScrobble != null){
                Toast.makeText(LoginActivity.this, sessionScrobble.toString(), Toast.LENGTH_SHORT).show();
                // Creating user login session
                session.createLoginSession(username, password);
                // Staring MainActivity
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, sessionString, Toast.LENGTH_SHORT).show();
                // username / password doesn't match
                alert.showAlertDialog(LoginActivity.this, "Login failed..", "Username/Password is incorrect", false);
            }
        }
    }
}
