package com.hhb.hardyhardybank;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * List the transaction history for a specific account
 */
public class TransactionLogActivity extends Activity {

    // Adapter for the Todos Parse Query
    private ParseQueryAdapter<ParseObject> transactionAdapter;

    private LayoutInflater inflater;

    private ListView lv;


    private int accountNumber;
    private String accountInfo;
    private String accountBalance;
    private String accountRole;
    Bundle bundle;

    protected void onCreate(Bundle SavedInstanceState) {

        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_transaction_log);

        // Get the data passed from the previous page
        bundle = getIntent().getExtras();
        accountNumber = bundle.getInt("accountnumber");
        accountBalance = bundle.getString("accountBalance");
        accountInfo = bundle.getString("accountInfo");

        final ParseObject currentUser = ParseUser.getCurrentUser();
        accountRole = currentUser.getString("role");

        // Set up the views
        TextView accountInfoView = (TextView)findViewById(R.id.transactions_account_info);
        TextView accountBalanceView = (TextView)findViewById(R.id.transactions_balance);

        //Set up the titles
        accountInfoView.setText(accountInfo);
        accountBalanceView.setText(accountBalance);

        // Set up the Parse query to use in the adapter
        ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                ParseQuery<ParseObject> query = new ParseQuery("Transaction");
                query.whereEqualTo("accountNumber", accountNumber);
                query.orderByDescending("createdAt");
                return query;
            }
        };


        inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        transactionAdapter = new CustomAdapter(this, factory);

        // Attach the query adapter to the view
        lv = (ListView)findViewById(R.id.transactions_list_view);
        lv.setAdapter(transactionAdapter);


        // Activity once back button is pressed
        Button mReturnButton = (Button) findViewById(R.id.action_return);
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // for Admin
                if (accountRole.equals("admin")) {
                    // go to AddAccountActivity
                    Intent i = new Intent(TransactionLogActivity.this, AccountAdminActivity.class);

                    // passes along the bundle
                    i.putExtras(bundle);

                    startActivity(i);

                    // close this activity
                    finish();
                }

                // for Customer
                else if (accountRole.equals("customer")) {
                    // go to AddAccountActivity
                    Intent i = new Intent(TransactionLogActivity.this, AccountUserActivity.class);

                    // passes along the bundle
                    i.putExtras(bundle);

                    startActivity(i);

                    // close this activity
                    finish();
                }
            }

        });
    }


    private class CustomAdapter extends ParseQueryAdapter<ParseObject> {

        TextView transactionInfoView;
        TextView transactionAmountView;
        TextView transactionDateView;


        public CustomAdapter(Context context,
                             ParseQueryAdapter.QueryFactory<ParseObject> queryFactory) {
            super(context, queryFactory);
        }


        @Override
        public View getItemView(ParseObject object, View view, ViewGroup parent) {

            if (view == null) {
                view = inflater.inflate(R.layout.transaction_list_item, parent, false);
            }

            // Take advantage of ParseQueryAdapter's getItemView logic
            // The IDs in your custom layout must match what ParseQueryAdapter expects
            super.getItemView(object, view, parent);

            // Set up the listView item before returning the View.
            transactionInfoView = (TextView) view.findViewById(R.id.transaction_item1);
            transactionAmountView = (TextView) view.findViewById(R.id.transaction_item2);
            transactionDateView = (TextView) view.findViewById(R.id.transaction_item3);


            int accountNumber = object.getInt("accountNumber");
            double amount = object.getDouble("amount");
            String action = object.getString("action");
            Date date = object.getCreatedAt();

            DecimalFormat accountNumberFormat = new DecimalFormat("#.#");
            DecimalFormat balanceFormat = new DecimalFormat("#0.00");

            String accountInfo = "HARDY BANK" + " (" + accountNumberFormat.format(accountNumber) + ") ";

            final String transactionInfo = accountInfo + action;
            final String transactionAmount = "$" + balanceFormat.format(amount);

            transactionInfoView.setText(transactionInfo);
            transactionDateView.setText("" + date);
            transactionAmountView.setText(transactionAmount);

            return view;
        }
    }
}
