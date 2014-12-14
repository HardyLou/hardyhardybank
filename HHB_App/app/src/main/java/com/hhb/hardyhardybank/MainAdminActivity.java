package com.hhb.hardyhardybank;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.DecimalFormat;

/**
 * Landing page from login; menu access to other functionality for Admins
 */
public class MainAdminActivity extends Activity {
        private String userName;

        // Adapter for the Todos Parse Query
        private ParseQueryAdapter<ParseObject> mainActivityAdminAdapter;

        private LayoutInflater inflater;

        private Button mLogoutButton;
        private ListView lv;

        private String accountType;
        private String fullName;

        private int accountNumber;
        private double balance;

    protected void onCreate(Bundle SavedInstanceState) {

        // Connect app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_main_admin);

        ParseUser currentUser = ParseUser.getCurrentUser();
        userName = currentUser.getString("username");

        ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery<ParseObject> create() {
                ParseQuery<ParseObject> query = new ParseQuery("Account");
                query.orderByDescending("CreatedAt");
                return query;
            }
        };

        inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainActivityAdminAdapter = new AdminCustomAdapter(this, factory);

        // Attach the query adapter to the view
        lv = (ListView)findViewById(R.id.all_accounts_list_view);
        lv.setAdapter(mainActivityAdminAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ParseObject object = mainActivityAdminAdapter.getItem(position);
                Intent i = new Intent(MainAdminActivity.this, AccountAdminActivity.class);


                String userName = object.getString("userID");

                accountType = object.getString("accountType");
                accountNumber = object.getInt("accountnumber");
                balance = object.getDouble("balance");

                DecimalFormat accountNumberFormat = new DecimalFormat("#.#");
                DecimalFormat balanceFormat = new DecimalFormat("#0.00");

                final String accountInfo = userName + "'s HARDY " + accountType + " (" + accountNumberFormat.format(accountNumber) + ")";
                final String accountBalance = "$" + balanceFormat.format(balance);

                i.putExtra("accountInfo", accountInfo);
                i.putExtra("accountBalance", accountBalance);
                i.putExtra("accountnumber", accountNumber);

                startActivity(i);

                finish();
            }
        });


        // Button to log out user
        mLogoutButton = (Button) findViewById(R.id.action_logout_admin);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                currentUser.logOut();

                Toast.makeText(getApplicationContext(), "You have been logged out",
                        Toast.LENGTH_LONG).show();

                // Go to Login Page
                Intent i = new Intent(MainAdminActivity.this, LoginActivity.class);
                startActivity(i);

                // Close this activity
                finish();
            }
        });
    }

    private class AdminCustomAdapter extends ParseQueryAdapter<ParseObject> {

        TextView mAccountInfoView;
        TextView mBalanceView;

        public AdminCustomAdapter(Context context,
                             ParseQueryAdapter.QueryFactory<ParseObject> queryFactory) {
            super(context, queryFactory);
        }



        @Override
        public View getItemView(final ParseObject object, View view, ViewGroup parent) {

            if (view == null) {
                view = inflater.inflate(R.layout.admin_list_item, parent, false);
            }

            // Take advantage of ParseQueryAdapter's getItemView logic
            // The IDs in your custom layout must match what ParseQueryAdapter expects
            super.getItemView(object, view, parent);

            // Set up the listView item before returning the View.
            mAccountInfoView = (TextView) view.findViewById(R.id.admin_item1);
            mBalanceView = (TextView) view.findViewById(R.id.admin_item2);

            String userName = object.getString("userID");

            String accountType = object.getString("accountType");
            double accountNumber = object.getDouble("accountnumber");
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
