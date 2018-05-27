package com.showmik.ProfileManager;

import com.showmik.ProfileManager.ProfileManagerService.LocalBinder;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;


public class ProfileManagerActivity extends Activity{
	
	public Intent intent;
	public ToggleButton state;
	
	public SharedPreferences sharedpreferences;
	
	public static String MyPREFERENCES="preferences";
	public static final String keyOfMemory ="value";
	
	boolean mBound = false;
	
	ProfileManagerService mService;
		
		
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		state = (ToggleButton)findViewById(R.id.toggleButton1);
		
		sharedpreferences=getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
		Boolean getMemory=sharedpreferences.getBoolean(keyOfMemory, false);
		if(getMemory==true)
		{
			state.setChecked(true);
		}
		else
		{
			state.setChecked(false);
		}
		
		intent= new Intent(ProfileManagerActivity.this, ProfileManagerService.class);
		
		state.setOnClickListener(new View.OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(state.isChecked())
				 {
					Toast.makeText(ProfileManagerActivity.this, "Profile Manager Is Activated", Toast.LENGTH_SHORT).show();
					startService(intent);
					bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
					
					sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedpreferences.edit();
					editor.putBoolean(keyOfMemory, true);
			        editor.commit();
				 }
				 else 
				 {
					Toast.makeText(ProfileManagerActivity.this, "Profile Manager Is Deactivated", Toast.LENGTH_SHORT).show();
					stopService(intent);
					 
					sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedpreferences.edit();
					editor.putBoolean(keyOfMemory, false);
			        editor.commit();
				 }
			}
		});	
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	 @Override
	 protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	 
	private ServiceConnection mConnection = new ServiceConnection() {
	
		//@Override
		public void onServiceConnected(ComponentName className,IBinder service) {
		    // We've bound to LocalService, cast the IBinder and get LocalService instance
		    LocalBinder binder = (LocalBinder) service;
		    mService = binder.getService();
		    mBound = true;
		}
		
		//@Override
    	public void onServiceDisconnected(ComponentName arg0) {
        	mBound = false;
    	}
	};
}