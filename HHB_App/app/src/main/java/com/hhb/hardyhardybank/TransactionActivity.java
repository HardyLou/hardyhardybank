package com.hhb.hardyhardybank;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Xiaohan on 12/3/14.
 * List all the transaction history for one specific account
 */
public class TransactionActivity extends Activity {

    protected void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_transaction);


        Bundle bundle = getIntent().getExtras();
        String accountNumber = " " + bundle.getDouble("accountnumber");

        TextView mAccountInfoView = (TextView) findViewById(R.id.transactions);
        mAccountInfoView.setText(accountNumber);


    }


}
