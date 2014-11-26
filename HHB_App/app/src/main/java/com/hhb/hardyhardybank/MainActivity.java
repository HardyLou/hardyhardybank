package com.hhb.hardyhardybank;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

import java.text.DecimalFormat;

/**
 *   Landing page from login; menu access to other functionality
 */
public class MainActivity extends ActionBarActivity {
    // UI References
    private TextView mSavingsBalance;
    private TextView mCheckingBalance;

    double savings_balance, checking_balance;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the Credit Account form
        mSavingsBalance = (TextView) findViewById(R.id.savings_balance);
        mCheckingBalance = (TextView) findViewById(R.id.checking_balance);

        // Query Parse for username
        ParseObject currentUser = ParseUser.getCurrentUser();
        //final ParseObject user_obj_checking = ParseUser.getCurrentUser();
        userName = currentUser.getString("username");

        // Query Parse for checking balance
        ParseQuery<ParseObject> queryChecking = ParseQuery.getQuery("Account");
        queryChecking.whereEqualTo("userID", userName);
        queryChecking.whereEqualTo("accountType", "Checking");
        queryChecking.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject account, ParseException e) {
                if (account == null) {
                    Log.d("account", "The getFirst request failed.");
                } else {
                    //savings_balance = account.getDouble("balance");
                    checking_balance = account.getDouble("balance");
                    DecimalFormat format_checkings = new DecimalFormat("#0.00");
                    final String formatted_balance_checkings = format_checkings.format(checking_balance);
                    mCheckingBalance.setText("$" + formatted_balance_checkings);
                }
            }
        });


        // Query Parse for savings balance
        ParseQuery<ParseObject> querySaving = ParseQuery.getQuery("Account");
        querySaving.whereEqualTo("userID", userName);
        querySaving.whereEqualTo("accountType", "Saving");
        querySaving.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject account, ParseException e) {
                if (account == null) {
                    Log.d("account", "The getFirst request failed.");
                } else {
                    savings_balance = account.getDouble("balance");
                    //checking_balance = account.getDouble("balance");
                    DecimalFormat format_savings = new DecimalFormat("#0.00");
                    final String formatted_balance_savings = format_savings.format(savings_balance);
                    mSavingsBalance.setText("$" + formatted_balance_savings);
                }
            }
        });


        // TODO separate checking & savings balances by query

        //user_obj_checking.
        //savings_balance = user_obj_savings.getDouble("balance");
        //checking_balance = user_obj_checking.getDouble("balance");

        // Format savings balance
        //DecimalFormat format_savings = new DecimalFormat("#0.00");
        //final String formatted_balance_savings = format_savings.format(savings_balance);
        //mSavingsBalance.setText("$" + formatted_balance_savings);

        // TODO Format checking balance
//        DecimalFormat format_checking = new DecimalFormat("#0.00");
  //      final String formatted_balance_checking = format_checking.format(checking_balance);
    //    mCheckingBalance.setText("$" + formatted_balance_checking);

        // TODO button to Savings account activity
        // TODO button to Checking account activity

        // Button to debit activity
        Button mDebitButton = (Button) findViewById(R.id.action_debit);
        mDebitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Debit Activity
                Intent i = new Intent(MainActivity.this, DebitActivity.class);
                startActivity(i);

                // Close this activity
                finish();
            }
        });

        // Button to credit activity
        Button mCreditButton = (Button) findViewById(R.id.action_credit);
        mCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Credit Activity
                Intent i = new Intent(MainActivity.this, CreditActivity.class);
                startActivity(i);

                // Close this activity
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
