package com.hhb.hardyhardybank;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

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
public class CreditActivity extends ActionBarActivity {
    // UI references.
    private EditText mCreditAmount;
    double balance, credit_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        // Set up the Credit Account form
        mCreditAmount = (EditText) findViewById(R.id.credit_amount);
        Button mCreditReturn = (Button) findViewById(R.id.credit_return_main);
        Button mCreditBalance = (Button) findViewById(R.id.credit_balance);

        // Button to Show Balance activity
        mCreditBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Balance Activity
                Intent i = new Intent(CreditActivity.this, BalanceActivity.class);
                startActivity(i);

                // DO NOT close this activity!
            }
        });


        // Activity once Deposit button is pressed
        Button mCreditButton = (Button) findViewById(R.id.credit_enter);
        mCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseObject currentUser = ParseUser.getCurrentUser();
                credit_amount =  Double.valueOf(mCreditAmount.getText().toString());

                if (currentUser.get("role").toString().contentEquals("admin")) {

                    Bundle bundle = getIntent().getExtras();
                    final int accountNumber = Integer.valueOf(bundle.getString("accountnumber"));


                    //Toast.makeText(getApplicationContext(), "ADMIN has Deposited into " + accountNumber + "'s Account!", Toast.LENGTH_LONG).show();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
                    query.whereEqualTo("accountnumber", accountNumber);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject accountInfo, com.parse.ParseException e) {
                            if (accountInfo == null) {
                                Toast.makeText(getApplicationContext(), "ADMIN could not find Account!", Toast.LENGTH_LONG).show();
                            } else {
                                // deposits money into balance
                                accountInfo.increment("balance", Double.valueOf(mCreditAmount.getText().toString()));
                                accountInfo.saveEventually();

                                // documents the transaction
                                ParseObject transaction = new ParseObject("Transaction");
                                transaction.put("accountNumber", accountNumber);
                                transaction.put("action", "credit");
                                transaction.put("amount", Double.valueOf(mCreditAmount.getText().toString()));
                                transaction.put("resultingBalance", accountInfo.getDouble("balance"));
                                transaction.saveEventually();

                                Toast.makeText(getApplicationContext(), "ADMIN has deposited $" +
                                        credit_amount + " into " + accountInfo.getString("userID") +
                                        "'s Account!", Toast.LENGTH_LONG).show();
                            }

                        }

                    });
                }
                
                // DO NOT NEED FOR REQUIREMENTS OF PROJECT but it works
//                else {
//                    balance = currentUser.getDouble("balance");
//
//                    // Format displayed balance
//                    DecimalFormat format = new DecimalFormat("#0.00");
//                    String formatted_balance = format.format(balance);
//                    mDisplayBalance.setText("$" + formatted_balance);
//
//
//                    // Query Parse for account balance value
//                    String username = currentUser.getString("username");
//                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
//                    query.whereEqualTo("userID", username);
//                    query.getFirstInBackground(new GetCallback<ParseObject>() {
//                        public void done(ParseObject accountInfo, com.parse.ParseException e) {
//                            if (accountInfo == null) {
//                                Toast.makeText(getApplicationContext(), "Could not find Account!", Toast.LENGTH_LONG).show();
//                            } else {
//                                accountInfo.increment("balance", Double.valueOf(mCreditAmount.getText().toString()));
//
//                                accountInfo.saveEventually();
//
//                                // Notifies user of successful deposit
//                                Toast.makeText(getApplicationContext(), "Deposited $" + credit_amount
//                                                + " into your account.",
//                                        Toast.LENGTH_LONG).show();
//                            }
//
//                        }
//
//                    });
//                }
            }
        });


        // Activity when "Return to Main" button is pressed
        mCreditReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to ADMIN Main Activity
                Intent i = new Intent(CreditActivity.this, MainActivityAdmin.class);
                startActivity(i);

                // Close this activity
                finish();
            }
         });

    }
}