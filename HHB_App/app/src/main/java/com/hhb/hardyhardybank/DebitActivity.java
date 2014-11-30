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
 *   Credit screen for users to credit their account
 */
public class DebitActivity extends ActionBarActivity {
    // UI references.
    private EditText mCreditAmount;
    private TextView mDisplayBalance;

    double balance, credit_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit);

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
                credit_amount =  Double.valueOf(mCreditAmount.getText().toString());
                if (currentUser.get("role").toString().contentEquals("admin")) {

                    Bundle bundle = getIntent().getExtras();
                    int accountNumber = Integer.valueOf(bundle.getString("accountnumber"));


                    //Toast.makeText(getApplicationContext(), "ADMIN has Deposited into " + accountNumber + "'s Account!", Toast.LENGTH_LONG).show();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
                    query.whereEqualTo("accountnumber", accountNumber);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject accountInfo, com.parse.ParseException e) {
                            if (accountInfo == null) {
                                Toast.makeText(getApplicationContext(), "ADMIN could not find Account!", Toast.LENGTH_LONG).show();
                            } else {
                                accountInfo.increment("balance", -1*Double.valueOf(mCreditAmount.getText().toString()));

                                accountInfo.saveEventually();

                                Toast.makeText(getApplicationContext(), "ADMIN has Withdrew $" + credit_amount + " from " + accountInfo.getString("userID") + "'s Account!", Toast.LENGTH_LONG).show();
                            }

                        }

                    });
                }
//                balance = currentUser.getDouble("balance");
//
//                // Adds inputted deposit amount to current balance
//                credit_amount =  Double.valueOf(mCreditAmount.getText().toString());
//                if(balance - credit_amount > 0) {
//                    currentUser.increment("balance", -1 * credit_amount);
//                    // TODO: Follow DRY
//                    // Update the displayed balance to reflect new account balance
//                    DecimalFormat format = new DecimalFormat("#0.00");
//                    String formatted_balance = format.format(currentUser.getDouble("balance"));
//                    mDisplayBalance.setText("$" + formatted_balance);
//
//                    // Notifies user of successful deposit
//                    Toast.makeText(getApplicationContext(), "Withdrew $" + format.format(credit_amount)
//                                    + ".",
//                            Toast.LENGTH_LONG).show();
//
//                    currentUser.saveEventually();
//                }
//                else{
//
//                    Toast.makeText(getApplicationContext(), "Insufficient funds.",
//                            Toast.LENGTH_LONG).show();
//                }
            }
        });

//        Button mDebitReturn = (Button) findViewById(R.id.debit_return_main);
//        mDebitReturn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Go to Main Activity
//                Intent i = new Intent(DebitActivity.this, MainActivity.class);
//                startActivity(i);
//
//                // Close this activity
//                finish();
//            }
//        });
    }
}