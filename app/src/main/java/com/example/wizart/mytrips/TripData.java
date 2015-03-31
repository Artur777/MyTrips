package com.example.wizart.mytrips;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.maps.model.LatLng;

import android.text.format.Time;

public class TripData implements Serializable {
	public TripData(double distance, String sDistance, String elapsedTime,
			double averagePace, String sAveragePAce, int carories,
			String sCalories, ArrayList<LatLng> arrayRoutePoints, int steps,
			Date startTime, Date endTime, String sEndTime, String sStartTime,
			int activity, double velocity, double sVelocity,
			double avarageVelocity, double sAvarageVelocity) {
		super();
		Distance = distance;
		this.sDistance = sDistance;
		this.elapsedTime = elapsedTime;
		AveragePace = averagePace;
		this.sAveragePAce = sAveragePAce;
		Carories = carories;
		this.sCalories = sCalories;
		this.arrayRoutePoints = arrayRoutePoints;
		this.steps = steps;
		this.startTime = startTime;
		this.endTime = endTime;
		this.sEndTime = sEndTime;
		this.sStartTime = sStartTime;
		this.activity = activity;
		Velocity = velocity;
		this.sVelocity = sVelocity;
		AvarageVelocity = avarageVelocity;
		this.sAvarageVelocity = sAvarageVelocity;
	}

	public double getDistance() {
		return Distance;
	}

	public void setDistance(double distance) {
		Distance = distance;
	}

	public String getsDistance() {
		return sDistance;
	}

	public void setsDistance(String sDistance) {
		this.sDistance = sDistance;
	}

	public String getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(String elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public double getAveragePace() {
		return AveragePace;
	}

	public void setAveragePace(double averagePace) {
		AveragePace = averagePace;
	}

	public String getsAveragePAce() {
		return sAveragePAce;
	}

	public void setsAveragePAce(String sAveragePAce) {
		this.sAveragePAce = sAveragePAce;
	}

	public int getCarories() {
		return Carories;
	}

	public void setCarories(int carories) {
		Carories = carories;
	}

	public String getsCalories() {
		return sCalories;
	}

	public void setsCalories(String sCalories) {
		this.sCalories = sCalories;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getsEndTime() {
		return sEndTime;
	}

	public void setsEndTime(String sEndTime) {
		this.sEndTime = sEndTime;
	}

	public String getsStartTime() {
		return sStartTime;
	}

	public void setsStartTime(Date sStartTime) {
		this.sStartTime = sStartTime.toString();
	}

	public int getActivity() {
		return activity;
	}

	public void setActivity(int activity) {
		this.activity = activity;
	}

	public double getVelocity() {
		return Velocity;
	}

	public void setVelocity(double velocity) {
		Velocity = velocity;
	}

	public double getsVelocity() {
		return sVelocity;
	}

	public void setsVelocity(double sVelocity) {
		this.sVelocity = sVelocity;
	}

	public double getAvarageVelocity() {
		return AvarageVelocity;
	}

	public void setAvarageVelocity(double avarageVelocity) {
		AvarageVelocity = avarageVelocity;
	}

	public double getsAvarageVelocity() {
		return sAvarageVelocity;
	}

	public void setsAvarageVelocity(double sAvarageVelocity) {
		this.sAvarageVelocity = sAvarageVelocity;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 6117597584326677201L;
	private double Distance=0;
	private String sDistance="";
	private String elapsedTime=null;
	private double AveragePace=0;
	private  String sAveragePAce="";
	private int Carories=0;
	private  String sCalories="";
	private ArrayList<LatLng> arrayRoutePoints = null;
	private int steps=0;
	private Date startTime=null;
	private Date endTime=null;
	private String sEndTime="";
	private  String sStartTime="";	
	private int activity=0;
	private double Velocity=0;
	private double sVelocity=0;
	private double AvarageVelocity=0;
	private  double sAvarageVelocity=0;
	private int icon;
	
	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public TripData(Date time){
		arrayRoutePoints = new ArrayList<LatLng>();
		activity=DetectedActivity.ON_FOOT;		
		startTime=time;
	}
	
	public ArrayList<LatLng> getArrayRoutePoints() {
		return arrayRoutePoints;
	}
	public void setArrayRoutePoints(ArrayList<LatLng> arrayRoutePoints) {
		this.arrayRoutePoints = arrayRoutePoints;
	}
	
}
