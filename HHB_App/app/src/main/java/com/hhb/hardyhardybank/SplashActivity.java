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
    double monthlyAverage, interest;
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
            checkInterest();
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

                // close this activity
                finish();
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

                        // documents the penalty
                        ParseObject transaction = new ParseObject("Transaction");
                        transaction.put("accountNumber", currentAccount.getInt("accountnumber"));
                        transaction.put("action", "penalty");
                        transaction.put("amount", -25 * monthsInPenalty);
                        transaction.put("resultingBalance", currentAccount.getDouble("balance"));
                        transaction.saveEventually();

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

            // saves updated information in Parse database
            currentAccount.save();
        }
    }

    // Checks average daily balance at the end of every month to determine if interest should be applied
    public void checkInterest() throws ParseException {
        // grabs all existing accounts
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Account");
        // puts all the accounts into a List
        List<ParseObject> accounts = query.find();

        // iterate through the list of accounts
        for (int i = 0; i < accounts.size(); i++) {
            ParseObject currentAccount = accounts.get(i);
            // checks if the total balance count has been updated for today's date
            if (currentAccount.getInt("updatedICounterOn") != currentDate.get(Calendar.DAY_OF_MONTH)) {
                // adds today's balance to total balance count
                currentAccount.increment("interestCounter", currentAccount.getDouble("balance"));
                // flag to show that the total balance has been updated today
                currentAccount.put("updatedICounterOn", currentDate.get(Calendar.DAY_OF_MONTH));

                // checks for interest on the last day of the month
                if (currentDate.get(Calendar.DAY_OF_MONTH) == currentDate.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    // calculates daily average balance of the account over the last month
                    monthlyAverage = currentAccount.getDouble("interestCounter") /
                                     currentDate.getActualMaximum(Calendar.DAY_OF_MONTH);

                    // for savings accounts
                    if (currentAccount.getString("accountType").equals("Saving")) {
                        // average balance was over $2000 and below $3000
                        // 4% interest
                        if (monthlyAverage > 3000) {
                            interest = 0.04 * currentAccount.getDouble("balance");
                        }

                        // average balance was over $2000 and below $3000
                        // 3% interest
                        else if (monthlyAverage > 2000 && monthlyAverage <= 3000) {
                            interest = 0.03 * currentAccount.getDouble("balance");
                        }

                        // average balance was over $1000 and below $2000
                        // 2% interest
                        else if (monthlyAverage > 1000 && monthlyAverage <= 2000) {
                            interest = 0.02 * currentAccount.getDouble("balance");
                        }

                        // average balance was not eligible for any interest
                        else {
                            interest = 0;
                        }
                    }

                    // for checking accounts
                    else if (currentAccount.getString("accountType").equals("Checking")) {
                        // average balance was over $3000
                        // 3% interest
                        if (monthlyAverage > 3000) {
                            interest = 0.03 * currentAccount.getDouble("balance");
                        }

                        // average balance was over $2000 and below $3000
                        // 2% interest
                        else if (monthlyAverage > 2000 && monthlyAverage <= 3000) {
                            interest = 0.02 * currentAccount.getDouble("balance");
                        }

                        // average balance was over $1000 and below $2000
                        // 1% interest
                        else if (monthlyAverage > 1000 && monthlyAverage <= 2000) {
                            interest = 0.01 * currentAccount.getDouble("balance");
                        }

                        // average balance was not eligible for any interest
                        else {
                            interest = 0;
                        }
                    }

                    // adds interest into account
                    currentAccount.put("balance", interest + currentAccount.getDouble("balance"));
                    // reset monthly total count
                    currentAccount.put("interestCounter", 0);

                    // documents the interest if applicable
                    if (interest > 0) {
                        ParseObject transaction = new ParseObject("Transaction");
                        transaction.put("accountNumber", currentAccount.getInt("accountnumber"));
                        transaction.put("action", "interest");
                        transaction.put("amount", interest);
                        transaction.put("resultingBalance", currentAccount.getDouble("balance"));
                        transaction.saveEventually();
                    }
                }
            }

            // saves updated information in Parse database
            currentAccount.save();
        }
    }
}