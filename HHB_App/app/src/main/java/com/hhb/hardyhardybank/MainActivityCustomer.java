package com.hhb.hardyhardybank;

import android.app.Activity;
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
    private TextView mAccountInfoView;
    private TextView mBalanceView;

    protected void onCreate(Bundle SavedInstanceState) {

        // Connect app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_main_customer);

        ParseUser currentUser = ParseUser.getCurrentUser();
        userName = currentUser.getString("username");


        mainActivityCustomerAdapter = new CustomAdapter(this);

        ListView showBalanceAndAccountType = (ListView)findViewById(R.id.listView);
        showBalanceAndAccountType.setAdapter(mainActivityCustomerAdapter);

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

        private String accountType;
        private double accountNumber;

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
        public View getItemView(ParseObject object, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.list_item, null);
            }

            // Take advantage of ParseQueryAdapter's getItemView logic
            // The IDs in your custom layout must match what ParseQueryAdapter expects
            super.getItemView(object, v, parent);

            // Set up the listView item before returning the View.
            mAccountInfoView = (TextView) v.findViewById(R.id.account_info);
            mBalanceView = (TextView) v.findViewById(R.id.account_balance);

            accountType = object.getString("accountType");
            accountNumber = object.getDouble("accountnumber");
            double balance = object.getDouble("balance");

            DecimalFormat accountNumberFormat = new DecimalFormat("#.#");
            DecimalFormat balanceFormat = new DecimalFormat("#0.00");

            mAccountInfoView.setText("HARDY " + accountType + " (" + accountNumberFormat.format(accountNumber) + ")");
            mBalanceView.setText("$" + balanceFormat.format(balance));

            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(MainActivityCustomer.this, TransactionActivity.class);
                    i.putExtra("accountType", accountType);
                    i.putExtra("accountnumber", accountNumber);

                    startActivity(i);
                    finish();
                }
            });

            return v;
        }
    }
/*
    public View getNextPageView(View v, ViewGroup parent) {
        if (v == null) {
            // R.layout.adapter_next_page contains an ImageView with a custom graphic
            // and a TextView.
            v = View.inflate(getContext(), R.layout.adapter_next_page, null);
        }
        TextView textView = (TextView) v.findViewById(R.id.nextPageTextViewId);
        textView.setText("Loaded " + getCount() + " rows. Get more!");
        return v;
    }
*/

/*
        public class MainActivityCustomerAdapter extends BaseAdapter {

            private int numOfAccounts;
            @Override
            public int getCount() {

                // Get current user
                ParseUser currentUser = ParseUser.getCurrentUser();
                String userName = currentUser.getString("username");

                //final int[] numOfAccounts = new int[1];
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
                query.whereEqualTo("userID", userName);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> accounts, ParseException e) {
                        if (e == null) {
                            //numOfAccounts[0] = accounts.size();
                            numOfAccounts = accounts.size();
                        } else {
                            Log.d("score", "Error: " + e.getMessage());
                        }
                    }
                });

                return numOfAccounts;
            }

            @Override
            public ParseObject getItem(final int arg0) {

                ParseUser currentUser = ParseUser.getCurrentUser();
                String userName = currentUser.getString("username");

                final ParseObject[] parseObject = new ParseObject[1];
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
                query.whereEqualTo("userID", userName);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> accounts, ParseException e) {
                        if (e == null) {
                            parseObject[0] = accounts.get(arg0);
                        } else {
                            Log.d("accounts", "Error: " + e.getMessage());
                        }
                    }
                });

                return parseObject[0];
            }

            @Override
            public long getItemId(int arg0) {

                return arg0;
            }

            @Override
            public View getView(final int arg0, View arg1, ViewGroup arg2) {

                ParseUser currentUser = ParseUser.getCurrentUser();
                String userName = currentUser.getString("username");

                if(arg1==null)
                {
                    LayoutInflater inflater = (LayoutInflater) MainActivityCustomer.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    arg1 = inflater.inflate(R.layout.activity_main_customer, arg2,false);
                }

                final TextView mAccountInfoView = (TextView)arg1.findViewById(R.id.account_info);
                final TextView mBalanceView = (TextView)arg1.findViewById(R.id.account_balance);

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
                query.whereEqualTo("userID", userName);
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> accounts, ParseException e) {
                        if (e == null) {
                            ParseObject parseObject = accounts.get(arg0);

                            String accountType = parseObject.getString("accountType");
                            double accountNumber = parseObject.getDouble("accountnumber");
                            double balance = parseObject.getDouble("balance");

                            DecimalFormat accountNumberFormat=new DecimalFormat("#.#");
                            DecimalFormat balanceFormat = new DecimalFormat("#0.00");

                            mAccountInfoView.setText("HARDY " + accountType + " (" + accountNumberFormat.format(accountNumber) + ")");
                            mBalanceView.setText("$" + balanceFormat.format(balance));
                        } else {
                            Log.d("accounts", "Error: " + e.getMessage());
                        }
                    }
                });

                return arg1;

            }

        } */
/*
    public List<ParseObject> getDataForListView() {

        // Get current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        String userName = currentUser.getString("username");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        query.whereEqualTo("userID", userName);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> accounts, ParseException e) {
                if (e == null) {
                    //numOfAccounts[0] = accounts.size();
                    numOfAccounts = accounts.size();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });



    }

*/
}
