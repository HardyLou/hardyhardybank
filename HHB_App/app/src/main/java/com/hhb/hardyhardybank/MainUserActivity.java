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
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.DecimalFormat;

/**
 * Landing page from login; menu access to other functionality for Users
 */
public class MainUserActivity extends Activity {

    private String userName;

    // Adapter for the Todos Parse Query
    private ParseQueryAdapter<ParseObject> mainActivityCustomerAdapter;

    private LayoutInflater inflater;

    private ListView lv;

    private String accountType;

    private ParseUser currentUser;

    private int accountNumber;
    private double balance;

    protected void onCreate(Bundle SavedInstanceState) {

        // Connect app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_main_user);

        currentUser = ParseUser.getCurrentUser();
        userName = currentUser.getString("username");

        ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                ParseQuery<ParseObject> query = new ParseQuery("Account");
                query.whereEqualTo("userID", userName);
                try {
                    if (query.count() == 0) {
                        Toast.makeText(getApplicationContext(), "User has been closed.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(MainUserActivity.this, LoginActivity.class);

                        startActivity(i);
                        finish();

                    }

                } catch (ParseException e2) {
                    e2.printStackTrace();
                }


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
                // if an account is clicked, go to AccountUserActivity
                Intent i = new Intent(MainUserActivity.this, AccountUserActivity.class);

                // bundles all the account information needed in the next screen(s)
                accountType = object.getString("accountType");
                accountNumber = object.getInt("accountnumber");
                balance = object.getDouble("balance");

                DecimalFormat accountNumberFormat = new DecimalFormat("#.#");
                DecimalFormat balanceFormat = new DecimalFormat("#0.00");

                final String accountInfo = currentUser.getString("fullname") + "'s HARDY " + accountType
                                            + " (" + accountNumberFormat.format(accountNumber) + ")";
                final String accountBalance = "$" + balanceFormat.format(balance);

                i.putExtra("accountInfo", accountInfo);
                i.putExtra("accountBalance", accountBalance);
                i.putExtra("accountnumber", accountNumber);

                startActivity(i);

                finish();

            }
        });


        // Button to Log Out Account
        Button mLogOutButton = (Button) findViewById(R.id.action_logout);
        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentUser.logOut();

                Toast.makeText(getApplicationContext(), "You have been logged out",
                        Toast.LENGTH_LONG).show();


                // go to AddAccountActivity
                Intent i = new Intent(MainUserActivity.this, LoginActivity.class);
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
                Intent i = new Intent(MainUserActivity.this, AddAccountActivity.class);
                startActivity(i);

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
                view = inflater.inflate(R.layout.customer_list_item, parent, false);
            }

            // Take advantage of ParseQueryAdapter's getItemView logic
            // The IDs in your custom layout must match what ParseQueryAdapter expects
            super.getItemView(object, view, parent);

            // Set up the listView item before returning the View.
            mAccountInfoView = (TextView) view.findViewById(R.id.customer_item1);
            mBalanceView = (TextView) view.findViewById(R.id.customer_item2);

            String accountType = object.getString("accountType");
            int accountNumber = object.getInt("accountnumber");
            double balance = object.getDouble("balance");

            DecimalFormat accountNumberFormat = new DecimalFormat("#.#");
            DecimalFormat balanceFormat = new DecimalFormat("#0.00");

            final String accountInfo = userName + "'s HARDY " + accountType + " (" + accountNumberFormat.format(accountNumber) + ")";
            final String accountBalance = "$" + balanceFormat.format(balance);

            mAccountInfoView.setText(accountInfo);
            mBalanceView.setText(accountBalance);

            return view;
        }
    }
}
