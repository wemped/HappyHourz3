package com.drakewempe.happyhourz3;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by drakewempe on 5/10/15.
 */
public class HappyHour {
    public String name;
    public String time;
    public String descrip;
    public int id;
    public LatLng loc;

    public HappyHour(int id,String name,String time,String descrip, LatLng loc){
        this.id = id;
        this.name = name;
        this.time = time;
        this.descrip = descrip;
        this.loc = loc;
    }

}
