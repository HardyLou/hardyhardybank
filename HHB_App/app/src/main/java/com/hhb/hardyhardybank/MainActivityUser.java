package com.hhb.hardyhardybank;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Landing page from login; menu access to other functionality for Users
 */

public class MainActivityUser extends ActionBarActivity {

    // UI References
    private Spinner mAccounts;
    //private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);


        // Set up Spinner item for available accounts
        // temporary code to make spinner work (hardcoded account values)
        mAccounts = (Spinner) findViewById(R.id.available_accounts);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.account_numbers_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAccounts.setAdapter(adapter);

        //List<String> account_numbers_array2 = new ArrayList<String>();

        //TODO fix this to dynamically populate accounts
        //for each entry in accounts table
        //if username == current user
        //adapter.add("account number here");

        //mAccounts = (Spinner) findViewById(R.id.available_accounts);
        //adapter = new ArrayAdapter<String>(this,
        //        android.R.layout.simple_spinner_item, account_numbers_array2);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //mAccounts.setAdapter(adapter);

        // Button to Show Balance
        Button mBalanceButton = (Button) findViewById(R.id.action_balance);
        mBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to BalanceActivity
                Intent i = new Intent(MainActivityUser.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        });

        // Button for Account Summary
        Button mSummaryButton = (Button) findViewById(R.id.action_summary);
        mSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        // go to SummaryActivity
                Intent i = new Intent(MainActivityUser.this, DisplayUserInfoActivity.class);
                startActivity(i);

        // close this activity
                finish();
            }
        });

        // Button to Transfer Funds
        Button mTransferButton = (Button) findViewById(R.id.action_transfer);
        mTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        // go to TransferActivity
                Intent i = new Intent(MainActivityUser.this, TransferActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }
        });


        // Button to Add New Account
        Button mAddAccountButton = (Button) findViewById(R.id.action_add_acct_main);
        mAddAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to AddAccountActivity
                Intent i = new Intent(MainActivityUser.this, AddAccountActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        });


        // Button to Close Account
            Button closeUserAccount = (Button) findViewById(R.id.action_close);
            closeUserAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(MainActivityUser.this, CloseActivity.class);
                //startActivity(i);

                ParseUser currentUser = ParseUser.getCurrentUser();
                // Find account associated with input email and assign to targetAccount
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
                // TODO: change to query for the current account being viewed!
                // currently grabs all the accounts under the user
                query.whereEqualTo("userID", currentUser.getUsername());
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject targetAccount, com.parse.ParseException e) {
                        // Target account's email is not registered in database
                        if (targetAccount == null) {
                            Toast.makeText(getApplicationContext(), "Account to be closed does not exist.", Toast.LENGTH_LONG).show();
                        } else {
                            // Add transferred money to target account's balance
                            targetAccount.deleteInBackground();
                            Toast.makeText(getApplicationContext(), "Target account closed!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                // TODO: Remove after multiple account functionality is implemented!
                // currently deletes entire user
                currentUser.deleteInBackground(new DeleteCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "User has been deleted.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "User deletion failed.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

                // TODO: Remove after multiple account functionality is implemented!
                // currently sends user back to login screen
                Intent i = new Intent(MainActivityUser.this, LoginActivity.class);
                startActivity(i);
            }
        });


        // Button to Log Out Account
        Button mLogOutButton = (Button) findViewById(R.id.action_logout);
        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to AddAccountActivity
                Intent i = new Intent(MainActivityUser.this, LoginActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
