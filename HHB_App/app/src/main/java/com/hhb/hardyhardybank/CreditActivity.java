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
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * A credit screen for users to credit their account
 */

public class CreditActivity extends Activity {
    // UI references.
    private EditText mCreditAmount;
    private TextView mDisplayBalance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        // query parse for balance variable and store to display in available_balance

        super.onCreate(savedInstanceState);
        setContentView(R.layout.credit_main);

        // Set up the credit form.
        mCreditAmount = (EditText) findViewById(R.id.credit_amount);
        mDisplayBalance = (TextView) findViewById(R.id.available_balance);


        // Activity once Sign In button is pressed
        Button mCreditButton = (Button) findViewById(R.id.credit_enter);
        mCreditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creditAccount();
            }
        });

    }

    public void creditAccount() {

    // add input variable to current balance and store in parse

    }
}
