package drawable.androidespoteldemo.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;

import com.example.android.androidespoteldemo.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

enum BarometerCalibrationCoefficients {
    INSTANCE;
    volatile public List<Integer> barometerCalibrationCoefficients;
    volatile public double heightCalibration;
}

enum MagnetometerCalibrationCoefficients {
    INSTANCE;
    public Point3D val = new Point3D(0.0,0.0,0.0);

}

public class BleSensorTag {
    private static boolean NOTIFY_DEBUG;

    private static String TAG="BleSensorTag";
    public static final String tmpCharUID = "f000aa01";
    public static final String accCharUID = "f000aa11";
    public static final String humCharUID = "f000aa21";
    public static final String barCharUID = "f000aa41";
    public static final String gyrCharUID = "f000aa51";
    public static final String magCharUID = "f000aa31";

    public static final String ctmpCharUID = "f000aa02";
    public static final String caccCharUID = "f000aa12";
    public static final String chumCharUID = "f000aa22";
    public static final String cbarCharUID = "f000aa42";
    public static final String cgyrCharUID = "f000aa52";
    public static final String cmagCharUID = "f000aa32";


    public static final String stmpCharUID = "f000aa00";
    public static final String saccCharUID = "f000aa10";
    public static final String shumCharUID = "f000aa20";
    public static final String sbarCharUID = "f000aa40";
    public static final String sgyrCharUID = "f000aa50";
    public static final String smagCharUID = "f000aa30";

    public static int IND_SERVICE_TEMPERATURE=3;
    public static int IND_SERVICE_BAROMETER=7;
    public static int IND_SERVICE_HUMIDITY=5;
    public static int IND_SERVICE_GYROSCOPE=4;

    public String[] sensorStrings ={"Temperature","Humidity"};

    public static String[] sensorTagNotifyStrings=null;


    public static HashMap<String, String> tiSensorTagValues= new HashMap<String, String>();
    static {
        tiSensorTagValues.put("Temperature", "");
        tiSensorTagValues.put("Accelerometer", "");
        tiSensorTagValues.put("Humidity", "");
        tiSensorTagValues.put("Barometer", "");
        tiSensorTagValues.put("Gyroscope", "");
        tiSensorTagValues.put("Magnetometer", "");
    }


    public static HashMap<String, String> tiSensorTagUUID= new HashMap<String, String>();
    static {
        tiSensorTagUUID.put(stmpCharUID , "Temperature");
        tiSensorTagUUID.put(saccCharUID, "Accelerometer");
        tiSensorTagUUID.put(shumCharUID, "Humidity");
        tiSensorTagUUID.put(sgyrCharUID, "Gyroscope");
        tiSensorTagUUID.put(smagCharUID, "Magnetometer");
    }

    private static String TEMPERATURE="";

    public static enum TISensorTag {
        TEMPERATURE ,
        IR_TEMPERATURE,
        ACCELEROMETER,
        MAGNETOMETER,
        GYROSCOPE,
        HUMIDITY,
        BAROMETER
    }



    public BleSensorTag() {
    }


    public static double extractAmbientTemperature(BluetoothGattCharacteristic c) {
        int offset = 2;
        return shortUnsignedAtOffset(c, offset) / 128.0;
    }

    public static double extractAmbientTemperature(byte[] b) {
        int offset = 2;
        return shortUnsignedAtOffset(b, offset) / 128.0;
    }


    public static double extractTargetTemperature(BluetoothGattCharacteristic c, double ambient) {
        Integer twoByteValue = shortSignedAtOffset(c, 0);

        double Vobj2 = twoByteValue.doubleValue();
        Vobj2 *= 0.00000015625;

        double Tdie = ambient + 273.15;

        double S0 = 5.593E-14;	// Calibration factor
        double a1 = 1.75E-3;
        double a2 = -1.678E-5;
        double b0 = -2.94E-5;
        double b1 = -5.7E-7;
        double b2 = 4.63E-9;
        double c2 = 13.4;
        double Tref = 298.15;
        double power1 = 2;
        double power2 = .25;
        double power3 = 4;
        double S = S0*(1+a1*(Tdie - Tref)+a2*Math.pow((Tdie - Tref), power1));
        double Vos = b0 + b1*(Tdie - Tref) + b2*Math.pow((Tdie - Tref),power1);
        double fObj = (Vobj2 - Vos) + c2*Math.pow((Vobj2 - Vos),power1);
        double tObj = Math.pow(Math.pow(Tdie,power3) + (fObj/S),power2);

        return tObj - 273.15;
    }


