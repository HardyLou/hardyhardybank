package com.hhb.hardyhardybank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


/**
 * Activity to close user's bank accounts.
 */
public class CloseActivity extends Activity{


    String toDelete;
    private EditText mAccountNumberView;
    private int accountNumber;
    private int input_AccountNumber;
    private String accountBalance;
    private String accountRole;
    private String accountUser;
    Bundle bundle;
    View focusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_close);
        final ParseObject currentUser = ParseUser.getCurrentUser();

        // opens bundle
        bundle = getIntent().getExtras();

        // edit text field
        mAccountNumberView = (EditText) findViewById(R.id.account_number_close);
        accountRole = currentUser.getString("role");

        // Activity once back button is pressed
        Button mReturnButton = (Button) findViewById(R.id.action_return);
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // for Admin
                if (accountRole.equals("admin")) {
                    // go to AddAccountActivity
                    Intent i = new Intent(CloseActivity.this, AccountAdminActivity.class);

                    // passes along the bundle
                    i.putExtras(bundle);

                    startActivity(i);

                    // close this activity
                    finish();
                }

                // for Customer
                else if (accountRole.equals("customer")) {
                    // go to AddAccountActivity
                    Intent i = new Intent(CloseActivity.this, AccountUserActivity.class);

                    // passes along the bundle
                    i.putExtras(bundle);

                    startActivity(i);

                    // close this activity
                    finish();
                }
            }

        });


        // Activity once confirm button is pressed
        Button mCloseButton = (Button) findViewById(R.id.action_confirm);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accountNumber = bundle.getInt("accountnumber");
                accountBalance = bundle.getString("accountBalance");

                final ParseObject currentUser = ParseUser.getCurrentUser();
                input_AccountNumber = Integer.parseInt(mAccountNumberView.getText().toString());

                if (input_AccountNumber == accountNumber) {
                    mAccountNumberView.setError(null);
                    focusView = null;

                    ParseQuery<ParseObject> queryCurrentUser = ParseQuery.getQuery("Account");
                    queryCurrentUser.whereEqualTo("accountnumber", input_AccountNumber);

                    queryCurrentUser.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject cAccountInfo, com.parse.ParseException e) {
                            if (cAccountInfo == null) {
                                Toast.makeText(getApplicationContext(), "Could not find Account!", Toast.LENGTH_LONG).show();
                            } else {
                                accountUser = cAccountInfo.getString("userID");
                                if (accountRole.equals("admin")) {
                                    toDelete = cAccountInfo.getObjectId();
                                    try {
                                        ParseObject.createWithoutData("Account", toDelete).delete();

                                        Toast.makeText(getApplicationContext(),
                                                "The remaining balance (" + accountBalance +
                                                        ") in this account will be mailed to " +
                                                        accountUser + "'s address ",
                                                Toast.LENGTH_LONG).show();

                                        // go to AddAccountActivity
                                        Intent i = new Intent(CloseActivity.this, MainAdminActivity.class);

                                        startActivity(i);

                                        // close this activity
                                        finish();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }

                                } else if (accountRole.equals("customer")) {
                                    toDelete = cAccountInfo.getObjectId();
                                    try {
                                        Toast.makeText(getApplicationContext(),
                                                       "The remaining balance in this account" +
                                                       " will be mailed to your address ",
                                                        Toast.LENGTH_LONG).show();

                                        ParseObject.createWithoutData("Account", toDelete).delete();

                                        // go to AddAccountActivity
                                        Intent i = new Intent(CloseActivity.this, MainUserActivity.class);

                                        startActivity(i);

                                        // close this activity
                                        finish();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }
                                } // end of if statement

                                else {
                                    Toast.makeText(getApplicationContext(), "Could not find Account!", Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                    });
                }

                else {
                    mAccountNumberView.setError("Account number does not match.");
                    focusView = mAccountNumberView;
                    focusView.requestFocus();
                }

            } // End of OnClickView
        }); // End of OnClickListener

    }// End of OnCreateBundle

} //End of CloseActivity class
