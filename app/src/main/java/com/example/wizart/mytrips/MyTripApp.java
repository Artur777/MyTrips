package com.example.wizart.mytrips;


import com.activeandroid.ActiveAndroid;
import com.example.wizart.mytrips.Parse.TripP;
import com.example.wizart.mytrips.RecyclerAdapter.DataItem;
import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;


public class MyTripApp extends com.activeandroid.app.Application {
    private String TAG="EspotelAndroidDemo";
//    public DaoSession daoSession;

    public static final int EDIT_ACTIVITY_RC = 1;
    private static final String CLASS_NAME = MyTripApp.class.getSimpleName();
    List<DataItem> itemList;



    @Override
    public void onCreate() {
        super.onCreate();
        // Read from properties file.
        initParse();
        ActiveAndroid.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());

//        setupDatabase();
    }

    private void initParse() {
        Parse.enableLocalDatastore(getApplicationContext());
        ParseObject.registerSubclass(TripP.class);
        Parse.initialize(this, "U0jfSztKvO7pUn4iqXUI5vtY2lnqN6Fxr207CjjR", "JUKW3HFrNX1pqMAKRzgN47mSvRea4IZijHnTzsHx");
        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);


    }



/*
    public void setupDatabase() {
         DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "BLEDemo-db", null);
         SQLiteDatabase db = helper.getWritableDatabase();
         DaoMaster daoMaster = new DaoMaster(db);
         daoSession = daoMaster.newSession();
         */
/*long max_size = db.getMaximumSize();
         Log.d(TAG, "setupDataBase, maximum size = " + max_size);*//*

    }

    public DaoSession getDaoSession() {
       return daoSession;
    }
*/

}