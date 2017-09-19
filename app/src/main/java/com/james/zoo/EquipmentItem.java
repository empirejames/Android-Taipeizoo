package com.james.zoo;

import android.content.Context;

/**
 * Created by 101716 on 2017/6/16.
 */

public class EquipmentItem {

    private String s_item, s_summary, s_location,s_gps, s_geo, s_pic_URL, killer;
    private int _id;
    public EquipmentItem(String s_item , String s_summary, String s_location,String s_gps, String s_geo, String killer, String s_pic_URL){
        this.s_item = s_item;
        this.s_summary = s_summary;
        this.s_location = s_location;
        this.s_gps = s_gps;
        this.s_geo = s_geo;
        this.killer = killer;
        this.s_pic_URL = s_pic_URL;
    }
    public int getId() {
        return _id;
    }
    public void setId(int id) {
        this._id = id;
    }
    public String getS_item() {
        return s_item;
    }
    public String getS_summary() {
        return s_summary;
    }
    public String getS_location() {
        return s_location;
    }
    public String getS_gps() {
        return s_gps;
    }
    public String getS_geo() {
        return s_geo;
    }
    public String getKiller() {
        return killer;
    }
    public String getS_pic_URL() {
        return s_pic_URL;
    }
}
