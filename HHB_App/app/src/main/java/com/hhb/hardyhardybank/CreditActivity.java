package com.hhb.hardyhardybank;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * A credit screen for users to credit their account
 */

public class CreditActivity extends Activity {
    // UI references.
    private EditText mCreditAmount;
    private TextView mDisplayBalance;
    double credit_amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        // query parse for balance variable and store to display in available_balance

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        // Set up the credit form.
        mCreditAmount = (EditText) findViewById(R.id.credit_amount);
        mDisplayBalance = (TextView) findViewById(R.id.available_balance);

        final ParseObject user_obj = new ParseObject("User");
        double balance = user_obj.getDouble("balance");

        DecimalFormat format = new DecimalFormat("#0.00");
        String formatted_balance = format.format(balance);
        mDisplayBalance.setText("$" + formatted_balance);
        //mDisplayBalance.setText("$" + balance);

        // Activity once Sign In button is pressed
        Button mCreditButton = (Button) findViewById(R.id.credit_enter);
        mCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_amount =  Double.valueOf(mCreditAmount.getText().toString());
                user_obj.increment("balance", credit_amount);

                // TODO: Follow DRY
                DecimalFormat format = new DecimalFormat("##.00");
                String formatted_balance = format.format(user_obj.getDouble("balance"));
                mDisplayBalance.setText("$" + formatted_balance);
                //mDisplayBalance.setText("$" + user_obj.getDouble("balance"));
            }
        });

    }

    public void creditAccount() {
    // add input variable to current balance and store in parse

    }
}
