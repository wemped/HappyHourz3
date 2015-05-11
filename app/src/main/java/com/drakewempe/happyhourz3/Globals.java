package com.drakewempe.happyhourz3;

import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;

/**
 * Created by drakewempe on 5/11/15.
 */
public class Globals{
    public static GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public static UiSettings  uiSettings;
    public static GoogleApiClient mGoogleApiClient;
    public static Location mLastLocation;
    public static ArrayList<HappyHour> happyHours = new ArrayList<HappyHour>();
    public static ArrayList<Marker> markers = new ArrayList<Marker>();
    public static boolean first = true;
    public static LatLng lastClick = null;
    public static ArrayList<Polyline> lines = new ArrayList<Polyline>();
}
