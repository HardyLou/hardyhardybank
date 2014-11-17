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

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.DecimalFormat;

/**
 * Created by cell on 11/15/2014.
 */
public class CloseActivity extends Activity{
    // UI references.
    //private EditText mCreditAmount;
    //private TextView mDisplayBalance;

    double balance;// credit_amount;
    String address;
    private Spinner mAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user); //???????what is this?

        // Activity once Close button is pressed
        Button mCloseButton = (Button) findViewById(R.id.action_close);
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Query Parse for account balance value and user's address
                ParseObject currentUser = ParseUser.getCurrentUser();
                balance = currentUser.getDouble("balance");
                address = currentUser.getString("address");

                mAccount = (Spinner) findViewById(R.id.accounts_label);
                String toDelete = mAccount.getSelectedItem().toString(); // get account selected in spinner
                //ParseObject accountUser = currentUser.getObjectId();
                ParseObject.createWithoutData("Account", toDelete).deleteEventually();
              /*  try {
                    accountUser.deleteEventually();
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/
                // delete current user

               /* // Adds inputted deposit amount to current balance
                credit_amount =  Double.valueOf(mCreditAmount.getText().toString());
                currentUser.increment("balance", credit_amount);
                currentUser.saveEventually();*/


               // TODO: Follow DRY
                // Update the displayed balance to reflect new account balance
                DecimalFormat format = new DecimalFormat("#0.00");
                //String formatted_balance = format.format(currentUser.getDouble("balance"));
               // mDisplayBalance.setText("$" + formatted_balance);*/



                // Notifies user of successful deposit // NEED TO NOTIFY USER THAT REMAINING BALANCE WILL BE MAILED TO ADDRESS
                Toast.makeText(getApplicationContext(), "The remaining " + format.format(balance)
                                + " will be mailed to this address " + address,
                        Toast.LENGTH_LONG).show();
            }
        });
        // return to main button
       /* Button mCreditReturn = (Button) findViewById(R.id.credit_return_main);
        mCreditReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Main Activity
                Intent i = new Intent(CloseActivity.this, MainActivity.class);
                startActivity(i);

                // Close this activity
                finish();
            }
        });*/
    }
}
