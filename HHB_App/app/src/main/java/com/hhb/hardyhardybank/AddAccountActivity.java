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
        import com.parse.ParseObject;
        import com.parse.SignUpCallback;
        import com.parse.Parse;
        import com.parse.ParseException;
        import com.parse.ParseUser;

        import java.text.DecimalFormat;

/**
 * A register screen for users to create an account
 */
public class AddAccountActivity extends Activity {
    // UI references
    private TextView mNameView;
    private TextView mAddressView;
    private TextView mEmailView;
    private TextView mUsernameView;
    private EditText mNumberView;
    private Spinner mAccount;
    private String UserName;
    private String UserAddress;
    private String UserEmail;
    private String UserUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addaccount);

        // Set up the add account form
        mNameView = (TextView) findViewById(R.id.name);
        mAddressView = (TextView) findViewById(R.id.address);
        mEmailView = (TextView) findViewById(R.id.email);
        mUsernameView = (TextView) findViewById(R.id.username);
        mNumberView = (EditText) findViewById(R.id.number);
        mAccount = (Spinner) findViewById(R.id.account_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.accounts_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAccount.setAdapter(adapter);

        // Query Parse for account info
        ParseObject currentUser = ParseUser.getCurrentUser();
        UserName = currentUser.getString("fullname");
        UserAddress = currentUser.getString("address");
        UserEmail = currentUser.getString("email");
        UserUserName = currentUser.getString("username");

        // Format displayed balance
//        DecimalFormat format = new DecimalFormat("#0.00");
//        final String formatted_balance = format.format(balance);
        mNameView.setText(UserName);
        mAddressView.setText(UserAddress);
        mEmailView.setText(UserEmail);
        mUsernameView.setText(UserUserName);


        // Activity once Add Account button is pressed
        Button mUserAddAcctButton = (Button) findViewById(R.id.user_add_acct_button);
        mUserAddAcctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptAdd();
            }
        });

        // Sends user to MainActivityUser Screen
        TextView loginButton = (TextView) findViewById(R.id.user_cancel_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(AddAccountActivity.this, MainActivityUser.class);
//                startActivity(i);

                // Close this activity
                finish();
            }
        });
    }

    /**
     * Attempts to add the account specified by the account add form.
     */
    public void attemptAdd()
    {

        String UserUserName;
        String UserEmail;

        // Query Parse for account info
        ParseObject currentUser = ParseUser.getCurrentUser();
        UserUserName = currentUser.getString("username");
        UserEmail = currentUser.getString("email");


        // Reset errors
        mNumberView.setError(null);

        // Store values entered at the time of the login attempt
        String input_Number =  mNumberView.getText().toString();
        String input_AccountType = mAccount.getSelectedItem().toString();

        boolean cancel = false;
        View focusView = null;

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
            ParseObject account = new ParseObject("Account");               // create new Account object
            account.put("userID", UserUserName);                            // joins User table with Account table
            account.put("userEmail", UserEmail);                            // alternate way to join tables
            account.put("accountnumber", Integer.valueOf(input_Number));    // set account number
            account.put("accountType", input_AccountType);                  // specifies whether it is a checking or savings account
            account.put("balance", 0.0);                                    // initialize balance to $0
            account.saveEventually();



            // Successful Registration, return to LoginActivity
            Intent i = new Intent(AddAccountActivity.this, MainActivityCustomer.class);
            startActivity(i);

            // Notify user registration has been successful
            Toast.makeText(getApplicationContext(), "Account has been added.",
                Toast.LENGTH_LONG).show();

            // Close this activity
            finish();

        }

    }

    /**
     * Shows the progress UI and hides the login form.

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
    }*/
}