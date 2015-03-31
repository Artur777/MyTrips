package drawable.androidespoteldemo.ble;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.androidespoteldemo.Database.greendaoRepository;
import com.example.android.androidespoteldemo.R;
import com.example.android.androidespoteldemo.RecyclerAdapter.DataItem;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import Memos.Memo;
import Memos.MemoDao;

import static com.example.android.androidespoteldemo.Utils.stringToDate;


public class GraphViewActivity extends Activity {

    private List<Memo> listOfMemos = new ArrayList<>();
    private List<DataItem> mDataItem = new ArrayList<>();
    private List<String> listDate= new ArrayList<>();
    private List<String> listTemperature= new ArrayList<>();
    private List<String> listHumidity= new ArrayList<>();
    int sizeTable=0;


    public static Context mContext;
    ProgressBar progressBar=null;
    private String TAG="GraphViewActivity ";
    GraphView graphT;
    GraphView graphH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
//        graphT=new GraphView(mContext);
//        graphH=new GraphView(mContext);

        setContentView(R.layout.activity_graph_data_cloud);

        graphT = (GraphView) findViewById(R.id.graph1);
        graphH = (GraphView) findViewById(R.id.graph2);




//        showIndicator();
//        loadData();
//        showGraphs();
//        hideIndicator();
        new LoadDataFromDatabase().execute();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.graph_data_cloud, menu);
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



    private class LoadDataFromDatabase extends AsyncTask<Void, Integer, List<Memo>> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showIndicator();
        }

        @Override
        protected List<Memo> doInBackground(Void... params) {
            //Load memos by date into list<memos> object
            listOfMemos = greendaoRepository.getMemoDao(mContext)
                    .queryBuilder()
                    .orderDesc(MemoDao.Properties.Date).list();
            sizeTable=listOfMemos.size();

            // Pass item from lisOfMemos collection
            for (ListIterator<Memo> iter = listOfMemos.listIterator();iter.hasNext();) {
                Memo a= iter.next();
                listDate.add(a.getDate());
                String temp=a.getTemperature();
                temp=temp.substring(0,temp.length()-2);
                temp= temp.replaceAll(",",".");
                listTemperature.add(temp);

                String hum =a.getHumidity();
                hum=hum.substring(0,hum.length()-3);
                hum= hum.replaceAll(",",".");
                listHumidity.add(hum);
            }
            return listOfMemos;
        }
        /*        @Override
                protected void onProgressUpdate(Integer... progress) {
                    //TODO: Implement spinner
                }*/
        @Override
        protected void onPostExecute(List<Memo> dataItems){

            DataPoint[] dataT = new DataPoint[sizeTable];
            for (int i=0; i<sizeTable; i++) {

                Double doubleTemp=Double.parseDouble(listTemperature.get(i));
                Date d= (Date) stringToDate(listDate.get(i));
                dataT[i] = new DataPoint(i, doubleTemp);
            }


            LineGraphSeries<DataPoint> seriesT = new LineGraphSeries<>(dataT);

//            PointsGraphSeries<DataPoint> seriesT = new PointsGraphSeries<DataPoint>(dataT);
            graphT.addSeries(seriesT);
            seriesT.setColor(Color.RED);

            DataPoint[] dataH = new DataPoint[sizeTable];
            for (int i=0; i<sizeTable; i++) {
                Double doubleHum=Double.parseDouble(listHumidity.get(i));
                Date d= (Date) stringToDate(listDate.get(i));
                dataH[i] = new DataPoint(i, doubleHum);
            }


            LineGraphSeries<DataPoint> seriesH = new LineGraphSeries<DataPoint>(dataH);
            graphH.addSeries(seriesH);
            seriesH.setColor(Color.GREEN);

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



    public void loadData() {

        listOfMemos = greendaoRepository.getMemoDao(mContext)
                .queryBuilder()
                .orderDesc(MemoDao.Properties.Date).list();
        sizeTable=listOfMemos.size();

        // Pass item from lisOfMemos collection
        for (ListIterator<Memo> iter = listOfMemos.listIterator();iter.hasNext();) {
            Memo a= iter.next();
            listDate.add(a.getDate());
            String temp=a.getTemperature();
            temp=temp.substring(0,temp.length()-2);
            temp= temp.replaceAll(",",".");
            listTemperature.add(temp);

            String hum =a.getHumidity();
            hum=hum.substring(0,hum.length()-3);
            hum= hum.replaceAll(",",".");
            listHumidity.add(hum);
        }
    }


    public void showGraphs() {

        DataPoint[] dataT = new DataPoint[sizeTable];
        for (int i=0; i<sizeTable; i++) {

            Double doubleTemp=Double.parseDouble(listTemperature.get(i));
            Date d= stringToDate(listDate.get(i));
            dataT[i] = new DataPoint(i, doubleTemp);
        }


        LineGraphSeries<DataPoint> seriesT = new LineGraphSeries<>(dataT);

//            PointsGraphSeries<DataPoint> seriesT = new PointsGraphSeries<DataPoint>(dataT);
        graphT.addSeries(seriesT);
        seriesT.setColor(Color.RED);



//        StaticLabelsFormatter staticLabelsFormatterT = new StaticLabelsFormatter(graphT);
//        staticLabelsFormatterT.setHorizontalLabels(new String[] {"0","Time"});
//        staticLabelsFormatterT.setVerticalLabels(new String[] {"0","Temp"});
//        graphT.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatterT);


        DataPoint[] dataH = new DataPoint[sizeTable];
        for (int i=0; i<sizeTable; i++) {
            Double doubleHum=Double.parseDouble(listHumidity.get(i));
            Date d= stringToDate(listDate.get(i));
            dataH[i] = new DataPoint(i, doubleHum);
        }


        LineGraphSeries<DataPoint> seriesH = new LineGraphSeries<>(dataH);
        graphH.addSeries(seriesH);
        seriesH.setColor(Color.GREEN);


//        StaticLabelsFormatter staticLabelsFormatterH = new StaticLabelsFormatter(graphT);
//        staticLabelsFormatterH.setHorizontalLabels(new String[] {"0","Time"});
//        staticLabelsFormatterH.setVerticalLabels(new String[] {"0","Hum"});
//        graphH.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatterH);

        graphT.getLegendRenderer().setVisible(true);
        graphT.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        graphH.getLegendRenderer().setVisible(true);
        graphH.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        seriesT.setTitle("Temperature");
        seriesH.setTitle("Humidity");



//        hideIndicator();
    }

}
