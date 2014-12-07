package com.hhb.hardyhardybank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

public class TransferUserActivity extends Activity {

//    private EditText mEmail;
    private EditText mTransferAmount;
    private static double cUserBalance = 0;
    private Spinner mAccounts;
    private Spinner mAccounts2;
    private ParseQueryAdapter<ParseObject> mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_user);

//        mEmail = (EditText) findViewById(R.id.transfer_amount1);
        mTransferAmount = (EditText) findViewById(R.id.transfer_amount);
        final ParseObject currentUser = ParseUser.getCurrentUser();

        // Set up spinners with accounts
        mainAdapter = new ParseQueryAdapter<ParseObject>(this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {
                        // Here we can configure a ParseQuery to our heart's desire.
                        ParseQuery query = new ParseQuery("Account");
                        query.whereEqualTo("userID", currentUser.getString("username"));
                        return query;
                    }
                });
        mainAdapter.setTextKey("accountnumber");
        mAccounts = (Spinner) findViewById(R.id.transfer_from_accounts);
        mAccounts.setAdapter(mainAdapter);
        mainAdapter.loadObjects();

        mAccounts2 = (Spinner) findViewById(R.id.transfer_to_accounts);
        mAccounts2.setAdapter(mainAdapter);
        mainAdapter.loadObjects();


        // When Enter button is clicked
        Button mCreditButton = (Button) findViewById(R.id.transfer_enter);
        mCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // stores transfer amount into variable transferAmount
                final double transferAmount = Double.valueOf(mTransferAmount.getText().toString());

                // queryCurrentUser holds the accounts of the logged in user
                ParseQuery<ParseObject> queryCurrentUser = ParseQuery.getQuery("Account");
                queryCurrentUser.whereEqualTo("accountnumber", mAccounts.getSelectedItem().toString());
                // Get the first account found for the user
                queryCurrentUser.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(final ParseObject cAccountInfo, com.parse.ParseException e) {
                        if (cAccountInfo == null) {
                            Toast.makeText(getApplicationContext(), "Could not find user account!", Toast.LENGTH_LONG).show();
                        } else {
                            // Stores user's balance into cUserBalance
                            cUserBalance = cAccountInfo.getDouble("balance");

                            // User has sufficient funds to transfer
                            if (cUserBalance - transferAmount >= 0) {
                                // Subtract transfer amount from user balance
                                cAccountInfo.increment("balance", -1 * transferAmount);
                                cAccountInfo.saveEventually();

                                // Find account associated with targetAccount
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
                                query.whereEqualTo("accountnumber", mAccounts2.getSelectedItem().toString());
                                query.getFirstInBackground(new GetCallback<ParseObject>() {
                                    public void done(ParseObject targetAccount, com.parse.ParseException e) {
                                        // Target account's email is not registered in database
                                        if (targetAccount == null) {
                                            Toast.makeText(getApplicationContext(), "Target account not registered!", Toast.LENGTH_LONG).show();
                                        } else {
                                            // Add transferred money to target account's balance
                                            targetAccount.increment("balance", transferAmount);
                                            targetAccount.saveEventually();
                                            Toast.makeText(getApplicationContext(), "Success! $" +
                                                            transferAmount + " was transferred to " +
                                                            targetAccount.getString("userID") + ".",
                                                    Toast.LENGTH_LONG).show();

                                            // documents the transaction
                                            // transaction log for sender
                                            ParseObject sTransaction = new ParseObject("Transaction");
                                            sTransaction.put("accountNumber", cAccountInfo.getInt("accountnumber"));
                                            sTransaction.put("action", "transfer");
                                            sTransaction.put("amount", -1 * transferAmount);
                                            sTransaction.put("resultingBalance", cAccountInfo.getDouble("balance"));
                                            sTransaction.saveEventually();
                                            // transaction log for recipient
                                            ParseObject rTransaction = new ParseObject("Transaction");
                                            rTransaction.put("accountNumber", targetAccount.getInt("accountnumber"));
                                            rTransaction.put("action", "transfer");
                                            rTransaction.put("amount", transferAmount);
                                            rTransaction.put("resultingBalance", targetAccount.getDouble("balance"));
                                            rTransaction.saveEventually();
                                        }
                                    }
                                });
                            }
                            // User does not have enough money to make the transfer
                            else {
                                Toast.makeText(getApplicationContext(), "Transfer Failed! Insufficient Funds!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });

        // Return to Main button sends user back to main menu
        Button mReturnButton = (Button) findViewById(R.id.transfer_return_main);
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(TransferUserActivity.this, MainActivityCustomer.class);
//                startActivity(i);
                finish();
            }
        });
    }
}