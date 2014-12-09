package com.hhb.hardyhardybank;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.Button;
import android.view.View;
import android.content.Intent;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;

/**
 *   Debit screen for users to debit their account
 */
public class DebitActivity extends ActionBarActivity {
    // UI references.
    private EditText mDebitAmount;
    private TextView mAccountInfoView;
    private TextView mBalanceView;

    private int accountNumber;
    private String accountInfo;
    private String accountBalance;
    private String currentBalance;
    private double balance, debit_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit);

        // Set up the Debit Account form
        mDebitAmount = (EditText) findViewById(R.id.debit_amount);
        Button mDebitButton = (Button) findViewById(R.id.debit_enter);

        // Set up the account info and account balance before returning the View.
        mAccountInfoView = (TextView)findViewById(R.id.debit_activity_account_info);
        mBalanceView = (TextView)findViewById(R.id.debit_activity_account_balance);

        Bundle bundle = getIntent().getExtras();
        accountNumber = bundle.getInt("accountnumber");
        accountInfo = bundle.getString("accountInfo");
        accountBalance = bundle.getString("accountBalance");

        // Set up the accountInfoView and accountBalanceView
        mAccountInfoView.setText(accountInfo);
        mBalanceView.setText(accountBalance);
/*
        // get the current balance of the account number
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        query.whereEqualTo("accountnumber", accountNumber);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject account, com.parse.ParseException e) {
                balance = account.getDouble("balance");
            }

        });

        DecimalFormat balanceFormat = new DecimalFormat("#0.00");

        final String initialAccountBalance = "$" + balanceFormat.format(balance);

        mAccountInfoView.setText(accountInfo);
        mBalanceView.setText(initialAccountBalance);
*/
        // Query Parse for account balance value
//        ParseObject currentUser = ParseUser.getCurrentUser();
//        balance = currentUser.getDouble("balance");

        // Format displayed balance
//        DecimalFormat format = new DecimalFormat("#0.00");
//        final String formatted_balance = format.format(balance);
//        mDisplayBalance.setText("$" + formatted_balance);

        // Activity once Deposit button is pressed
        mDebitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Query Parse for account balance value
                ParseObject currentUser = ParseUser.getCurrentUser();
                debit_amount =  Double.valueOf(mDebitAmount.getText().toString());
                if (currentUser.get("role").toString().contentEquals("admin")) {

                    //final int accountNumber = Integer.valueOf(bundle.getString("accountnumber"));


                    //Toast.makeText(getApplicationContext(), "ADMIN has Deposited into " + accountNumber + "'s Account!", Toast.LENGTH_LONG).show();
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
                    query.whereEqualTo("accountnumber", accountNumber);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject account, com.parse.ParseException e) {
                            if (account== null) {
                                Toast.makeText(getApplicationContext(), "ADMIN could not find Account!", Toast.LENGTH_LONG).show();
                            } else {

                                balance = account.getDouble("balance") - debit_amount;
                                if(balance >= 0) {
                                    account.increment("balance", -debit_amount);
                                    account.saveEventually();

                                    // Update the balance textview
                                    DecimalFormat balanceFormat = new DecimalFormat("#0.00");
                                    currentBalance = "$" + balanceFormat.format(balance);
                                    mBalanceView.setText(currentBalance);

                                    // documents the transaction
                                    ParseObject transaction = new ParseObject("Transaction");
                                    transaction.put("accountNumber", accountNumber);
                                    transaction.put("action", "debit");
                                    transaction.put("amount", -1 * Double.valueOf(mDebitAmount.getText().toString()));
                                    transaction.put("resultingBalance", account.getDouble("balance"));
                                    transaction.saveEventually();

                                    Toast.makeText(getApplicationContext(), "ADMIN has withdrawn $" +
                                            debit_amount + " from " + account.getString("userID") +
                                            "'s Account!", Toast.LENGTH_LONG).show();
                                }
                                else{

                                    Toast.makeText(getApplicationContext(), "Insufficient funds!", Toast.LENGTH_LONG).show();
                                }
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



        // Activity when "Return to Main" button is pressed
        Button mDebitReturn = (Button) findViewById(R.id.debit_return);
        mDebitReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to ADMIN Main Activity
                Intent i = new Intent(DebitActivity.this, AdminActivity.class);
                i.putExtra("accountnumber", accountNumber);
                i.putExtra("accountInfo", accountInfo);
                i.putExtra("accountBalance", currentBalance);
                startActivity(i);

                // Close this activity
                finish();
            }
        });
    }
}