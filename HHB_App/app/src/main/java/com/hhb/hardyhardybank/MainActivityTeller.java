package com.hhb.hardyhardybank;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import com.parse.Parse;


/**
 * Created by dan on 11/15/14.
 */
public class MainActivityTeller extends  ActionBarActivity{

    private EditText mAccountNumber;
    private String accountNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_teller);

        mAccountNumber = (EditText) findViewById(R.id.account_number_teller);

        Button mDebitButton = (Button) findViewById(R.id.action_debit);
        Button mBalanceButton = (Button) findViewById(R.id.action_balance);
        Button action_credit = (Button) findViewById(R.id.action_credit);
        Button action_transfer = (Button) findViewById(R.id.action_transfer);
        Button action_close = (Button) findViewById(R.id.action_close);


            // Button to credit activity

        //TODO: BALANCE ACTIVITY
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

        // Button to debit activity

        mDebitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Debit Activity
                Intent i = new Intent(MainActivityTeller.this, DebitActivity.class);
                accountNumber = mAccountNumber.getText().toString();
                i.putExtra("accountnumber", accountNumber);
                startActivity(i);

                // Close this activity
                finish();
            }
        });


        // Button to credit activity

        action_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Credit Activity
                Intent i = new Intent(MainActivityTeller.this, CreditActivity.class);
                accountNumber = mAccountNumber.getText().toString();
                i.putExtra("accountnumber", accountNumber);
                startActivity(i);

                // Close this activity
                finish();
            }
        });

    }
}


