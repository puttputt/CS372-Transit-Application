package com.basementbobs.transitdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


public class TransitMapActivity extends MapActivity {

	MapView mapView;
	MapController mapController;
	
	private Timer busLocationUpdateTimer;
	
	public TransitMapActivity() {
		busLocationUpdateTimer = new Timer();
	}
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maplayout);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
 
        mapController = mapView.getController();
        
        mapController.setCenter(new GeoPoint((int)(50.4547222 * 1E6), (int)(-104.6066667 * 1E6)));
        mapController.setZoom(14);
       
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	//busLocationUpdateTimer.cancel();
    }
    
    
    @Override
    protected void onResume() {
    	super.onPause();
    	//busLocationUpdateTimer.schedule(mUpdateTimeTask, 0, 1000);
    }
    

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private static class BusData {
		public Integer busId;
		public Integer routeId;
		public Float latitude;
		public Float longitude;
		public Integer bearing;
		public String desc;
	}
	
	
	private ArrayList<BusData> getBusLocations() throws Exception{
		
		ArrayList<BusData> busDataList = new ArrayList<BusData>();
		
		String response = rawBusLocationRequest();
		//String response = "sizeof=5&busID=625&routeID=3&latitude=50.4159469604492&longitude=-104.601631164551&bearing=0&desc=UNIVERSITY&busID=620&routeID=7&latitude=50.455150604248&longitude=-104.559364318848&bearing=90&desc=GLENCAIRN&busID=619&routeID=9&latitude=50.4551811218262&longitude=-104.571228027344&bearing=270&desc=ALBERT+PARK&busID=612&routeID=12&latitude=50.4438018798828&longitude=-104.606513977051&bearing=180&desc=VARSITY+PARK&busID=611&routeID=3&latitude=50.4629974365234&longitude=-104.641555786133&bearing=260&desc=SHERWOOD";
		
		String[] params = response.split("&");
		Log.d("***Reponse", params[0].split("=")[1]);
		
		if (!params[0].split("=")[0].equals("sizeof")) {
			throw new Exception("Response appears invalid");
		}
		
		int sizeof = Integer.parseInt(params[0].split("=")[1]);
		for (int i = 0; i < sizeof; i++) {
			
			BusData newBusData = new BusData();
			newBusData.busId = Integer.parseInt(params[6*i + 1].split("=")[1]);
			newBusData.routeId = Integer.parseInt(params[6*i + 2].split("=")[1]);
			newBusData.latitude = Float.parseFloat(params[6*i + 3].split("=")[1]);
			newBusData.longitude = Float.parseFloat(params[6*i + 4].split("=")[1]);
			newBusData.bearing = Integer.parseInt(params[6*i + 5].split("=")[1]);
			newBusData.desc = params[6*i + 6].split("=")[1];
			
			busDataList.add(newBusData);
//			
//			Log.e("***BUS #" + i, "ID: " + newBusData.busId);
//			Log.e("***BUS #" + i, "rID: " + newBusData.routeId);
//			Log.e("***BUS #" + i, "lat: " + newBusData.latitude);
//			Log.e("***BUS #" + i, "lon:" + newBusData.longitude);
//			Log.e("***BUS #" + i, "b:" + newBusData.bearing);
//			Log.e("***BUS #" + i, "desc: " +newBusData.desc);
		}
		
		return busDataList;
		
		
	}
	
	
	private String rawBusLocationRequest() {
		
        // Create a new HttpClient and Post Header
        HttpResponse response = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://transitlive.com/tools/flash_interface.php");
        
        InputStream is = null;

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("getinfo", "allbuses"));
            nameValuePairs.add(new BasicNameValuePair("all", "undefined"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            
            // Execute HTTP Post Request
            response = httpclient.execute(httppost);
            
            is = response.getEntity().getContent();
            
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
        
        if (response == null) {
        	return null;
        }
        
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return sb.toString();
		
	}
	
	
	
	
	private TimerTask mUpdateTimeTask = new TimerTask() {
		
		@Override
		public void run() {
			
			Log.d("***UpdateTask", "Run");
			
	        ArrayList<BusData> busLocations = null;
	        try {
				busLocations = getBusLocations();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		    
			Message data = new Message();
			data.what = 1;
			data.obj = busLocations;
			
	        mDialogHandler.sendMessage(data);
		}
	};
	
	private Handler mDialogHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			
			case 1:
				
				ArrayList<BusData> busLocations = (ArrayList<BusData>) msg.obj;
				
				// We are now back in the UI thread
				
		        List<Overlay> mapOverlays = mapView.getOverlays();
		        Drawable drawable = TransitMapActivity.this.getResources().getDrawable(R.drawable.androidmarker);
		        
				HelloItemizedOverlay itemizedoverlay = new HelloItemizedOverlay(drawable, TransitMapActivity.this);
		        
			
				for (BusData location : busLocations) {

					// Create popup
			        GeoPoint point = new GeoPoint((int) (location.latitude * 1E6), (int) (location.longitude * 1E6));
			        OverlayItem overlayitem = new OverlayItem(point, location.desc, 
			        		String.format("Current position: %s,%s. Heading: %s", location.latitude, location.longitude, location.bearing)
			        );
			        

			        BusMarker mymark = new BusMarker();
			        mymark.setBounds(0, 0, 10, 30);
			        overlayitem.setMarker(mymark);
			        
			        itemizedoverlay.addOverlay(overlayitem);
				}
				
//		        for (int i = 0; i < mapOverlays.size(); i++) {
//		        	mapOverlays.remove(i);
//		        }
		        
		        mapOverlays.add(itemizedoverlay);    
		        
				TransitMapActivity.this.mapView.invalidate();
			}

		};
 };

}
