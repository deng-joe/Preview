package com.joe.preview.data.remote;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Cast implements Parcelable {

    @SerializedName("cast_id")
    private long castId;

    private String character;

    @SerializedName("credit_id")
    private String creditId;

    private long id;
    private String name;
    private int order;

    @SerializedName("profile_path")
    private String profilePath;

    public Cast() {
    }

    public long getCastId() {
        return castId;
    }

    public void setCastId(long castId) {
        this.castId = castId;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    protected Cast(Parcel in) {
        this.castId = in.readLong();
        this.character = in.readString();
        this.creditId = in.readString();
        this.id = in.readLong();
        this.name = in.readString();
        this.order = in.readInt();
        this.profilePath = in.readString();
    }

    public static final Creator<Cast> CREATOR = new Creator<Cast>() {
        @Override
        public Cast createFromParcel(Parcel in) {
            return new Cast(in);
        }

        @Override
        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.castId);
        parcel.writeString(this.character);
        parcel.writeString(this.creditId);
        parcel.writeLong(this.id);
        parcel.writeString(this.name);
        parcel.writeInt(this.order);
        parcel.writeString(this.profilePath);
    }

}
