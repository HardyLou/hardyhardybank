package com.hhb.hardyhardybank;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.DecimalFormat;

/**
 * Created by Xiaohan on 12/5/14.
 * Landing page from login; menu access to other functionality for Admins
 */
public class MainActivityAdmin extends Activity {
        private String userName;

        // Adapter for the Todos Parse Query
        private ParseQueryAdapter<ParseObject> mainActivityAdminAdapter;

        private LayoutInflater inflater;

        private ListView lv;

        private String accountType;


        private double accountNumber;
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
                //query.whereEqualTo("userID", userName);
                return query;
            }
        };


        inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainActivityAdminAdapter = new ParseQueryAdapter<ParseObject>(this, factory);

        // Attach the query adapter to the view
        lv = (ListView)findViewById(R.id.all_accounts_list_view);
        lv.setAdapter(mainActivityAdminAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ParseObject object = mainActivityAdminAdapter.getItem(position);
                Intent i = new Intent(MainActivityAdmin.this, TransactionActivity.class);
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

    }



}
