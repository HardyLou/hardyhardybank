package com.hhb.hardyhardybank;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


/**
 *  Landing page from login; menu access to other functionality for Admins
 */
public class MainActivityTeller extends  ActionBarActivity{

    private EditText mAccountNumber;
    private String input_account;
    boolean cancel = false;
    View focusView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_teller);

        mAccountNumber = (EditText) findViewById(R.id.account_number_teller);

        Button mDebitButton = (Button) findViewById(R.id.action_debit);
        Button mBalanceButton = (Button) findViewById(R.id.action_balance);
        Button mCreditButton = (Button) findViewById(R.id.action_credit);
        Button mTransferButton = (Button) findViewById(R.id.action_transfer);
        Button mCloseButton = (Button) findViewById(R.id.action_close);
        Button mLogoutButton = (Button) findViewById(R.id.action_logout);


        //TODO: SHOW BALANCE
        // Button to Show Balance activity
        mBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Balance Activity
                Intent i = new Intent(MainActivityTeller.this, BalanceActivity.class);
                startActivity(i);

                // Close this activity
                finish();
            }
        });


        //TODO: ACCOUNT SUMMARY


        // Button to debit activity
        mDebitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    checkAccount();
                } catch (ParseException e2){
                    Toast.makeText(getApplicationContext(), "ERROR",
                            Toast.LENGTH_LONG).show();
                }

                if (cancel) {
                    focusView.requestFocus();
                }

                else {
                    // Go to Debit Activity
                    Intent i = new Intent(MainActivityTeller.this, DebitActivity.class);
                    i.putExtra("accountnumber", input_account);
                    startActivity(i);

                    // Close this activity
                    finish();
                }
            }
        });


        // Button to credit activity
        mCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    checkAccount();
                } catch (ParseException e2){
                    Toast.makeText(getApplicationContext(), "ERROR",
                            Toast.LENGTH_LONG).show();
                }

                if (cancel) {
                    focusView.requestFocus();
                }

                else {
                    // Go to Credit Activity
                    Intent i = new Intent(MainActivityTeller.this, CreditActivity.class);
                    i.putExtra("accountnumber", input_account);
                    startActivity(i);

                    // Close this activity
                    finish();
                }
            }
        });


        //TODO: TRANSFER FUNDS


        //TODO: CLOSE ACCOUNT
        // Button to close activity
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Close Activity
                Intent i = new Intent(MainActivityTeller.this, CloseActivity.class);
                startActivity(i);
                // Close this activity
                finish();
            }
        });


        // Button to log out user
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                currentUser.logOut();

                Toast.makeText(getApplicationContext(), "You have been logged out",
                        Toast.LENGTH_LONG).show();

                // Go to Login Page
                Intent i = new Intent(MainActivityTeller.this, LoginActivity.class);
                startActivity(i);

                // Close this activity
                finish();
            }
        });
    }

    public void checkAccount() throws ParseException {
        mAccountNumber.setError(null);

        input_account = mAccountNumber.getText().toString();

        if (TextUtils.isEmpty(input_account)) {
            mAccountNumber.setError(getString(R.string.error_field_required));
            focusView = mAccountNumber;
            cancel = true;
        }

        else {
            //Toast.makeText(getApplicationContext(), "ADMIN has Deposited into " + accountNumber + "'s Account!", Toast.LENGTH_LONG).show();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
            query.whereEqualTo("accountnumber", Integer.valueOf(input_account));
            List<ParseObject> account = query.find();
            if (account.size() == 0) {
                mAccountNumber.setError(getString(R.string.error_invalid_account));
                focusView = mAccountNumber;
                cancel = true;
            }

            else {
                cancel = false;
            }
        }
    }
}


