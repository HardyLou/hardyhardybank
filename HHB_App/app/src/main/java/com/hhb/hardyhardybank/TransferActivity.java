package com.hhb.hardyhardybank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

public class TransferActivity extends Activity {

    private EditText mEmail;
    private EditText mTransferAmount;
    private static double cUserBalance = 0;
    private ParseObject cAccountInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        mEmail = (EditText) findViewById(R.id.transfer_amount1);
        mTransferAmount = (EditText) findViewById(R.id.transfer_amount);
        double accountBalance = 0;
        final ParseObject currentUser = ParseUser.getCurrentUser();

        Toast.makeText(getApplicationContext(), "Transfer Success!" + " " + cUserBalance, Toast.LENGTH_LONG).show();
        Button mCreditButton = (Button) findViewById(R.id.credit_enter);
        mCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final double transferAmount = Double.valueOf(mTransferAmount.getText().toString());

                ParseQuery<ParseObject> queryCurrentUser = ParseQuery.getQuery("Account");
                queryCurrentUser.whereEqualTo("userID", currentUser.getString("username"));
                queryCurrentUser.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject cAccountInfo, com.parse.ParseException e) {
                        if (cAccountInfo == null) {
                            Toast.makeText(getApplicationContext(), "Could not find Account!", Toast.LENGTH_LONG).show();
                        } else {

                            cUserBalance = cAccountInfo.getDouble("balance");

                            if(cUserBalance - transferAmount >= 0){
                                cAccountInfo.increment("balance", -1 * transferAmount);
                                cAccountInfo.saveEventually();
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
                                query.whereEqualTo("userID", mEmail.getText().toString());
                                query.getFirstInBackground(new GetCallback<ParseObject>() {
                                    public void done(ParseObject accountInfo, com.parse.ParseException e) {
                                        if (accountInfo == null) {
                                            Toast.makeText(getApplicationContext(), "Could not find Account!", Toast.LENGTH_LONG).show();
                                        } else {
                                            accountInfo.increment("balance", transferAmount);

                                            accountInfo.saveEventually();

                                            Toast.makeText(getApplicationContext(), "Transfer Success!", Toast.LENGTH_LONG).show();
                                        }

                                    }

                                });


                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Transfer Failed! Insufficient Funds!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });


            }
        });
    }
}
