package com.hhb.hardyhardybank;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by cell on 11/15/2014.
 */
public class CloseActivity extends Activity{


    String toDelete;
    private EditText mAccountNumberView;
    private int accountNumber;
    private int input_AccountNumber;
    private String accountBalance;
    private String accountRole;
    private String accountUser;
    private Spinner mAccounts;
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
                    Intent i = new Intent(CloseActivity.this, AdminActivity.class);

                    // passes along the bundle
                    i.putExtras(bundle);

                    startActivity(i);

                    // close this activity
                    finish();
                }

                // for Customer
                else if (accountRole.equals("customer")) {
                    // go to AddAccountActivity
                    Intent i = new Intent(CloseActivity.this, MainActivityAccount.class);

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
                //int accountValue = Integer.parseInt(input_AccountNumber);

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
                                        Intent i = new Intent(CloseActivity.this, MainActivityAdmin.class);

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
                                        Intent i = new Intent(CloseActivity.this, MainActivityCustomer.class);

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
 
 
    /* IMPLEMENTATION That was in MainActivityUser
    // String toDelete;
   // private ParseObject cAccountInfo;
     // Button to Close Account
            Button closeUserAccount = (Button) findViewById(R.id.action_close);
            closeUserAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(MainActivityUser.this, CloseActivity.class);
                //startActivity(i);
 
 
                try {
                    attemptCloseAccount();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
 
            }
        });
 
    }
 
       public void attemptCloseAccount() throws ParseException {
        ParseObject currentUser = ParseUser.getCurrentUser();
       // String accountID;
       // accountID = currentUser.getObjectId();
           ParseQuery<ParseObject> queryCurrentUser = ParseQuery.getQuery("Account");
          // queryCurrentUser.whereEqualTo("userID", currentUser.getString("username"));
           queryCurrentUser.whereEqualTo("objectId", mAccounts.getSelectedItem());
 
                queryCurrentUser.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject cAccountInfo, com.parse.ParseException e) {
                        if (cAccountInfo == null) {
                            Toast.makeText(getApplicationContext(), "Could not find Account!", Toast.LENGTH_LONG).show();
                        } else {
 
                            //cUserBalance = cAccountInfo.getDouble("balance");
                            toDelete = cAccountInfo.getObjectId();
                            try {
                                ParseObject.createWithoutData("Account", toDelete).delete();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
         Toast.makeText(getApplicationContext(), "Account closed. The remaining balance will be mailed to your address",
                 Toast.LENGTH_LONG).show();
        //currentUser.createWithoutData("Account", accountID).delete();
 
    }
 
     */
} //End of CloseActivity class