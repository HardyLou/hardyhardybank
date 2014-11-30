package com.hhb.hardyhardybank;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;
import android.content.Intent;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Calendar;
import java.util.List;

public class SplashActivity extends Activity {
    int daysInPenalty, monthsInPenalty;
    // today's date
    Calendar currentDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Connects app with Parse
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm", "14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Makes sure all accounts are up to date
        try {
            checkPenalty();
        } catch (ParseException e2){
            Toast.makeText(getApplicationContext(), "ERROR",
                    Toast.LENGTH_LONG).show();
        }

        // Sets timer for Splash Screen
        int myTimer = 3000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(i);
                finish(); // close this activity
            }
        }, myTimer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Makes sure all accounts that have had a balance under $100 for 30 days are penalized $25.
    public void checkPenalty() throws ParseException {
        // grabs all existing accounts
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        // puts all the accounts into a List
        List<ParseObject> accounts = query.find();

        // iterate through the list of accounts
        for (int i = 0; i < accounts.size(); i++) {
            ParseObject currentAccount = accounts.get(i);
            daysInPenalty = 0;

            if ((currentAccount.get("inPenalty") == null) || (currentAccount.get("pDateCounter") == null)) {
                currentAccount.put("inPenalty", true);
                currentAccount.put("pDateCounter", currentDate.getTime());
            }

            // if balance is less than $100
            if (currentAccount.getDouble("balance") < 100) {
                // holds the date that the account was first under $100
                Calendar startDate = Calendar.getInstance();
                startDate.setTime(currentAccount.getDate("pDateCounter"));

                // account has already been in penalty
                if (currentAccount.getBoolean("inPenalty")) {
                    // created to count number of days elapsed
                    Calendar tempStartDate = Calendar.getInstance();
                    tempStartDate.setTime(currentAccount.getDate("pDateCounter"));

                    // finds the days elapsed since the account's balance was first under $100
                    while(tempStartDate.before(currentDate)) {
                        tempStartDate.add(Calendar.DAY_OF_MONTH, 1);
                        daysInPenalty++;
                    }

                    // account has been in penalty for more than 30 days; else, do nothing
                    if (daysInPenalty > 30) {
                        // under the circumstances that no one has opened the app in months
                        monthsInPenalty = daysInPenalty / 30;
                        startDate.add(Calendar.DATE, 30 * monthsInPenalty);
                        // subtract $25 from account balance
                        currentAccount.put("balance", currentAccount.getDouble("balance") - 25 * monthsInPenalty);
                        // update penalty date counter
                        currentAccount.put("pDateCounter", startDate.getTime());
                    }
                }

                // balance previously was NOT in penalty range
                else {
                    currentAccount.put("inPenalty", true);
                    currentAccount.put("pDateCounter", currentDate.getTime());
                }
            }

            // if balance is $100 or more
            else {
                currentAccount.put("inPenalty", false);
                currentAccount.put("pDateCounter", currentDate.getTime());

            }
            currentAccount.save();
        }
    }
}
