package com.hhb.hardyhardybank;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Xiaohan on 11/30/14.
 * Landing page from login; menu access to other functionality for Users
 */
public class MainActivityCustomer extends Activity {

    private String userName;
    private CustomAdapter mainActivityCustomerAdapter;
    private ListView lv;


    protected void onCreate(Bundle SavedInstanceState) {

        // Connect app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_main_customer);

        ParseUser currentUser = ParseUser.getCurrentUser();
        userName = currentUser.getString("username");

        ListView lv = (ListView)findViewById(R.id.listView);
        mainActivityCustomerAdapter = new CustomAdapter(this);
        lv.setAdapter(mainActivityCustomerAdapter);

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


    public class CustomAdapter extends ParseQueryAdapter<ParseObject> {

        Context mContext;
        TextView mAccountInfoView;
        TextView mBalanceView;
        String accountType;
        double accountNumber;

        public CustomAdapter(Context context) {
            // Use the QueryFactory to construct a PQA
            super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
                public ParseQuery create() {
                    ParseQuery query = new ParseQuery("Account");
                    query.whereEqualTo("userID", userName);
                    return query;
                }
            });

        }

        @Override
        public View getItemView(ParseObject object, View view, ViewGroup parent) {

            if (view == null) {
                view = View.inflate(getContext(),R.layout.list_item, null);
            }

            // Take advantage of ParseQueryAdapter's getItemView logic
            // The IDs in your custom layout must match what ParseQueryAdapter expects
            super.getItemView(object, view, parent);

            // Set up the listView item before returning the View.
            mAccountInfoView = (TextView) view.findViewById(R.id.account_info);
            mBalanceView = (TextView) view.findViewById(R.id.account_balance);


            accountType = object.getString("accountType");
            accountNumber = object.getDouble("accountnumber");
            double balance = object.getDouble("balance");

            DecimalFormat accountNumberFormat = new DecimalFormat("#.#");
            DecimalFormat balanceFormat = new DecimalFormat("#0.00");

            mAccountInfoView.setText("HARDY " + accountType + " (" + accountNumberFormat.format(accountNumber) + ")");
            mBalanceView.setText("$" + balanceFormat.format(balance));

            mAccountInfoView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(MainActivityCustomer.this, TransactionActivity.class);
                    i.putExtra("accountType", accountType);
                    i.putExtra("accountnumber", accountNumber);

                    startActivity(i);

                    finish();
                }
            });

            mBalanceView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(MainActivityCustomer.this, TransactionActivity.class);
                    i.putExtra("accountType", accountType);
                    i.putExtra("accountnumber", accountNumber);

                    startActivity(i);

                    finish();
                }
            });

            return view;
        }
    }



}
