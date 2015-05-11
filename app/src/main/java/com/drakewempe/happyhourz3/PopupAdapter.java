package com.drakewempe.happyhourz3;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by drakewempe on 5/10/15.
 */
public class PopupAdapter implements GoogleMap.InfoWindowAdapter{
    private View popup=null;
    private LayoutInflater inflater = null;

    PopupAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getInfoContents(Marker marker) {
        if (popup == null) {
            popup=inflater.inflate(R.layout.popup, null);
        }
        if(marker.getSnippet()!= null){


            String[] timeDescrip = marker.getSnippet().split("#");

            TextView tv = (TextView) popup.findViewById(R.id.title);
            tv.setText(marker.getTitle());

            tv = (TextView) popup.findViewById(R.id.time);
            tv.setText(timeDescrip[0]);

            tv = (TextView) popup.findViewById(R.id.descrip);
            tv.setText(timeDescrip[1]);
        }else{
            TextView tv = (TextView) popup.findViewById(R.id.title);
            tv.setText(marker.getTitle());

            tv = (TextView) popup.findViewById(R.id.time);
            tv.setText("No info available");

            tv = (TextView) popup.findViewById(R.id.descrip);
            tv.setText("n/a");
        }
        new drawLine(Globals.mMap,Globals.lines,Globals.lastClick,marker.getPosition());
        Globals.lastClick = marker.getPosition();
        return(popup);
    }
}
