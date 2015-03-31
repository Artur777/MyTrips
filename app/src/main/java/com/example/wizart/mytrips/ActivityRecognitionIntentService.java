package com.example.wizart.mytrips;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

/**
 * Created by tutsberry on 17/03/15.
 */
public class ActivityRecognitionIntentService extends IntentService {

    public ActivityRecognitionIntentService() {
        super("ActivityRecognitionIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if(ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            DetectedActivity detectedActivity = result.getMostProbableActivity();

            int confidence = detectedActivity.getConfidence();
            ActivityRecEnum mostProbableName = getActivityName(detectedActivity.getType());

            Intent i = new Intent("ActivityRecognitionMessage");
            i.putExtra("activity", mostProbableName);
            i.putExtra("confidence", confidence);

            Log.d("Saquib", "mostProbableName " + mostProbableName);
            Log.d("Saquib", "Confidence : " + confidence);

            //Send Broadcast
            this.sendBroadcast(i);

        }else {
            Log.d("Saquib", "Intent had no data returned");
        }
    }

    private ActivityRecEnum getActivityName(int type) {
        switch (type)
        {
            case DetectedActivity.IN_VEHICLE:
                return ActivityRecEnum.IN_VEHICLE;
            case DetectedActivity.ON_BICYCLE:
                return ActivityRecEnum.ON_BICYCLE;
            case DetectedActivity.WALKING:
                return ActivityRecEnum.WALKING;
            case DetectedActivity.STILL:
                return ActivityRecEnum.STILL;
            case DetectedActivity.TILTING:
                return ActivityRecEnum.TILTING;
            case DetectedActivity.UNKNOWN:
                return ActivityRecEnum.UNKNOWN;
            case DetectedActivity.RUNNING:
                return ActivityRecEnum.RUNNING;

        }
        return ActivityRecEnum.UNKNOWN;
    }
}