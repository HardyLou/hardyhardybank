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
 *   Credit screen for admins to credit a user's account
 */
public class CreditActivity extends ActionBarActivity {
    // UI references.
    private EditText mCreditAmount;
    private TextView mAccountInfoView;
    private TextView mBalanceView;

    private int accountNumber;
    private String accountInfo;
    private String accountBalance;
    private String currentBalance;
    private double balance, credit_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        // Set up the Credit Account form
        mCreditAmount = (EditText) findViewById(R.id.credit_amount);
        Button mCreditButton = (Button) findViewById(R.id.credit_enter);

        // Set up the account info and account balance before returning the View.
        mAccountInfoView = (TextView)findViewById(R.id.credit_activity_account_info);
        mBalanceView = (TextView)findViewById(R.id.credit_activity_account_balance);

        Bundle bundle = getIntent().getExtras();
        accountNumber = bundle.getInt("accountnumber");
        accountInfo = bundle.getString("accountInfo");
        accountBalance = bundle.getString("accountBalance");

        // Set up the accountInfoView and accountBalanceView
        mAccountInfoView.setText(accountInfo);
        mBalanceView.setText(accountBalance);


        // Activity once Deposit button is pressed
        mCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseObject currentUser = ParseUser.getCurrentUser();
                credit_amount =  Double.valueOf(mCreditAmount.getText().toString());

                if (currentUser.get("role").toString().contentEquals("admin")) {

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
                    query.whereEqualTo("accountnumber", accountNumber);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject account, com.parse.ParseException e) {
                            if (accountInfo == null) {
                                Toast.makeText(getApplicationContext(), "ADMIN could not find Account!", Toast.LENGTH_LONG).show();
                            } else {

                                // deposits money into balance
                                balance = account.getDouble("balance") + credit_amount;
                                account.increment("balance", credit_amount);
                                account.saveEventually();

                                // Update the balance textview
                                DecimalFormat balanceFormat = new DecimalFormat("#0.00");
                                currentBalance = "$" + balanceFormat.format(balance);
                                mBalanceView.setText(currentBalance);

                                // documents the transaction
                                ParseObject transaction = new ParseObject("Transaction");
                                transaction.put("accountNumber", accountNumber);
                                transaction.put("action", "credit");
                                transaction.put("amount", Double.valueOf(mCreditAmount.getText().toString()));
                                transaction.put("resultingBalance", account.getDouble("balance"));
                                transaction.saveEventually();

                                Toast.makeText(getApplicationContext(), "ADMIN has deposited $" +
                                        credit_amount + " into " + account.getString("userID") +
                                        "'s Account!", Toast.LENGTH_LONG).show();
                            }

                        }

                    });
                }
            }
        });


        // Activity when "Return to Main" button is pressed
        Button mCreditReturn = (Button) findViewById(R.id.credit_return);
        mCreditReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to ADMIN Main Activity
                Intent i = new Intent(CreditActivity.this, AccountAdminActivity.class);
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