package com.hhb.hardyhardybank;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import com.parse.Parse;

/**
 *  Landing page from login; menu access to other functionality for Admins
 */
public class AdminActivity extends  ActionBarActivity{


    private String accountInfo;
    private String accountBalance;
    private int accountNumber;
    private Bundle bundle;

    boolean cancel = false;
    View focusView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Set up TextView on the top to show the account info and balance
        TextView accountInfoView = (TextView)findViewById(R.id.admin_activity_account_info);
        TextView accountBalanceView = (TextView)findViewById(R.id.admin_activity_account_balance);

        bundle = getIntent().getExtras();
        accountInfo = bundle.getString("accountInfo");
        accountBalance = bundle.getString("accountBalance");
        accountNumber = bundle.getInt("accountnumber");


        accountInfoView.setText(accountInfo);
        accountBalanceView.setText(accountBalance);


        // Button to debit activity
        Button mDebitButton = (Button) findViewById(R.id.action_debit);
        mDebitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cancel) {
                    focusView.requestFocus();
                } else {
                    // Go to Debit Activity
                    Intent i = new Intent(AdminActivity.this, DebitActivity.class);
                    i.putExtra("accountnumber", accountNumber);
                    i.putExtra("accountInfo", accountInfo);
                    i.putExtra("accountBalance", accountBalance);
                    startActivity(i);

                    finish();

                }
            }
        });


        // Button for Transactions
        Button mTransactionButton = (Button) findViewById(R.id.action_transaction);
        mTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cancel) {
                    focusView.requestFocus();
                } else {
                    // Go to Credit Activity
                    Intent i = new Intent(AdminActivity.this, TransactionActivity.class);
                    i.putExtra("accountnumber", accountNumber);
                    i.putExtra("accountInfo", accountInfo);
                    i.putExtra("accountBalance", accountBalance);
                    startActivity(i);

                    finish();
                }
            }
        });


        // Button to credit activity
        Button mCreditButton = (Button) findViewById(R.id.action_credit);
        mCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cancel) {
                    focusView.requestFocus();
                } else {
                    // Go to Credit Activity
                    Intent i = new Intent(AdminActivity.this, CreditActivity.class);
                    i.putExtra("accountnumber", accountNumber);
                    i.putExtra("accountInfo", accountInfo);
                    i.putExtra("accountBalance", accountBalance);
                    startActivity(i);

                    finish();
                }
            }
        });


        // Button to close activity
        Button mCloseButton = (Button) findViewById(R.id.action_close);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Close Activity
                Intent i = new Intent(AdminActivity.this, CloseActivity.class);
                i.putExtra("accountnumber", accountNumber);
                i.putExtra("accountInfo", accountInfo);
                i.putExtra("accountBalance", accountBalance);
                startActivity(i);

                finish();
            }
        });


        // Button to return to main screen (MainActivityAdmin)
        Button mReturnButton = (Button) findViewById(R.id.action_return);
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to MainActivityAdmin
                Intent i = new Intent(AdminActivity.this, MainActivityAdmin.class);
                startActivity(i);

            }
        });

    }

}


