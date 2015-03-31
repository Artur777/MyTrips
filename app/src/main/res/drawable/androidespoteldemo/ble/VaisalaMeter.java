package drawable.androidespoteldemo.ble;

import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.example.android.androidespoteldemo.Utils;

import java.util.ArrayList;

/**
 * Created by AChojeck on 23/02/2015.
 */
public class VaisalaMeter
{


    public static String tmpCharUID = "d56a26ff";
    public static String dewpointCharUID = "f000aa11";
    public static String humCharUID = "5e5d91e0";
    public static String bulbCharUID = "f000aa41";
    public static String absHumCharUID = "f000aa51";
    public static String mixRatioCharUID = "f000aa31";
    public static String entalpyCharUID = "f000aa31";
    public static String probeCharUID = "";

    private static final String TAG = "VaisalaMeter";
    public static int IND_SERVICE_DEVICE_INFO=2;
    public static int IND_SERVICE_PROBE=3;
    public static int IND_SERVICE_TEMPERATURE=4;
    public static int IND_SERVICE_HUMIDITY=5;
    public static int IND_SERVICE_DEWPOINT=6;
    public static int IND_SERVICE_BULB=7;
    public static int IND_SERVICE_ABSOLUTE_HUMIDITY=8;
    public static int IND_SERVICE_MIXING_RATIO=9;
    public static int IND_SERVICE_ENTALPY=10;
    private static boolean NOTIFY_DEBUG=false;


    public VaisalaMeter() {
    }

    public static void disableMeterTagNotification(ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics, BLEService service) {
        Log.i(TAG, "disableMeterTagNotification");
        BluetoothGattCharacteristic characteristicT= mGattCharacteristics.get(IND_SERVICE_TEMPERATURE).get(0);
        service.setCharacteristicNotification(characteristicT, false);

        BluetoothGattCharacteristic characteristicH= mGattCharacteristics.get(IND_SERVICE_HUMIDITY).get(0);
        service.setCharacteristicNotification(characteristicH, false);
        Utils.Delay(500);

    }

    public void enableNotification(ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics, BLEService service) {
        Log.i(TAG, "enableNotification ");
        BluetoothGattCharacteristic characteristicT= mGattCharacteristics.get(IND_SERVICE_TEMPERATURE).get(0);
        service.setCharacteristicNotification(characteristicT, true);
        Utils.Delay(300);
        BluetoothGattCharacteristic characteristicH= mGattCharacteristics.get(IND_SERVICE_HUMIDITY).get(0);
        service.setCharacteristicNotification(characteristicH, true);

    }


    public void getUUIDStrings(ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics, BLEService service){
        Log.i(TAG, "getUUIDStrings");
        BluetoothGattCharacteristic charTemperature= mGattCharacteristics.get(IND_SERVICE_TEMPERATURE).get(0);
        tmpCharUID=charTemperature.getUuid().toString();

        BluetoothGattCharacteristic charHumidity= mGattCharacteristics.get(IND_SERVICE_HUMIDITY).get(0);
        humCharUID=charHumidity.getUuid().toString();

        BluetoothGattCharacteristic charDewPoint= mGattCharacteristics.get(IND_SERVICE_DEWPOINT).get(0);
        dewpointCharUID=charDewPoint.getUuid().toString();

        BluetoothGattCharacteristic charBulb= mGattCharacteristics.get(IND_SERVICE_BULB).get(0);
        bulbCharUID=charBulb.getUuid().toString();

        BluetoothGattCharacteristic charAbsHum= mGattCharacteristics.get(IND_SERVICE_ABSOLUTE_HUMIDITY).get(0);
        absHumCharUID=charAbsHum.getUuid().toString();

        BluetoothGattCharacteristic charMixRatio= mGattCharacteristics.get(IND_SERVICE_MIXING_RATIO).get(0);
        mixRatioCharUID=charMixRatio.getUuid().toString();

        BluetoothGattCharacteristic charEntalpy= mGattCharacteristics.get(IND_SERVICE_ENTALPY).get(0);
        entalpyCharUID=charEntalpy.getUuid().toString();

        BluetoothGattCharacteristic charProbe= mGattCharacteristics.get(IND_SERVICE_PROBE).get(0);
        probeCharUID=charProbe.getUuid().toString();

    }


    public static String convertAndWriteCharacteristics(BluetoothGattCharacteristic c){
        String ret="";
        byte[] b=c.getValue();
        String uuidstr =c.getUuid().toString();

        int index=0;
        if (c.getUuid().toString().equals(tmpCharUID)) { // temp data changed
            index=0;
            ret="Temperature";
        } else if (c.getUuid().toString().equals(humCharUID)) {
            index=1;
            // handle acc data
            ret="Humidity";
        }
        else if (c.getUuid().toString().equals(mixRatioCharUID)) {
            index=2;
            // handle bar data

            ret="MixRatio";
        }
        else if (c.getUuid().toString().equals(dewpointCharUID)) {
            index=3;

            ret="DewPoint";
        }
        else if (c.getUuid().toString().equals(bulbCharUID)) {
            index=4;
            ret="Bulb";
        }
        else if(c.getUuid().toString().equals(absHumCharUID)) {
            index=5;
            ret="AbsHum";
        }
        else if(c.getUuid().toString().equals(entalpyCharUID)) {
            index=5;
            ret="Entalpy";
        }

        return ret;

    }


}




