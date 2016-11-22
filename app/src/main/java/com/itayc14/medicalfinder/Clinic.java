package com.itayc14.medicalfinder;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by itaycohen on 21.11.2016.
 */

public class Clinic implements Parcelable{
    private String name;
    private String tel;
    private String address;
    private String freeText;
    private String img;
    private double lat;
    private double lng;
    private String currentDistance;

    public Clinic(JSONObject clinic){

        try {
            name = clinic.getString("name");
            tel = clinic.getString("tel");
            address = clinic.getString("address");
            freeText = clinic.getString("free text");
            img = clinic.getString("img");
            lat = clinic.getDouble("lat");
            lng = clinic.getDouble("lng");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //=========================================//

    private Clinic(Parcel in) {
        name = in.readString();
        tel = in.readString();
        address = in.readString();
        freeText = in.readString();
        img = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public static final Creator<Clinic> CREATOR = new Creator<Clinic>() {
        @Override
        public Clinic createFromParcel(Parcel in) {
            return new Clinic(in);
        }

        @Override
        public Clinic[] newArray(int size) {
            return new Clinic[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(tel);
        dest.writeString(address);
        dest.writeString(freeText);
        dest.writeString(img);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }

    //============================================//

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public String getAddress() {
        return address;
    }

    public String getFreeText() {
        return freeText;
    }

    public String getImg() {
        return img;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }


    public String getCurrentDistance() {
        return currentDistance;
    }

    public void setCurrentDistance(String currentDistance) {
        this.currentDistance = currentDistance;
    }
}
