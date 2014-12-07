package com.hhb.hardyhardybank;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


/**
 *  Landing page from login; menu access to other functionality for Admins
 */
public class AdminActivity extends  ActionBarActivity{

  //  private EditText mAccountNumber;
    private String accountInfo;
    private String accountBalance;
    private double accountNumber;
    private Bundle bundle;

    boolean cancel = false;
    View focusView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Set up textview on the top to show the account info and balance
        TextView accountInfoView = (TextView)findViewById(R.id.admin_activity_account_info);
        TextView accountBalanceView = (TextView)findViewById(R.id.admin_activity_account_balance);

        bundle = getIntent().getExtras();
        accountInfo = bundle.getString("accountInfo");
        accountBalance = bundle.getString("accountBalance");
        accountNumber = bundle.getDouble("accountnumber");


        accountInfoView.setText(accountInfo);
        accountBalanceView.setText(accountBalance);

        // mAccountNumber = (EditText) findViewById(R.id.account_number_teller);

//        Button mLogoutButton = (Button) findViewById(R.id.action_logout);
//        Button mBalanceButton = (Button) findViewById(R.id.action_balance);

/*
        //TODO: SHOW BALANCE
        // Button to Show Balance activity
        mBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Balance Activity
                Intent i = new Intent(AdminActivity.this, BalanceActivity.class);
                startActivity(i);

                // DO NOT close this activity!
                //finish();
            }
        });

*/
        //TODO: ACCOUNT SUMMARY


        // Button to debit activity
        Button mDebitButton = (Button) findViewById(R.id.action_debit);
        mDebitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                try {
                    checkAccount(bundle);
                } catch (ParseException e2) {
                    Toast.makeText(getApplicationContext(), "ERROR",
                            Toast.LENGTH_LONG).show();
                }
*/
                if (cancel) {
                    focusView.requestFocus();
                } else {
                    // Go to Debit Activity
                    Intent i = new Intent(AdminActivity.this, DebitActivity.class);
                    i.putExtra("accountnumber", accountNumber);
                    i.putExtra("accountInfo", accountInfo);
                    i.putExtra("accountBalance", accountBalance);
                    startActivity(i);

                }
            }
        });

        Button mTransactionButton = (Button) findViewById(R.id.action_transaction);
        mTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                try {
                    checkAccount(bundle);
                } catch (ParseException e2) {
                    Toast.makeText(getApplicationContext(), "ERROR",
                            Toast.LENGTH_LONG).show();
                }
*/
                if (cancel) {
                    focusView.requestFocus();
                } else {
                    // Go to Credit Activity
                    Intent i = new Intent(AdminActivity.this, TransactionActivity.class);
                    i.putExtra("accountnumber", accountNumber);
                    i.putExtra("accountInfo", accountInfo);
                    i.putExtra("accountBalance", accountBalance);
                    startActivity(i);

                }
            }
        });


        // Button to credit activity
        Button mCreditButton = (Button) findViewById(R.id.action_credit);
        mCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                try {
                    checkAccount(bundle);
                } catch (ParseException e2) {
                    Toast.makeText(getApplicationContext(), "ERROR",
                            Toast.LENGTH_LONG).show();
                }
*/
                if (cancel) {
                    focusView.requestFocus();
                } else {
                    // Go to Credit Activity
                    Intent i = new Intent(AdminActivity.this, CreditActivity.class);
                    i.putExtra("accountnumber", accountNumber);
                    i.putExtra("accountInfo", accountInfo);
                    i.putExtra("accountBalance", accountBalance);
                    startActivity(i);

                }
            }
        });


        //TODO: CLOSE ACCOUNT
        // Button to close activity
        Button mCloseButton = (Button) findViewById(R.id.action_close);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Close Activity
                Intent i = new Intent(AdminActivity.this, CloseActivity.class);
                startActivity(i);

            }
        });

    }

/*
        // Button to log out user
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                currentUser.logOut();

                Toast.makeText(getApplicationContext(), "You have been logged out",
                        Toast.LENGTH_LONG).show();

                // Go to Login Page
                Intent i = new Intent(AdminActivity.this, LoginActivity.class);
                startActivity(i);

                // Close this activity
                finish();
            }
        });
    }
*/
/*
    public void checkAccount(Bundle bundle) throws ParseException {








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
        } else {
            cancel = false;
        }


    }
*/

}


