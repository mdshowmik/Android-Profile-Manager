package com.showmik.ProfileManager;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;

public class ProfileManagerService extends Service implements SensorEventListener{

	public SensorManager mSensorManager;
	public Sensor mAccelerometer, mProximity;
	public AudioManager ringMode;
	
	private final IBinder mBinder = new LocalBinder();
	
	double xAccelerometer, yAccelerometer, zAccelerometer;
	int vProximity;
	
	public class LocalBinder extends Binder {
        ProfileManagerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ProfileManagerService.this;
        }
    }
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);	
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); 
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY); 
        ringMode = (AudioManager) getSystemService(AUDIO_SERVICE);
        
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	 @Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		mSensorManager.unregisterListener(this);
	}
	 
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub	
		return Service.START_STICKY;
	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		
		if(arg0.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
		{
			xAccelerometer=arg0.values[0];
			yAccelerometer=arg0.values[1];
			zAccelerometer=arg0.values[2];
		}
		if(arg0.sensor.getType() == Sensor.TYPE_PROXIMITY)
		{
			vProximity=(int) arg0.values[0];
		}
	
		//mobile in standing position
		if((yAccelerometer >= 7) || ( yAccelerometer <= -7))
		{
			//confirmation of in pocket or not
			if(vProximity==0)
			{
				//in pocket
				if(yAccelerometer<0)
				{
					ringMode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
				}
				else if(yAccelerometer>0)
				{
					ringMode.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
				}
			}
		}
		else
		{
			//not in pocket
			if(zAccelerometer<0)
			{
				ringMode.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			}
			else if(zAccelerometer>0)
			{
				ringMode.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			}
		}
	}
}
