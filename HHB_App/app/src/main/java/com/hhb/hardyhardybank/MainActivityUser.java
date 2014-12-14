package com.hhb.hardyhardybank;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.view.View;
import android.content.Intent;


/**
 * Landing page from login; menu access to other functionality for Users
 */

public class MainActivityUser extends ActionBarActivity {

    // UI References
    private Spinner mAccounts;
    private ArrayAdapter<String> adapter;
    private ParseQueryAdapter<ParseObject> mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);


        mainAdapter = new ParseQueryAdapter<ParseObject>(this,"Account");
        mainAdapter.setTextKey("accountnumber");
        mAccounts = (Spinner) findViewById(R.id.available_accounts);
        mAccounts.setAdapter(mainAdapter);
        mainAdapter.loadObjects();


        // Button to Show Balance
        Button mBalanceButton = (Button) findViewById(R.id.action_balance);
        mBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to BalanceActivity
                Intent i = new Intent(MainActivityUser.this, MainActivity.class);
                startActivity(i);

                // DO NOT close this activity!
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
        Button mTransferButton = (Button) findViewById(R.id.action_transfer_to_another);
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
                // go to CloseActivity
                Intent i = new Intent(MainActivityUser.this, CloseActivity.class);
                startActivity(i);

                // close this activity
                finish();
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
