package com.drakewempe.happyhourz3;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by drakewempe on 5/11/15.
 */
public class drawLine {
    public drawLine(GoogleMap mMap,ArrayList<Polyline> lines,LatLng first, LatLng second){
        Log.v("drawLine", "begin");
        if (first != null && second !=null){
            lines.add(mMap.addPolyline(new PolylineOptions().add(first,second).width(Float.valueOf(".5")).color(Color.RED)));
            return;
        }
        Log.v("drawLine","argument null");
    }
}
