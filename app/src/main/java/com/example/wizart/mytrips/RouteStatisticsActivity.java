package com.example.wizart.mytrips;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class RouteStatisticsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_statistics);
		TextView tvElapsedTime= (TextView)findViewById(R.id.id_stat_elapsed_time);
		TextView tvDistance = (TextView)findViewById(R.id.id_stat_Distance);
		TextView tvStartTime= (TextView)findViewById(R.id.id_stat_startTime);
		TextView tvAverageSpeed= (TextView)findViewById(R.id.id_stat_average_speed);
		String time = Integer.toString(MapsActivity.rHours)+":"+Integer.toString(MapsActivity.rMinutes)+":"+Integer.toString(MapsActivity.rSeconds);
		tvElapsedTime.setText(time);
		String dist=Double.toString(MapsActivity.routeDistance);
		tvDistance.setText(dist);
		tvStartTime.setText(MapsActivity.routeData.getsStartTime());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.route_statistics, menu);
		return true;
	}

}
