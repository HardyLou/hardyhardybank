package com.hhb.hardyhardybank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;

/**
 * Created by cell on 11/15/2014.
 */
public class CloseActivity extends Activity{

    double balance;
    String address;
    String toDelete;
    private ParseObject cAccountInfo;
    private Spinner mAccount;
    private EditText mAccountNumber;
    private String accountNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_close);



        mAccountNumber = (EditText) findViewById(R.id.account_number_close);


        // Activity once confirm button is pressed
        Button mCloseButton = (Button) findViewById(R.id.action_confirm);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Query Parse for account balance value and user's address
                final ParseObject currentUser = ParseUser.getCurrentUser();

                accountNumber = mAccountNumber.getText().toString();
                int accountValue = Integer.parseInt(accountNumber);
                ParseQuery<ParseObject> queryCurrentUser = ParseQuery.getQuery("Account");
                queryCurrentUser.whereEqualTo("accountnumber", accountValue);

                queryCurrentUser.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject cAccountInfo, com.parse.ParseException e) {
                        if (cAccountInfo == null) {
                            Toast.makeText(getApplicationContext(), "Could not find Account!", Toast.LENGTH_LONG).show();
                        } else {

                            toDelete = cAccountInfo.getObjectId();
                            try {
                                Toast.makeText(getApplicationContext(), "The remaining balance in this account"+
                                                 " will be mailed to your address ",
                                        Toast.LENGTH_LONG).show();

                                ParseObject.createWithoutData("Account", toDelete).delete();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });


            } // End of OnClickView
        }); // End of OnClickListener

    }// End of OnCreateBundle


    /* IMPLEMENTATION That was in MainActivityUser
    // String toDelete;
   // private ParseObject cAccountInfo;
     // Button to Close Account
            Button closeUserAccount = (Button) findViewById(R.id.action_close);
            closeUserAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(MainActivityUser.this, CloseActivity.class);
                //startActivity(i);


                try {
                    attemptCloseAccount();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

    }

       public void attemptCloseAccount() throws ParseException {
        ParseObject currentUser = ParseUser.getCurrentUser();
       // String accountID;
       // accountID = currentUser.getObjectId();
           ParseQuery<ParseObject> queryCurrentUser = ParseQuery.getQuery("Account");
          // queryCurrentUser.whereEqualTo("userID", currentUser.getString("username"));
           queryCurrentUser.whereEqualTo("objectId", mAccounts.getSelectedItem());

                queryCurrentUser.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject cAccountInfo, com.parse.ParseException e) {
                        if (cAccountInfo == null) {
                            Toast.makeText(getApplicationContext(), "Could not find Account!", Toast.LENGTH_LONG).show();
                        } else {

                            //cUserBalance = cAccountInfo.getDouble("balance");
                            toDelete = cAccountInfo.getObjectId();
                            try {
                                ParseObject.createWithoutData("Account", toDelete).delete();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });
         Toast.makeText(getApplicationContext(), "Account closed. The remaining balance will be mailed to your address",
                 Toast.LENGTH_LONG).show();
        //currentUser.createWithoutData("Account", accountID).delete();

    }

     */
} //End of CloseActivity class
