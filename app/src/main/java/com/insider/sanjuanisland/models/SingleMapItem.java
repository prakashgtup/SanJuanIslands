package com.insider.sanjuanisland.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 *
 */
public class SingleMapItem implements ClusterItem {
    public final String name;
    public final int profilePhoto;
    private final LatLng mPosition;
     boolean Parent_Location;
    public final int locationId;


    public SingleMapItem(LatLng position, String name, int pictureResource, boolean isparent, int locationId) {
        this.name = name;
        profilePhoto = pictureResource;
        mPosition = position;
        Parent_Location =isparent;
        this.locationId = locationId;
    }


    @Override
    public LatLng getPosition() {
        return mPosition;
    }


    public boolean getParent() {
        return Parent_Location;
    }
    public Integer getLocationID() {
        return locationId;
    }
}

