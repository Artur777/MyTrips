package com.example.wizart.mytrips;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


import com.activeandroid.query.Select;
import com.example.wizart.mytrips.ActiveAndroidDb.Trip;
import com.example.wizart.mytrips.Parse.TripP;
import com.example.wizart.mytrips.RecyclerAdapter.DataItem;
import com.example.wizart.mytrips.RecyclerAdapter.MyRecyclerAdapter;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class TripTableActivity extends Activity {
    boolean readFromParse=true;
    boolean readFromActiveAndroid=false;
    private String TAG="TripTableActivity";
    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Trip> listOfMemos = new ArrayList<>();
    private List<DataItem> mDataItem = new ArrayList<DataItem>();
    public static Context mContext;
    ProgressBar progressBar=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memos_table);
        final Intent intent = getIntent();
        mContext = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        progressBar=(ProgressBar)findViewById(R.id.id_progress_bar_database);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Setting adapter
        mAdapter = new MyRecyclerAdapter();
        mRecyclerView.setAdapter(mAdapter);

        //Fill data for UI with database
        //LoadDataFromDatabase
//        if(readFromActiveAndroid)
            new LoadDataFromDatabase().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_memos_table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    private class LoadDataFromDatabase extends AsyncTask<Void, Integer, List<DataItem>> {
        
        private ProgressDialog progressDialog;
        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showIndicator();
        }

        @Override
        protected List<DataItem> doInBackground(Void... params) {
            //Load memos by date into list<memos> object
            if (readFromActiveAndroid) {
                Select select = new Select();

            List<Trip> trips = select.all().from(Trip.class).execute();
            // Pass item from lisOfMemos collection
            for (ListIterator<Trip> iter = trips.listIterator(); iter.hasNext(); ) {
                Trip t = iter.next();
                DataItem rowElement = new DataItem();
                rowElement.setData(t);
                mDataItem.add(rowElement);
            }
        }
        if(readFromParse) {

            ParseQueryAdapter.QueryFactory<TripP> factory = new ParseQueryAdapter.QueryFactory<TripP>() {
                public ParseQuery<TripP> create() {
                    ParseQuery<TripP> query = TripP.getQuery();
                    query.orderByDescending("createdAt");
                    query.fromLocalDatastore();
                    return query;
                }
            };

        }


            return mDataItem;

        }
/*        @Override
        protected void onProgressUpdate(Integer... progress) {
            //TODO: Implement spinner
        }*/
        @Override
        protected void onPostExecute(List<DataItem> dataItems){
            mAdapter.updateList(dataItems);
            hideIndicator();
        }
    }
    private void hideIndicator() {
        if (progressBar!= null) {
            Log.i(TAG, "Removing indicator");
            progressBar.setVisibility(View.GONE);
        }
    }

    private void showIndicator() {
        if (progressBar!= null) {
            Log.i(TAG, "Enabling indicator");
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}