    public static double extractTargetTemperature(byte[] b, double ambient) {
        Integer twoByteValue = shortSignedAtOffset(b, 0);

        double Vobj2 = twoByteValue.doubleValue();
        Vobj2 *= 0.00000015625;

        double Tdie = ambient + 273.15;

        double S0 = 5.593E-14;	// Calibration factor
        double a1 = 1.75E-3;
        double a2 = -1.678E-5;
        double b0 = -2.94E-5;
        double b1 = -5.7E-7;
        double b2 = 4.63E-9;
        double c2 = 13.4;
        double Tref = 298.15;
        double power1 = 2;
        double power2 = .25;
        double power3 = 4;
        double S = S0*(1+a1*(Tdie - Tref)+a2*Math.pow((Tdie - Tref), power1));
        double Vos = b0 + b1*(Tdie - Tref) + b2*Math.pow((Tdie - Tref),power1);
        double fObj = (Vobj2 - Vos) + c2*Math.pow((Vobj2 - Vos),power1);
        double tObj = Math.pow(Math.pow(Tdie,power3) + (fObj/S),power2);

        return tObj - 273.15;
    }



    /**
     * Gyroscope, Magnetometer, Barometer, IR temperature
     * all store 16 bit two's complement values in the awkward format
     * LSB MSB, which cannot be directly parsed as getIntValue(FORMAT_SINT16, offset)
     * because the bytes are stored in the "wrong" direction.
     *
     * This function extracts these 16 bit two's complement values.
     * */

