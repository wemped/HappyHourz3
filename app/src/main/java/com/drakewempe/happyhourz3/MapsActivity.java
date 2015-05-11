package com.drakewempe.happyhourz3;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /*public GoogleMap mMap; // Might be null if Google Play services APK is not available.
    UiSettings uiSettings;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    public ArrayList<HappyHour> happyHours = new ArrayList<HappyHour>();
    public ArrayList<Marker> markers = new ArrayList<Marker>();
    public boolean first = true;
    public LatLng lastClick = null;
    public ArrayList<Polyline> lines = new ArrayList<Polyline>();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        buildGoogleApiClient();
        setUpMapIfNeeded();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Globals.mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Globals.mGoogleApiClient.isConnected()) {
            Globals.mGoogleApiClient.disconnect();
        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }*/

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (Globals.mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            Globals.mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (Globals.mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        if (!Globals.happyHours.isEmpty()) {
            Globals.happyHours.clear();
        }
        String[] names = getResources().getStringArray(R.array.names);
        String[] times = getResources().getStringArray(R.array.happy_hour_times);
        String[] descrips = getResources().getStringArray(R.array.happy_hour_descrip);
        String[] locations = getResources().getStringArray(R.array.latlgn);

        int length = names.length;
        for (int i=0; i<length; i++){
            String[] locStrings = locations[i].split(",");
            LatLng loc = new LatLng(Double.valueOf(locStrings[0]),Double.valueOf(locStrings[1]));
            HappyHour currHappy = new HappyHour(i,names[i],times[i],descrips[i],loc);
            Globals.happyHours.add(currHappy);
            //Marker currMarker = new Marker(new MarkerOptions().position(loc).title(names[i]).snippet(times[i] + "#" + descrips[i]));
            Globals.markers.add(currHappy.id,Globals.mMap.addMarker(new MarkerOptions().position(loc).title(names[i]).snippet(times[i] + "#" + descrips[i])));
        }

        Globals.mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));

        Globals.mMap.setMyLocationEnabled(true);
        Globals.uiSettings = Globals.mMap.getUiSettings();
        Globals.uiSettings.setZoomControlsEnabled(true);


        Globals.mMap.setMyLocationEnabled(true);
        Location location = Globals.mMap.getMyLocation();
        //final LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());


        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        HappyHoursArrayAdapter spinnerArrayAdapter = new HappyHoursArrayAdapter(this,R.layout.spinner_item,Globals.happyHours);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner.setAdapter(spinnerArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LatLng newLocation;
                if(!Globals.first){
                    Globals.markers.get(position).showInfoWindow();
                    newLocation = Globals.happyHours.get(position).loc;
                    Globals.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 16));

                    Globals.lastClick = newLocation;
                }
                Globals.first = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //
            }
        });

        Globals.mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){

            @Override
        public void onMapClick(LatLng pnt){
                Globals.mMap.addMarker(new MarkerOptions().position(pnt).title(pnt.latitude + "," + pnt.longitude));
                new drawLine(Globals.mMap,Globals.lines,Globals.lastClick,pnt);
                Globals.lastClick = pnt;
            }
        });

        Button clearButton = (Button)findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = Globals.lines.size();
                for (int i=0;i<length;i++){
                    Globals.lines.get(i).remove();
                }
                Globals.lines.clear();
            }
        });

        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,14));

        /*if (location !=null){
            myLocation = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,10));
        }else{
            Log.v("myLocation","location returned null, using default");
            LatLng BELLINGHAM = new LatLng(48.45,-122.29);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(BELLINGHAM).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }*/

    }

    @Override
    public void onConnected(Bundle connectionHint){
        Globals.mLastLocation = LocationServices.FusedLocationApi.getLastLocation(Globals.mGoogleApiClient);
        if (Globals.mLastLocation != null){
            LatLng myLocation = new LatLng(Globals.mLastLocation.getLatitude(),Globals.mLastLocation.getLongitude());
            Globals.mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,13));

        }
    }


    protected synchronized void buildGoogleApiClient() {
        Globals.mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i("err", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i("susp", "Connection suspended");
        Globals.mGoogleApiClient.connect();
    }



    public class HappyHoursArrayAdapter extends ArrayAdapter<HappyHour> {

        ArrayList<HappyHour> happyHours = null;

        public HappyHoursArrayAdapter(Context context, int textViewResourceId, ArrayList<HappyHour> happyHours){
            super(context,textViewResourceId,happyHours);
            this.happyHours = happyHours;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }



        public View getCustomView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater= getLayoutInflater();
            View row=inflater.inflate(R.layout.spinner_item, parent, false);
            TextView label=(TextView)row.findViewById(R.id.title);
            label.setText(happyHours.get(position).name);


            return row;
        }
    }


}
