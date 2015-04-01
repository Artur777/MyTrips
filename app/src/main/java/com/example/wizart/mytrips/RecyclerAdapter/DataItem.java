package com.example.wizart.mytrips.RecyclerAdapter;

import android.graphics.Bitmap;

import com.example.wizart.mytrips.ActiveAndroidDb.Trip;
import com.example.wizart.mytrips.MapsActivity;
import com.example.wizart.mytrips.Parse.TripP;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by egutierr on 2015-03-04.
 */
public class DataItem {
    public String Distance;

    public String elapsedTime;
    public double AveragePace;
    public ArrayList<LatLng> arrayRoutePoints = null;
    public Bitmap Activity;
    public String startTime=null;
    public String endTime=null;
    public String Calories=null;
    public String Steps=null;
    public Bitmap Icon;

/*    public DataItem(String name, String address, String temperature, String humidity, String timeStamp, byte[] pictureThumbnail, byte[] picture) {
        Name = name;
        Address = address;
        Temperature = temperature;
        Humidity = humidity;
        TimeStamp = timeStamp;
        BitmapFactory.Options options = new BitmapFactory.Options();
        PictureThumbnail = BitmapFactory.decodeByteArray(pictureThumbnail, 0, pictureThumbnail.length, options); //Convert bytearray to bitmap
        Picture = BitmapFactory.decodeByteArray(picture, 0, picture.length, options); //Convert bytearray to bitmap
    };*/

    public DataItem(String distance, String elapsedTime, double averagePace, ArrayList<LatLng> arrayRoutePoints, Bitmap activity, String startTime, String endTime, String calories, String steps, Bitmap icon) {
        this.Distance = distance;
        this.elapsedTime = elapsedTime;
        this.AveragePace = averagePace;
        this.arrayRoutePoints = arrayRoutePoints;
        this.Activity = activity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.Calories = calories;
        this.Steps = steps;
        this.Icon = icon;
    }

    public DataItem(){};


    public void setData(Trip trip) {
        this.elapsedTime= trip.getElapsedTime();
        this.AveragePace= trip.getAverageSpeed();
        this.startTime= String.valueOf(trip.getStartTime());
        this.endTime= String.valueOf(trip.getEndTime());

//        this.Distance= String.valueOf(trip.getDistance());
        this.Distance= MapsActivity.formatDistance(trip.getDistance());
        this.Calories=String.valueOf(trip.getCalories());
        this.Steps=String.valueOf(trip.getSteps());

    }

    public void setData(TripP trip) {
        this.elapsedTime= trip.getElapsedTime();
        this.AveragePace= trip.getAverageSpeed();
        this.startTime= String.valueOf(trip.getStartTime());
        this.endTime= String.valueOf(trip.getEndTime());

//        this.Distance= String.valueOf(trip.getDistance());
        this.Distance= MapsActivity.formatDistance(trip.getDistance());
        this.Calories=String.valueOf(trip.getCalories());
        this.Steps=String.valueOf(trip.getSteps());

    }

}


/*
_____MET Value  x  3.5  x  _____kg body weight  ÷  200 = calories burned per minute.

 */