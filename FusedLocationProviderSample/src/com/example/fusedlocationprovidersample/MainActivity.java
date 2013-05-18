package com.example.fusedlocationprovidersample;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import android.location.Location;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;

public class MainActivity extends Activity {

	private LocationClient _locationClient;
	private TextView _textResult;
    private final LocationListener _locationListener = new LocationListener() {
		
		@Override
		public void onLocationChanged(final Location location) {
			MainActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					String text = _textResult.getText().toString();
		            text = DateFormat.format("hh:mm:ss.sss", location.getTime()) + " - " 
		                    + location.getLatitude() + "/" +
		                    + location.getLongitude() + "/" +
		                    + location.getAccuracy() + 
		                    "\n" + text;

		            _textResult.setText(text);
				}
			});
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_textResult = (TextView)findViewById(R.id.text_result);
		
		final Button buttonLocate = (Button)findViewById(R.id.button_locate);
		buttonLocate.setOnClickListener(new OnClickListener() {
			private boolean _isStarted = false;
			
			@Override
			public void onClick(View v) {
				if (!_isStarted) {
					startLocate();
					buttonLocate.setText("Stop");
				} else {
					stopLocate();
					buttonLocate.setText("Start");
				}
				
				_isStarted = !_isStarted;
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		stopLocate();
		super.onDestroy();
	}
	
	private void startLocate() {
		_locationClient = new LocationClient(this, new ConnectionCallbacks() {

			@Override
            public void onConnected(Bundle bundle) {
            	LocationRequest request = new LocationRequest();
            	request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            	request.setInterval(5000); // 5秒おき
            	_locationClient.requestLocationUpdates(request, _locationListener);
            }

            @Override
            public void onDisconnected() {
            	_locationClient = null;
            }

        }, new OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult result) {
            }
        });
		
		_locationClient.connect();
	}

	private void stopLocate() {
		if (_locationClient == null || !_locationClient.isConnected()) {
			return;
		}
		
		_locationClient.removeLocationUpdates(_locationListener);
		_locationClient.disconnect();
	}
}
