package com.hhb.hardyhardybank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.DecimalFormat;

/**
 * Page for a specific account under a user; menu access to other functionality for Accounts
 */
public class AccountUserActivity extends Activity {

    private String accountInfo;
    private String currentBalance;
    private int accountNumber;
    private double balance;
    private Bundle bundle;

    private TextView accountInfoView;
    private TextView accountBalanceView;

    protected void onCreate(Bundle SavedInstanceState) {

        // Connect app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_account_user);


        // Set up text view on the top to show the account info and balance
        accountInfoView = (TextView)findViewById(R.id.account_activity_customer_item1);
        accountBalanceView = (TextView)findViewById(R.id.account_activity_customer_item2);

        bundle = getIntent().getExtras();
        accountInfo = bundle.getString("accountInfo");
        accountNumber = bundle.getInt("accountnumber");


        // Update the balance textview
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        query.whereEqualTo("accountnumber", accountNumber);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject account, com.parse.ParseException e) {
                balance = account.getDouble("balance");

                // Update the balance TextView
                DecimalFormat balanceFormat = new DecimalFormat("#0.00");
                currentBalance = "$" + balanceFormat.format(balance);
                accountInfoView.setText(accountInfo);
                accountBalanceView.setText(currentBalance);
            }
        });


        // Button to view transaction log for current account
        Button mTransLogButton = (Button) findViewById(R.id.action_transaction_log);
        mTransLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to TransactionLogActivity
                Intent i;
                i = new Intent(AccountUserActivity.this, TransactionLogActivity.class);

                // passes along the bundle
                i.putExtra("accountnumber",accountNumber);
                i.putExtra("accountInfo",accountInfo);

                // Pass the updated accountBalance
                i.putExtra("accountBalance", currentBalance);

                startActivity(i);

            }
        });


        // Button to Transfer Funds to another user
        Button mTransferToAnotherButton = (Button) findViewById(R.id.action_transfer_to_another);
        mTransferToAnotherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to TransferUserActivity
                Intent i;
                i = new Intent(AccountUserActivity.this, TransferUserActivity.class);
                i.putExtra("accountnumber",accountNumber);
                i.putExtra("accountInfo",accountInfo);

                // Pass the updated accountBalance
                i.putExtra("accountBalance",currentBalance);

                startActivity(i);

            }
        });

        // Button to Transfer Funds
        Button mTransferButton = (Button) findViewById(R.id.action_transfer_to_yourself);
        mTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to TransferUserActivity
                Intent i;
                i = new Intent(AccountUserActivity.this, TransferAccountActivity.class);
                i.putExtra("accountnumber",accountNumber);
                i.putExtra("accountInfo",accountInfo);

                // Pass the updated accountBalance
                i.putExtra("accountBalance",currentBalance);
                startActivity(i);

            }
        });


        // Button to Close Account
        Button mCloseAcctButton = (Button) findViewById(R.id.action_close_acct);
        mCloseAcctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to AddAccountActivity
                Intent i = new Intent(AccountUserActivity.this, CloseActivity.class);

                i.putExtra("accountnumber",accountNumber);
                i.putExtra("accountInfo",accountInfo);

                // Pass the updated accountBalance
                i.putExtra("accountBalance",currentBalance);

                startActivity(i);
            }
        });


        // Button to return to main screen (MainUserActivity)
        Button mReturnButton = (Button) findViewById(R.id.action_return);
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to AddAccountActivity
                Intent i = new Intent(AccountUserActivity.this, MainUserActivity.class);
                startActivity(i);

            }
        });
    }

}
