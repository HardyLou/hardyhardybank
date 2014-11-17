package com.hhb.hardyhardybank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.util.List;

/**
 * An account summary screen that displays the information registered on record.
 */
public class DisplayUserInfoActivity extends Activity{

    private TextView mNameView;
    private TextView mAddressView;
    private TextView mEmailView;
    private TextView mUserName;
    private TextView mRoleView;
    private TextView mAccountTypeView;
    private TextView mAccountNumberView;
    private Button mCoolButton;

    private String fullName;
    private String address;
    private String email;
    private String userName;
    private String role;
    private String accountType;
    private double accountNumber;

    protected void onCreate(Bundle savedInstanceState) {

        // Connect App with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountinfo);

        // Set up the Account Information Form
        mNameView = (TextView) findViewById(R.id.name_retrieved);
        mAddressView = (TextView) findViewById(R.id.address_retrieved);
        mEmailView = (TextView) findViewById(R.id.email_retrieved);
        mUserName = (TextView) findViewById(R.id.user_name_retrieved);
        mRoleView = (TextView) findViewById(R.id.role_retrieved);
        mAccountTypeView = (TextView) findViewById(R.id.account_type_retrieved);
        mAccountNumberView = (TextView) findViewById(R.id.account_number_retrieved);
        mCoolButton = (Button) findViewById(R.id.cool_button);

        // Retrieve user information from Parse database class -- User
        ParseUser currentUser = ParseUser.getCurrentUser();
        fullName = currentUser.getString("fullname");
        address = currentUser.getString("address");
        email = currentUser.getString("email");
        userName = currentUser.getString("username");
        role = currentUser.getString("role");

        // Retrieve user information from Parse database class -- Account
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        query.whereEqualTo("userID", userName);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject accountInfo, ParseException e) {
            if (accountInfo == null) {
                Log.d("accountInfo", "The getFirst request failed.");
            } else {
                accountType = accountInfo.getString("accountType");
                accountNumber = accountInfo.getDouble("accountnumber");
                mAccountTypeView.setText(accountType);
                DecimalFormat decimalFormat=new DecimalFormat("#.#");
                mAccountNumberView.setText(decimalFormat.format(accountNumber));
            }
            }
        });

        // Display user information
        displayAccountInfo();
    }

    public void displayAccountInfo() {
        mNameView.setText(fullName);
        mAddressView.setText(address);
        mEmailView.setText(email);
        mUserName.setText(userName);
        mRoleView.setText(role);

        mCoolButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(DisplayUserInfoActivity.this, LoginActivity.class);
                //startActivity(i);
                if (ParseUser.getCurrentUser() == null) {

                    Intent i = new Intent(DisplayUserInfoActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(DisplayUserInfoActivity.this, MainActivityUser.class);
                    startActivity(i);
                }
            }
        });
    }
}