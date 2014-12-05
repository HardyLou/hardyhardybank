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

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.DecimalFormat;

/**
 * Created by Xiaohan on 11/30/14.
 * Landing page from login; menu access to other functionality for Users
 */
public class MainActivityCustomer extends Activity {

    private String userName;

    // Adapter for the Todos Parse Query
    private ParseQueryAdapter<ParseObject> mainActivityCustomerAdapter;

    private LayoutInflater inflater;

    private ListView lv;

    private String accountType;


    private double accountNumber;
    private double balance;

    protected void onCreate(Bundle SavedInstanceState) {

        // Connect app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_main_customer);

        ParseUser currentUser = ParseUser.getCurrentUser();
        userName = currentUser.getString("username");


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

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ParseObject object = mainActivityCustomerAdapter.getItem(position);
                Intent i = new Intent(MainActivityCustomer.this, TransactionActivity.class);
                accountType = object.getString("accountType");
                accountNumber = object.getDouble("accountnumber");
                balance = object.getDouble("balance");

                DecimalFormat accountNumberFormat = new DecimalFormat("#.#");
                DecimalFormat balanceFormat = new DecimalFormat("#0.00");

                final String accountInfo = "HARDY " + accountType + " (" + accountNumberFormat.format(accountNumber) + ")";
                final String accountBalance = "$" + balanceFormat.format(balance);

                i.putExtra("accountInfo", accountInfo);
                i.putExtra("accountBalance", accountBalance);
                i.putExtra("accountnumber", accountNumber);

                startActivity(i);

            }
        });


        // Button to Log Out Account
        Button mLogOutButton = (Button) findViewById(R.id.action_logout);
        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to AddAccountActivity
                Intent i = new Intent(MainActivityCustomer.this, LoginActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        });



        // Button to Transfer Funds
        Button mTransferButton = (Button) findViewById(R.id.action_transfer);
        mTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to TransferActivity
                Intent i;
                i = new Intent(MainActivityCustomer.this, TransferActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }
        });


        // Button to Add New Account
        Button mAddAccountButton = (Button) findViewById(R.id.action_add_acct_main);
        mAddAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to AddAccountActivity
                Intent i = new Intent(MainActivityCustomer.this, AddAccountActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        });
    }


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
                view = inflater.inflate(R.layout.account_list_item, parent, false);
            }

            // Take advantage of ParseQueryAdapter's getItemView logic
            // The IDs in your custom layout must match what ParseQueryAdapter expects
            super.getItemView(object, view, parent);

            // Set up the listView item before returning the View.
            mAccountInfoView = (TextView) view.findViewById(R.id.item1);
            mBalanceView = (TextView) view.findViewById(R.id.item2);


            String accountType = object.getString("accountType");
            double accountNumber = object.getDouble("accountnumber");
            double balance = object.getDouble("balance");

            DecimalFormat accountNumberFormat = new DecimalFormat("#.#");
            DecimalFormat balanceFormat = new DecimalFormat("#0.00");

            final String accountInfo = "HARDY " + accountType + " (" + accountNumberFormat.format(accountNumber) + ")";
            final String accountBalance = "$" + balanceFormat.format(balance);

            mAccountInfoView.setText(accountInfo);
            mBalanceView.setText(accountBalance);

            /*mAccountInfoView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(MainActivityCustomer.this, TransactionActivity.class);
                    TextView textview = (TextView)view;

                    i.putExtra("accountType", accountType);
                    i.putExtra("accountnumber", accountNumber);
                    i.putExtra("accountInfo", textview.getText());
                    i.putExtra("accountBalance", textview.getText());

                    startActivity(i);

                    finish();
                }
            });
*/
            /*mBalanceView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(MainActivityCustomer.this, TransactionActivity.class);
                    i.putExtra("accountType", accountType);
                    i.putExtra("accountnumber", accountNumber);
                    i.putExtra("accountInfo", accountInfo);
                    i.putExtra("accountBalance", accountBalance);

                    startActivity(i);

                    finish();
                }
            });&*/

            return view;
        }
    }
}