    private static Integer shortUnsignedAtOffset(BluetoothGattCharacteristic c, int offset) {
        Integer lowerByte = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset);
        Integer upperByte = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset + 1); // Note: interpret MSB as unsigned.

        return (upperByte << 8) + lowerByte;
    }

    public static Integer shortSignedAtOffset(BluetoothGattCharacteristic c, int offset) {
        Integer lowerByte = c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset);
        Integer upperByte = c.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8, offset + 1); // Note: interpret MSB as signed.

        return (upperByte << 8) + lowerByte;
    }



    public static Integer shortSignedAtOffset(byte[] c, int offset) {
        Integer lowerByte = (int) c[offset] & 0xFF;
        Integer upperByte = (int) c[offset+1]; // // Interpret MSB as signed
        return (upperByte << 8) + lowerByte;
    }

    public static Integer shortUnsignedAtOffset(byte[] c, int offset) {
        Integer lowerByte = (int) c[offset] & 0xFF;
        Integer upperByte = (int) c[offset+1] & 0xFF; // // Interpret MSB as signed
        return (upperByte << 8) + lowerByte;
    }



    public static Point3D getAccelerometerValue(final byte[] value) {
		/*
		 * The accelerometer has the range [-2g, 2g] with unit (1/64)g.
		 *
		 * To convert from unit (1/64)g to unit g we divide by 64.
		 *
		 * (g = 9.81 m/s^2)
		 *
		 * The z value is multiplied with -1 to coincide with how we have arbitrarily defined the positive y direction. (illustrated by the apps accelerometer
		 * image)
		 */
        Integer x = (int) value[0];
        Integer y = (int) value[1];
        Integer z = (int) value[2] * -1;

        return new Point3D(x / 64.0, y / 64.0, z / 64.0);
    }


    public static Point3D getGyroscopeValue(final byte [] value) {

        float y = shortSignedAtOffset(value, 0) * (500f / 65536f) * -1;
        float x = shortSignedAtOffset(value, 2) * (500f / 65536f);
        float z = shortSignedAtOffset(value, 4) * (500f / 65536f);

        return new Point3D(x,y,z);
    }


    public static Point3D getHumidityValue(final byte[] value) {
        int a = shortUnsignedAtOffset(value, 2);
        // bits [1..0] are status bits and need to be cleared according
        // to the user guide, but the iOS code doesn't bother. It should
        // have minimal impact.
        a = a - (a % 4);

        return new Point3D((-6f) + 125f * (a / 65535f), 0, 0);
    }


    public static Point3D getMagnetoValue(final byte [] value) {
        Point3D mcal = MagnetometerCalibrationCoefficients.INSTANCE.val;
        // Multiply x and y with -1 so that the values correspond with the image in the app
        float x = shortSignedAtOffset(value, 0) * (2000f / 65536f) * -1;
        float y = shortSignedAtOffset(value, 2) * (2000f / 65536f) * -1;
        float z = shortSignedAtOffset(value, 4) * (2000f / 65536f);

        return new Point3D(x - mcal.x, y - mcal.y, z - mcal.z);
    }


    public static Point3D getBarometerValue(final byte [] value) {

        List<Integer> barometerCalibrationCoefficients = BarometerCalibrationCoefficients.INSTANCE.barometerCalibrationCoefficients;
        if (barometerCalibrationCoefficients == null) {
            Log.w("Custom", "Data notification arrived for barometer before it was calibrated.");
            return new Point3D(0,0,0);
        }

        final int[] c; // Calibration coefficients
        final Integer t_r; // Temperature raw value from sensor
        final Integer p_r; // Pressure raw value from sensor
        final Double S; // Interim value in calculation
        final Double O; // Interim value in calculation
        final Double p_a; // Pressure actual value in unit Pascal.

        c = new int[barometerCalibrationCoefficients.size()];
        for (int i = 0; i < barometerCalibrationCoefficients.size(); i++) {
            c[i] = barometerCalibrationCoefficients.get(i);
        }

        t_r = shortSignedAtOffset(value, 0);
        p_r = shortUnsignedAtOffset(value, 2);

        S = c[2] + c[3] * t_r / Math.pow(2, 17) + ((c[4] * t_r / Math.pow(2, 15)) * t_r) / Math.pow(2, 19);
        O = c[5] * Math.pow(2, 14) + c[6] * t_r / Math.pow(2, 3) + ((c[7] * t_r / Math.pow(2, 15)) * t_r) / Math.pow(2, 4);
        p_a = (S * p_r + O) / Math.pow(2, 14);

        return new Point3D(p_a,0,0);
    }




    public static String convertAndWriteCharacteristics(BluetoothGattCharacteristic c){
        String ret="";
        byte[] b=c.getValue();
        if(NOTIFY_DEBUG) {
            Log.i(TAG, "convertAndWriteCharacteristics");
            Log.v(TAG, "value in bytes = "+b.toString());
        }
        String uuidstr =c.getUuid().toString();
        boolean bSW=c.getUuid().toString().startsWith(tmpCharUID) ;
        if(NOTIFY_DEBUG) {
            Log.v(TAG, "UUID = "+uuidstr);
            Log.v(TAG, "Tools.tmpCharUID = "+tmpCharUID);
            Log.v(TAG, "startwith boolean= "+bSW);
        }
        int index=0;
        if (c.getUuid().toString().startsWith(tmpCharUID)) { // temp data changed
            index=0;
            double ambient = extractAmbientTemperature(b);
            double target = extractTargetTemperature(b, ambient);
            String strTempA=String.format( "%.2f", ambient);
            String strTempT=String.format( "%.2f", target);
            if(NOTIFY_DEBUG) {
                Log.v(TAG, "TEMPA = "+strTempA);
                Log.v(TAG, "TEMPT = "+strTempT);
            }
            tiSensorTagValues.put("Temperature", strTempA);
            TEMPERATURE=strTempA;
//            sensorTagNotifyStrings[0]="Temperature";
//            sensorTagNotifyStrings[1]=b.toString();
            ret="Temperature";
        } else if (c.getUuid().toString().startsWith(accCharUID)) {
            index=1;
            // handle acc data
            Point3D acc= getAccelerometerValue(b);
            String strAcc=acc.toString();
            Log.d(TAG, "ACC = "+strAcc);
            tiSensorTagValues.put("Accelerometer", strAcc);
            ret="Accelerometer";
        }
        else if (c.getUuid().toString().startsWith(barCharUID)) {
            index=2;
            // handle bar data
            Point3D acc= getBarometerValue(b);
            String strBar=Double.toString(acc.x);
            Log.d(TAG, "BAR= "+strBar);

            tiSensorTagValues.put("Barometer =", strBar);
            ret="Barometer";
        }
        else if (c.getUuid().toString().startsWith(humCharUID)) {
            index=3;
            // handle hum data
            Point3D hum = getHumidityValue(b);
            String strHum=String.format( "%.2f", hum.x);
            //String strHum=Double.toString(hum.x);
            if(NOTIFY_DEBUG) {
                Log.d(TAG, "HUM = "+strHum);
            }
            tiSensorTagValues.put("Humidity", strHum);
            ret="Humidity";
        }
        else if (c.getUuid().toString().startsWith(gyrCharUID)) {
            index=4;
            // handle acc data
            Point3D gyr= getGyroscopeValue(b);
            String strGyr =gyr.toString();
            Log.d(TAG, "GYROSCOPE= "+strGyr);
            tiSensorTagValues.put("Gyroscope", strGyr);
            ret="Humidity";
        }
        else if(c.getUuid().toString().startsWith(magCharUID)) {
            index=5;
            // handle acc data
            Point3D mag = getMagnetoValue(b);
            String strMag=mag.toString();
            Log.d(TAG, "GYROSCOPE= "+strMag);
            tiSensorTagValues.put("Magnetometer", strMag);
            ret="Magnetometer";
        }

        return ret;

    }




    public static void toggleSensor(BluetoothGatt btGatt, BluetoothGattCharacteristic charIdx) {
        byte en[] = {0x01};
//        if (sensorEnabled[idx]) {
//            en[0] = 0x00;
//            sensorEnabled[idx] = false;
//        } else {
//            sensorEnabled[idx] = true;
//        }

        Log.i(TAG, "toggleSensor " );

//        BluetoothGattCharacteristic charIdx = btGatt.getService(UUID.fromString(serviceUIDs[idx])).getCharacteristic(UUID.fromString(charConfigUIDs[idx]));
        charIdx.setValue(en);
        btGatt.writeCharacteristic(charIdx);
        Utils.Delay(300);
    }


    public void toggleSensorTagCharacteristics(ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics, BLEService service) {
        Log.i(TAG, "toggleSensorTagCharacteristics ");
        BluetoothGattCharacteristic characteristicT= mGattCharacteristics.get(IND_SERVICE_TEMPERATURE).get(1);
        BluetoothGattCharacteristic characteristicH= mGattCharacteristics.get(IND_SERVICE_HUMIDITY).get(1);
        BluetoothGattCharacteristic characteristicB= mGattCharacteristics.get(IND_SERVICE_BAROMETER).get(1);
        toggleSensor(service.mBluetoothGatt,characteristicT);
        toggleSensor(service.mBluetoothGatt,characteristicH);
        toggleSensor(service.mBluetoothGatt,characteristicB);
    }


    public void enableSensorTagNotyfication(ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics, BLEService service) {
        Log.i(TAG, "enableSensorTagNotyfication");
        BluetoothGattCharacteristic characteristicTT= mGattCharacteristics.get(IND_SERVICE_TEMPERATURE).get(1);
        BluetoothGattCharacteristic characteristicHT= mGattCharacteristics.get(IND_SERVICE_HUMIDITY).get(1);
        BluetoothGattCharacteristic characteristicBT= mGattCharacteristics.get(IND_SERVICE_BAROMETER).get(1);

        BluetoothGattCharacteristic characteristicT= mGattCharacteristics.get(IND_SERVICE_TEMPERATURE).get(0);
        toggleSensor(service.mBluetoothGatt,characteristicTT);
        service.setCharacteristicNotification(characteristicT, true);
        Utils.Delay(200);

        BluetoothGattCharacteristic characteristicH= mGattCharacteristics.get(IND_SERVICE_HUMIDITY).get(0);
        toggleSensor(service.mBluetoothGatt,characteristicHT);
        service.setCharacteristicNotification(characteristicH, true);
        Utils.Delay(200);

        BluetoothGattCharacteristic characteristicB= mGattCharacteristics.get(IND_SERVICE_BAROMETER).get(0);
        toggleSensor(service.mBluetoothGatt,characteristicBT);
        service.setCharacteristicNotification(characteristicB, true);

    }

    public static void disableSensorTagNotification(ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics, BLEService service) {
        Log.i(TAG, "disableSensorTagNotification ");
        BluetoothGattCharacteristic characteristicT= mGattCharacteristics.get(IND_SERVICE_TEMPERATURE).get(0);
        service.setCharacteristicNotification(characteristicT, false);


        BluetoothGattCharacteristic characteristicH= mGattCharacteristics.get(IND_SERVICE_HUMIDITY).get(0);
        service.setCharacteristicNotification(characteristicH, false);


        BluetoothGattCharacteristic characteristicB= mGattCharacteristics.get(IND_SERVICE_BAROMETER).get(0);
        service.setCharacteristicNotification(characteristicB, false);

    }



}


