package com.hhb.hardyhardybank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


/**
 * Transfer funds between users
 */
public class TransferUserActivity extends Activity {

    private EditText mEmail;
    private EditText mTransferAmount;
    private String input_email;
    private String input_amount;

    private double cUserBalance;
    Bundle bundle;

    private int accountNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_user);

        bundle = getIntent().getExtras();
        accountNumber = bundle.getInt("accountnumber");
        mEmail = (EditText) findViewById(R.id.transfer_amount1);
        mTransferAmount = (EditText) findViewById(R.id.transfer_amount);

        final ParseObject currentUser = ParseUser.getCurrentUser();

        // When Enter button is clicked
        Button mCreditButton = (Button) findViewById(R.id.credit_enter);
        mCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input_email = mEmail.getText().toString();
                input_amount = mTransferAmount.getText().toString();

                if (TextUtils.isEmpty(input_email))
                {
                    mEmail.setError(getString(R.string.error_field_required));
                    View focusView = mEmail;
                    focusView.requestFocus();
                }

                if (TextUtils.isEmpty(input_amount))
                {
                    mTransferAmount.setError(getString(R.string.error_field_required));
                    View focusView = mTransferAmount;
                    focusView.requestFocus();
                }

                else {
                    mEmail.setError(null);
                    mTransferAmount.setError(null);

                    // stores transfer amount into variable transferAmount
                    final double transferAmount = Double.valueOf(mTransferAmount.getText().toString());

                    // queryCurrentUser holds the accounts of the logged in user
                    ParseQuery<ParseObject> queryCurrentUser = ParseQuery.getQuery("Account");
                    queryCurrentUser.whereEqualTo("userID", currentUser.getString("username"));
                    queryCurrentUser.whereEqualTo("accountnumber", accountNumber);
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
                                    // Find account associated with input email and assign to targetAccount
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
                                    query.whereEqualTo("userEmail", mEmail.getText().toString());
                                    query.orderByDescending("createdAt");
                                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                                        public void done(ParseObject targetAccount, com.parse.ParseException e) {
                                            // Target account's email is not registered in database
                                            if (targetAccount == null) {
                                                Toast.makeText(getApplicationContext(), "Target email not registered!", Toast.LENGTH_LONG).show();
                                            } else {

                                                // Subtract transfer amount from user balance
                                                cAccountInfo.increment("balance", -1 * transferAmount);
                                                cAccountInfo.saveEventually();

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
            }
        });

        // Back button sends user back to account menu
        Button mReturnButton = (Button) findViewById(R.id.action_return);
        mReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TransferUserActivity.this, AccountUserActivity.class);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });
    }
}
