package com.hhb.hardyhardybank;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import java.text.DecimalFormat;

import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 *  Balance screen to view a specified account balance.
 */
public class BalanceActivity extends ActionBarActivity {


    //UI references
    double balance;
    private TextView mDisplayBalance;
    private Button mBalanceBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        mBalanceBack = (Button) findViewById(R.id.balance_back);


        //TODO fix for multiple accounts database changes
        // Query Parse for account balance value
//        ParseObject currentUser = ParseUser.getCurrentUser();
//        balance = currentUser.getDouble("balance");

        // Format displayed balance
//        DecimalFormat format = new DecimalFormat("#0.00");
//        final String formatted_balance = format.format(balance);
//        mDisplayBalance.setText("$" + formatted_balance);


        Bundle bundle = getIntent().getExtras();
        final int accountNumber = Integer.valueOf(bundle.getString("accountnumber"));
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        query.whereEqualTo("accountnumber", accountNumber);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject accountInfo, com.parse.ParseException e) {
                if (accountInfo == null) {
                    Toast.makeText(getApplicationContext(), "ADMIN could not find Account!", Toast.LENGTH_LONG).show();
                } else {
                    balance = accountInfo.getDouble("balance");



                }

            }

        });
        // Format displayed balance
        DecimalFormat format = new DecimalFormat("#0.00");
        final String formatted_balance = format.format(balance);
        mDisplayBalance.setText("$" + formatted_balance);


        // Button to Show Balance activity
        mBalanceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Close this activity
                finish();
            }
        });
    }
}
