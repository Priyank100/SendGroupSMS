package com.priyank.sendsms.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class SmsModel implements Parcelable {

    private String name;
    private String number;
    private boolean selected;

    public SmsModel() {
    }

    public SmsModel(Parcel in) {
        name = in.readString();
        number = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(number);
    }

    public static final Parcelable.Creator<SmsModel> CREATOR = new Parcelable.Creator<SmsModel>() {
        public SmsModel createFromParcel(Parcel in) {
            return new SmsModel(in);
        }

        public SmsModel[] newArray(int size) {
            return new SmsModel[size];

        }
    };
}
