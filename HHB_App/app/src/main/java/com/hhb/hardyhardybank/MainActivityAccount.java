package com.hhb.hardyhardybank;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.DecimalFormat;

/**
 * Page for a specific account under a user; menu access to other functionality for Accounts
 */
public class MainActivityAccount extends Activity {

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
        setContentView(R.layout.activity_main_account);


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

                                           // Update the balance textview
                                           DecimalFormat balanceFormat = new DecimalFormat("#0.00");
                                           currentBalance = "$" + balanceFormat.format(balance);
                                           accountInfoView.setText(accountInfo);
                                           accountBalanceView.setText(currentBalance);
                                       }
                                   });




/*
        ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                ParseQuery<ParseObject> query = new ParseQuery("Account");
                query.whereEqualTo("userID", userName);
                return query;
            }
        };


        inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainActivityCustomerAdapter = new CustomAdapter(this, factory);

        // Attach the query adapter to the view
        lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(mainActivityCustomerAdapter);

*/
        // Button to view transaction log for current account
        Button mTransLogButton = (Button) findViewById(R.id.action_transaction_log);
        mTransLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to TransferActivity
                Intent i;
                i = new Intent(MainActivityAccount.this, TransactionActivity.class);

                // passes along the bundle
                i.putExtra("accountnumber",accountNumber);
                i.putExtra("accountInfo",accountInfo);

                // Pass the updated accountBalance
                i.putExtra("accountBalance",currentBalance);

                startActivity(i);

            }
        });


        // Button to Transfer Funds to another user
        Button mTransferButton = (Button) findViewById(R.id.action_transfer);
        mTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to TransferActivity
                Intent i;
                i = new Intent(MainActivityAccount.this, TransferActivity.class);
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
                Intent i = new Intent(MainActivityAccount.this, CloseActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        });


        // Button to return to main screen (MainActivityCustomer)
        Button mReturnButton = (Button) findViewById(R.id.action_return);
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to AddAccountActivity
                Intent i = new Intent(MainActivityAccount.this, MainActivityCustomer.class);
                startActivity(i);

            }
        });
    }
/*

    private class CustomAdapter extends ParseQueryAdapter<ParseObject> {

        TextView mAccountInfoView;
        TextView mBalanceView;

        public CustomAdapter(Context context,
                             ParseQueryAdapter.QueryFactory<ParseObject> queryFactory) {
            super(context, queryFactory);
        }

        @Override
        public View getItemView(ParseObject object, View view, ViewGroup parent) {

            if (view == null) {
                view = inflater.inflate(R.layout.customer_list_item, parent, false);
            }

            // Take advantage of ParseQueryAdapter's getItemView logic
            // The IDs in your custom layout must match what ParseQueryAdapter expects
            super.getItemView(object, view, parent);

            // Set up the listView item before returning the View.
            mAccountInfoView = (TextView) view.findViewById(R.id.customer_item1);
            mBalanceView = (TextView) view.findViewById(R.id.customer_item2);

            Bundle bundle = getIntent().getExtras();

            String accountType = bundle.getString("accountType");
            int accountNumber = bundle.getInt("accountnumber");
            double balance = bundle.getDouble("balance");

            DecimalFormat accountNumberFormat = new DecimalFormat("#.#");
            DecimalFormat balanceFormat = new DecimalFormat("#0.00");

            final String accountInfo = "HARDY " + accountType + " (" + accountNumberFormat.format(accountNumber) + ")";
            final String accountBalance = "$" + balanceFormat.format(balance);

            mAccountInfoView.setText(accountInfo);
            mBalanceView.setText(accountBalance);


            return view;
        }
    }
*/

}
