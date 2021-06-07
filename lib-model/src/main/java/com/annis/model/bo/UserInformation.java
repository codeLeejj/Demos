package com.annis.model.bo;

import android.os.Parcel;
import android.os.Parcelable;

public class UserInformation implements Parcelable {
    public String name;

    public String address;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.address);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.address = source.readString();
    }

    public UserInformation() {
    }

    protected UserInformation(Parcel in) {
        this.name = in.readString();
        this.address = in.readString();
    }

    public static final Creator<UserInformation> CREATOR = new Creator<UserInformation>() {
        @Override
        public UserInformation createFromParcel(Parcel source) {
            return new UserInformation(source);
        }

        @Override
        public UserInformation[] newArray(int size) {
            return new UserInformation[size];
        }
    };
}
