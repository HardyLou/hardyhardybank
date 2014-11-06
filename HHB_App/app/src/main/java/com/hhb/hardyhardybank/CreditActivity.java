package com.hhb.hardyhardybank;

import android.app.Activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import java.text.DecimalFormat;

import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 *   Credit screen for users to credit their account
 */
public class CreditActivity extends Activity {
    // UI references.
    private EditText mCreditAmount;
    private TextView mDisplayBalance;

    double balance, credit_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        // Set up the Credit Account form
        mCreditAmount = (EditText) findViewById(R.id.credit_amount);
        mDisplayBalance = (TextView) findViewById(R.id.available_balance);

        // Query Parse for account balance value
        ParseObject currentUser = ParseUser.getCurrentUser();
        balance = currentUser.getDouble("balance");

        // Format displayed balance
        DecimalFormat format = new DecimalFormat("#0.00");
        final String formatted_balance = format.format(balance);
        mDisplayBalance.setText("$" + formatted_balance);

        // Activity once Deposit button is pressed
        Button mCreditButton = (Button) findViewById(R.id.credit_enter);
        mCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Query Parse for account balance value
                ParseObject currentUser = ParseUser.getCurrentUser();
                balance = currentUser.getDouble("balance");

                // Adds inputted deposit amount to current balance
                credit_amount =  Double.valueOf(mCreditAmount.getText().toString());
                currentUser.increment("balance", credit_amount);
                currentUser.saveEventually();


                // TODO: Follow DRY
                // Update the displayed balance to reflect new account balance
                DecimalFormat format = new DecimalFormat("#0.00");
                String formatted_balance = format.format(currentUser.getDouble("balance"));
                mDisplayBalance.setText("$" + formatted_balance);

                // Notifies user of successful deposit
                Toast.makeText(getApplicationContext(), "Deposited $" + format.format(credit_amount)
                               + " to credit account.",
                        Toast.LENGTH_LONG).show();
            }
        });

        Button mCreditReturn = (Button) findViewById(R.id.credit_return_main);
        mCreditReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Main Activity
                Intent i = new Intent(CreditActivity.this, MainActivity.class);
                startActivity(i);

                // Close this activity
                finish();
            }
         });
    }
}