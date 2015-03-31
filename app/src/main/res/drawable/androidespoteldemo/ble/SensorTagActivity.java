package drawable.androidespoteldemo.ble;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.androidespoteldemo.BLEDemo;
import com.example.android.androidespoteldemo.Bluemix.BluemixDao;
import com.example.android.androidespoteldemo.Bluemix.DemoItem;
import com.example.android.androidespoteldemo.Bluemix.onDaoBluemixSuccessFaillListener;
import com.example.android.androidespoteldemo.Database.greendaoRepository;
import com.example.android.androidespoteldemo.R;
import com.example.android.androidespoteldemo.Utils;
import com.ibm.mobile.services.data.IBMDataFile;
import com.ibm.mobile.services.data.IBMDataFileException;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Memos.Memo;

public class SensorTagActivity extends Activity {
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    private IntentFilter mIntentTFilter = null;
    static final int REQUEST_TAKE_PHOTO_BIG = 1;

    private TextView tvTemperature;
    private TextView tvHumidity;
    private TextView tvBarometer;
    private TextView tvDeviceName;
    private ImageView ivBarometer;
    private ImageView mImageView;
    private String mDeviceName;
    private String mDeviceAddress;
    private boolean isSensorTag = false;
    private boolean isVaisalaMeter = false;
    private Bitmap imageBitmapPreview = null;
    public String mCurrentPhotoPath;
    public Context context;
    public Context mContext;
    private boolean isPictureTaken= false;
    private List<Memo> listOfMemos;
    private Bitmap mPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_tag);
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
        if (mDeviceName.equals("SensorTag")) {
            Log.i(TAG, "SensorTag detected");
            isSensorTag = true;
            isVaisalaMeter = false;
        } else if (mDeviceName.startsWith("HM")) {
            Log.i(TAG, "Vaisala meter detected");
            isSensorTag = false;
            isVaisalaMeter = true;
        }

        tvTemperature = (TextView) findViewById(R.id.id_text_temperature);
        tvHumidity = (TextView) findViewById(R.id.id_text_humidity);
        tvBarometer = (TextView) findViewById(R.id.id_text_pressure);
        tvDeviceName = (TextView) findViewById(R.id.id_device_name);
        ivBarometer = (ImageView) findViewById(R.id.id_barometer_image);
        mImageView = (ImageView) findViewById(R.id.id_photo_preview);

        if (isVaisalaMeter) {
            tvBarometer.setVisibility(View.GONE);
            ivBarometer.setVisibility(View.GONE);
        }
        tvDeviceName.setText(mDeviceName + " - "+mDeviceAddress);
        mContext= this;
        context = this;
        isPictureTaken= false;
        listOfMemos = new ArrayList<Memo>();
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sensor_tag, menu);
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

    @Override
    public void onResume() {
        super.onResume();
        mIntentTFilter=makeGattUpdateIntentFilter();
        registerReceiver(mGattUpdateReceiver, mIntentTFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        Picasso.with(this).cancelRequest(target);//Suggested in Picasso documentation
        Log.i(TAG,"onOause unregistering receiver ");
        if(mGattUpdateReceiver!=null) {
            unregisterReceiver(mGattUpdateReceiver);
            mGattUpdateReceiver = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mGattUpdateReceiver!=null) {
            unregisterReceiver(mGattUpdateReceiver);
            mGattUpdateReceiver = null;
        }
        if(isSensorTag)
            BLEDemo.bleSensorTag.disableSensorTagNotification(BLEDemo.mGattCharacteristics, BLEDemo.mBluetoothLeService);
        else if(isVaisalaMeter)
            VaisalaMeter.disableMeterTagNotification(BLEDemo.mGattCharacteristics, BLEDemo.mBluetoothLeService);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy disconnecting from device");
        BLEDemo.mBluetoothLeService.disconnect();
    }
    public void Clear(View v) {
       /*//Todo: Subscribe to characteristics
        Log.i(TAG,"Go to memos activity");
        Intent intentActivity = new Intent(this,TripTableActivity.class);
        startActivity(intentActivity);*/
        //BLEDemo.mBluetoothLeService.connect(mDeviceAddress);
       
        mIntentTFilter=makeGattUpdateIntentFilter();
        registerReceiver(mGattUpdateReceiver, mIntentTFilter);
        /* if(isSensorTag)
            BLEDemo.bleSensorTag.enableSensorTagNotyfication(BLEDemo.mGattCharacteristics, BLEDemo.mBluetoothLeService);*/
        
        
        Drawable placeHolder = getResources().getDrawable(R.drawable.placeholder_photo);
        mImageView.setImageDrawable(placeHolder);
        mImageView.buildDrawingCache();
    }

    public void SaveValuesInDb(View v) {
        Bitmap bitmap_full_size = mPicture;
        Bitmap bitmap_full_size2 = null;
        byte[] byte_thumbnail;
        Bitmap bitmap_thumbnail;
        byte[] byte_full_size;

        Log.i(TAG,"Saving values in database");
        Memo memo = new Memo();
        memo.setName(mDeviceName);
        memo.setAddress(mDeviceAddress);
        String temp= tvTemperature.getText().toString();
        memo.setTemperature(temp);
        String hum= tvHumidity.getText().toString();
        memo.setHumidity(hum);
        String dateTimeStamp = Utils.getCurrentDateTimeString();
        memo.setDate(dateTimeStamp);
        //Storing full image size in database, convert file to bitmap, then bitmap to byte array

        //mImageView.buildDrawingCache();
        bitmap_full_size2 = mImageView.getDrawingCache();

        if (isPictureTaken == true){
            byte_full_size = Utils.bitmap2bytes(bitmap_full_size2);
            //byte_full_size2 = Utils.bitmap2bytes(bitmap_full_size2);
            bitmap_thumbnail = Bitmap.createScaledBitmap(bitmap_full_size, 65, 65, false);
            byte_thumbnail = Utils.bitmap2bytes(bitmap_thumbnail);
            Utils.makeToast(this, "All data stored in database");
        }
        else{
            Log.d(TAG, "Photo not stored, photo is null");
            bitmap_full_size = BitmapFactory.decodeResource(getResources(), R.drawable.no_image);
            byte_full_size = Utils.bitmap2bytes(bitmap_full_size);
            bitmap_thumbnail = Bitmap.createScaledBitmap(bitmap_full_size, 65, 65, false);
            byte_thumbnail = Utils.bitmap2bytes(bitmap_thumbnail);
            Utils.makeToast(this, "Photo empty, default empty picture stored in database instead");
        }
        memo.setPicture(byte_full_size);
        memo.setPictureThumbnail(byte_thumbnail);
        //Now, let's scale image to thumbnail and store it in database
        greendaoRepository.insertOrUpdate(this,memo);
        Log.i(TAG,"values in database were stored");
//        saveValuesInBluemix();
    }



    public void saveValuesInBluemix() {
        Bitmap bitmap_full_size = null;
        byte[] byte_thumbnail;
        Bitmap bitmap_thumbnail;
        byte[] byte_full_size;

        Log.i(TAG,"Saving values in database");
        DemoItem memo = new DemoItem();
        memo.setName(mDeviceName);
        memo.setAddress(mDeviceAddress);
        String temp= tvTemperature.getText().toString();
        memo.setTemperature(temp);
        String hum= tvHumidity.getText().toString();
        memo.setHumidity(hum);
        String dateTimeStamp = Utils.getCurrentDateTimeString();
        memo.setDate(dateTimeStamp);
        IBMDataFile ibmDataFile;
        try {
            ibmDataFile=IBMDataFile.fileWithPath(this,mCurrentPhotoPath);
            memo.setImage(ibmDataFile);
        } catch (IBMDataFileException e) {
            e.printStackTrace();
        }
        //Storing full image size in database, convert file to bitmap, then bitmap to byte array
        if (isPictureTaken == true){
            byte_full_size = Utils.bitmap2bytes(bitmap_full_size);
            bitmap_thumbnail = Bitmap.createScaledBitmap(bitmap_full_size, 65, 65, false);
            String path =saveBitmapAsFile(bitmap_thumbnail);
            byte_thumbnail = Utils.bitmap2bytes(bitmap_thumbnail);
            try {
                ibmDataFile=IBMDataFile.fileWithPath(this,path);
                memo.setThumbnailImage(ibmDataFile);
            } catch (IBMDataFileException e) {
                e.printStackTrace();
            }

            Utils.makeToast(this, "All data was stored in Bluemix database");
        }
        else{
            Log.d(TAG, "Photo not stored, photo is null");
            bitmap_full_size = BitmapFactory.decodeResource(getResources(), R.drawable.no_image);
            byte_full_size = Utils.bitmap2bytes(bitmap_full_size);
            bitmap_thumbnail = Bitmap.createScaledBitmap(bitmap_full_size, 65, 65, false);
            byte_thumbnail = Utils.bitmap2bytes(bitmap_thumbnail);
            Utils.makeToast(this, "Photo empty, default empty picture stored in database instead");
        }

        BluemixDao.saveItem(memo, new onDaoBluemixSuccessFaillListener() {
            @Override
            public void onSuccess(Object object) {
                Utils.makeToast(mContext,"Data saved to Bluemix");
            }

            @Override
            public void onFail() {

            }
        });

        //Now, let's scale image to thumbnail and store it in database
        Log.i(TAG,"values in Bluemix database were stored");
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                if (requestCode == REQUEST_TAKE_PHOTO_BIG && resultCode == RESULT_OK) {
                    //Thumbnail to null
                    imageBitmapPreview = null;
                    //Handle Big picture
                    handleBigCameraPhoto();
                }
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mPicture= bitmap;
            mImageView.setImageBitmap(mPicture);
            mImageView.buildDrawingCache();
            isPictureTaken = true;

        }
        @Override public void onBitmapFailed(Drawable errorDrawable) {
            Utils.makeToast(mContext, "Photo failed to load into imageView");
        }
        @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }

    };
    private void handleBigCameraPhoto() {
        Picasso.with(this)
                .load(mCurrentPhotoPath)
                .placeholder(R.drawable.placeholder_photo)   // optional
                .error(R.drawable.no_image)      // optional
                        //.resize(250, 200)                        // optional
                        //.rotate(90)                             // optional
                .into(target);
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BLEService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private String TAG="SensorTagActivity";
    private BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "Intent received =" + action);
            if (BLEService.ACTION_DATA_AVAILABLE.equals(action)) {
                if(isSensorTag) {
                    String strType = intent.getStringExtra(BLEService.EXTRA_DATA_TYPE);
                    byte[] rx = intent.getByteArrayExtra(BLEService.EXTRA_DATA);
                    if (strType.equals("Temperature")) {
                        double ambient = BleSensorTag.extractAmbientTemperature(rx);
                        double target = BleSensorTag.extractTargetTemperature(rx, ambient);
                        String sa = String.format("%.2f", ambient) + " C";
                        Log.d(TAG, "Type ==" + strType);
                        Log.d(TAG, strType + "=" + sa);
                        tvTemperature.setText(sa);

                    } else if (strType.equals("Humidity")) {
                        Point3D hum = BleSensorTag.getHumidityValue(rx);
                        String strHum = String.format("%.2f ", hum.x) + " %rH";

                        Log.d(TAG, "Type ==" + strType);
                        Log.d(TAG, strType + "=" + strHum);
                        tvHumidity.setText(strHum);
                    } else if (strType.equals("Barometer")) {
                        Point3D bar = BleSensorTag.getBarometerValue(rx);
                        String strBar = Double.toString(bar.x) + " meter";

                        Log.d(TAG, "BAR= " + strBar);
                        tvBarometer.setText(strBar);
                    }
                } else if(isVaisalaMeter) {
                    String strType = intent.getStringExtra(BLEService.EXTRA_DATA_TYPE);
                    String strValue= intent.getStringExtra(BLEService.EXTRA_DATA);
                    Log.i(TAG,"StrValue="+strValue+" strType="+strType);
                    if (strType.equals("Temperature")) {
                        tvTemperature.setText(strValue);
                    } else if (strType.equals("Humidity")) {
                        tvHumidity.setText(strValue);
                    }
                }
            }
        }
    };

    public void dispatchTakePictureIntent(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "Error occurred while creating the file for the photo,error code:"+ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.i(TAG, "File was successfully created in:"+photoFile.getAbsolutePath());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO_BIG);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName;

        imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }


    public String saveBitmapAsFile(Bitmap bitmap) {
        String filename;
        Date date = new Date(0);
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMddHHmmss");
        filename =  sdf.format(date);

        try{
            String path = Environment.getExternalStorageDirectory().toString();
            OutputStream fOut = null;
            File file = new File(path, "/DCIM/Signatures/"+filename+".jpg");
            fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            return file.getPath();

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
