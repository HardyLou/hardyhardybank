package com.example.b.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;

public class MyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Parse.initialize(this, "lfGdLfCAS8VzoDc6OVxCv6XgCTQb0hrb5puTTbkg", "BDDSrZjiTMNIFRDXf32devEnSCSNS1V3pxuJOXhg");
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("username", "password");
        testObject.saveInBackground();
    }

//    public void buttonOnClick(View v){
//
//        Button button = (Button) V;
//        startActivity(new Intent(getApplicationContext(), create_account.class));
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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
}
