package com.androidbegin.hardybankucsd;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import android.app.Application;
 
public class ParseApplication extends Application {
 
    @Override
    public void onCreate() {
        super.onCreate();
 
        // Add your initialization code here
        Parse.initialize(this, "G9sNy6cFAc2j1ZnKVGuYKhW5gHRQdUqPV3D3BOAm" ,"14vOkrgINnOVIS1fSG08tdJgIsvYi7OMlw8zTFuC");
 
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
 
        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);
 
        ParseACL.setDefaultACL(defaultACL, true);
    }
 
}