package com.hhb.hardyhardybank;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.parse.GetCallback;
        import com.parse.ParseObject;
        import com.parse.ParseQuery;
        import com.parse.Parse;
        import com.parse.ParseUser;

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
    private String input_Number;
    private String input_AccountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

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
        mNameView.setText("Full name: " + UserName);
        mAddressView.setText("Address: " + UserAddress);
        mEmailView.setText("Email: " + UserEmail);
        mUsernameView.setText("Username: " + UserUserName);


        // Activity once Add Account button is pressed
        Button mUserAddAcctButton = (Button) findViewById(R.id.user_add_acct_button);
        mUserAddAcctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptAdd();
            }
        });

        // Sends user to MainUserActivity Screen
        TextView loginButton = (TextView) findViewById(R.id.user_cancel_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddAccountActivity.this, MainUserActivity.class);
                startActivity(i);

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

        // Query Parse for account info
        ParseObject currentUser = ParseUser.getCurrentUser();
        UserUserName = currentUser.getString("username");
        UserEmail = currentUser.getString("email");


        // Reset errors
        mNumberView.setError(null);

        // Store values entered at the time of the login attempt
        input_Number =  mNumberView.getText().toString();
        input_AccountType = mAccount.getSelectedItem().toString();

        boolean cancel = false;
        View focusView = null;

        // Check if account number was entered
        if (TextUtils.isEmpty(input_Number))
        {
            mNumberView.setError(getString(R.string.error_field_required));
            focusView = mNumberView;
            cancel = true;
        }

        // Check if account number is 6 digits long
        else if (input_Number.length() != 6)
        {
            mNumberView.setError(getString(R.string.incorrect_number_format));
            focusView = mNumberView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Account");
            query.whereEqualTo("accountnumber", Integer.valueOf(input_Number));
            // Get the first account found for the user
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject cAccountInfo, com.parse.ParseException e) {


                    if (cAccountInfo == null) {


                        // Enter values into Parse database
                        ParseObject account = new ParseObject("Account");               // create new Account object
                        account.put("userID", UserUserName);                            // joins User table with Account table
                        account.put("userEmail", UserEmail);                            // alternate way to join tables
                        account.put("accountnumber", Integer.valueOf(input_Number));    // set account number
                        account.put("accountType", input_AccountType);                  // specifies whether it is a checking or savings account
                        account.put("balance", 0.0);                                    // initialize balance to $0
                        account.saveEventually();


                        // Successful Registration, return to LoginActivity
                        Intent i = new Intent(AddAccountActivity.this, MainUserActivity.class);
                        startActivity(i);

                        // Notify user registration has been successful
                        Toast.makeText(getApplicationContext(), "Account has been added.",
                                Toast.LENGTH_LONG).show();

                        // Close this activity
                        finish();
                    } else {

                        // Add account failed because of duplicate account number
                        mNumberView.setError(getString(R.string.duplicate_acct_number));
                        View focusView = mNumberView;
                        focusView.requestFocus();
                    }
                }
            });
        }

    }
}