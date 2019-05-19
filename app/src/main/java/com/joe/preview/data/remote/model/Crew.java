package com.joe.preview.data.remote.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Crew implements Parcelable {

    @SerializedName("credit_id")
    private String creditId;

    private long id;
    private String name;

    @SerializedName("profile_path")
    private String profilePath;

    private String department;
    private String job;

    public Crew() {
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

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    protected Crew(Parcel in) {
        this.creditId = in.readString();
        this.id = in.readLong();
        this.name = in.readString();
        this.profilePath = in.readString();
        this.department = in.readString();
        this.job = in.readString();
    }

    public static final Creator<Crew> CREATOR = new Creator<Crew>() {
        @Override
        public Crew createFromParcel(Parcel in) {
            return new Crew(in);
        }

        @Override
        public Crew[] newArray(int size) {
            return new Crew[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.creditId);
        parcel.writeLong(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.profilePath);
        parcel.writeString(this.department);
        parcel.writeString(this.job);
    }
    
}
