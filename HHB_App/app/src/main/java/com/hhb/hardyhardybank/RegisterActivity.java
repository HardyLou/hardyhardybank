package com.hhb.hardyhardybank;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.SignUpCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * A register screen for users to create an account
 */
public class RegisterActivity extends Activity {
    // UI references
    private EditText mNameView;
    private EditText mAddressView;
    private EditText mEmailView;
    private EditText mUsernameView;
    private EditText mPasswordView;
    private EditText mNumberView;
    private Spinner mAccount;
    private View mProgressView;
    private View mRegisterFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set up the register form
        mNameView = (EditText) findViewById(R.id.name);
        mAddressView = (EditText) findViewById(R.id.address);
        mEmailView = (EditText) findViewById(R.id.email);
        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mNumberView = (EditText) findViewById(R.id.number);
        mAccount = (Spinner) findViewById(R.id.account_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.accounts_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAccount.setAdapter(adapter);


        // Activity once Register button is pressed
        Button mUserSignInButton = (Button) findViewById(R.id.user_sign_in_button);
        mUserSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        // Sends user to LoginActivity Screen
        TextView loginButton = (TextView) findViewById(R.id.user_login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        mRegisterFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptRegister()
    {
        // Reset errors
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mNameView.setError(null);
        mAddressView.setError(null);
        mEmailView.setError(null);
        mNumberView.setError(null);

        // Store values entered at the time of the login attempt
        String input_username = mUsernameView.getText().toString();
        String input_password = mPasswordView.getText().toString();
        String input_Name =  mNameView.getText().toString();
        String input_Address =  mAddressView.getText().toString();
        String input_Email =  mEmailView.getText().toString();
        String input_Number =  mNumberView.getText().toString();
        String input_AccountType = mAccount.getSelectedItem().toString();

        boolean cancel = false;
        View focusView = null;

        // Check if username has been entered
        if (TextUtils.isEmpty(input_username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }
        // Check if password has been entered
        if (TextUtils.isEmpty(input_password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check if name has been entered
        if (TextUtils.isEmpty(input_Name))
        {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }
        // Check if address has been entered
        if (TextUtils.isEmpty(input_Address))
        {
            mAddressView.setError(getString(R.string.error_field_required));
            focusView = mAddressView;
            cancel = true;
        }
        // Check if email has been entered
        if (TextUtils.isEmpty(input_Email))
        {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }
        // Check if account number was entered
        if (TextUtils.isEmpty(input_Number))
        {
            mNumberView.setError(getString(R.string.error_field_required));
            focusView = mNumberView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Enter values into Parse database
            ParseUser user = new ParseUser();
            user.setUsername(input_username);
            user.setPassword(input_password);
            user.setEmail(input_Email);
            user.put("fullname", input_Name);
            user.put("address", input_Address);
            user.put("accountnumber", Integer.valueOf(input_Number));
            user.put("accounttype", input_AccountType);
            user.put("balance", 0.0);

            // Check whether registration has succeeded or not
            user.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        // Show a progress spinner, and kick off a background task to
                        // perform the user registration attempt
                        showProgress(true);

                        // Successful Registration, return to LoginActivity
                        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(i);

                        // Notify user registration has been successful
                        Toast.makeText(getApplicationContext(), "Account has been registered.",
                                Toast.LENGTH_LONG).show();

                        // Close this activity
                        finish();
                    } else {
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                        Toast.makeText(getApplicationContext(), "Registration has failed.",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}