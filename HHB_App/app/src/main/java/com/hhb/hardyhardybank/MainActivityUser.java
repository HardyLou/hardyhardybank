package com.hhb.hardyhardybank;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

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
                Intent i = new Intent(MainActivityUser.this, BalanceActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        });

        // Button for Account Summary
        //Button mSummaryButton = (Button) findViewById(R.id.action_summary);
        //mSummaryButton.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {

        // go to SummaryActivity
        //        Intent i = new Intent(MainActivityUser.this, SummaryActivity.class);
        //        startActivity(i);

        // close this activity
        //        finish();
        //    }
        //});

        // Button to Transfer Funds
        Button mTransferButton = (Button) findViewById(R.id.action_transfer);
        mTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

        // go to TransferActivity
                Intent i = new Intent(MainActivityUser.this, TransferActivity.class);
                startActivity(i);
            }
        });


        // Button to Close Account
            Button closeUserAccount = (Button) findViewById(R.id.action_close);
            closeUserAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivityUser.this, CloseActivity.class);
                startActivity(i);
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
