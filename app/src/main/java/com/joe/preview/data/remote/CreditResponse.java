package com.joe.preview.data.remote;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class CreditResponse implements Parcelable {

    private List<Cast> casts;
    private List<Crew> crews;

    public CreditResponse() {
    }

    public CreditResponse(List<Cast> casts, List<Crew> crews) {
        this.casts = casts;
        this.crews = crews;
    }

    public List<Cast> getCasts() {
        return casts;
    }

    public void setCasts(List<Cast> casts) {
        this.casts = casts;
    }

    public List<Crew> getCrews() {
        return crews;
    }

    public void setCrews(List<Crew> crews) {
        this.crews = crews;
    }

    protected CreditResponse(Parcel in) {
        this.casts = in.createTypedArrayList(Cast.CREATOR);
        this.crews = in.createTypedArrayList(Crew.CREATOR);
    }

    public static final Creator<CreditResponse> CREATOR = new Creator<CreditResponse>() {
        @Override
        public CreditResponse createFromParcel(Parcel in) {
            return new CreditResponse(in);
        }

        @Override
        public CreditResponse[] newArray(int size) {
            return new CreditResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(this.casts);
        parcel.writeTypedList(this.crews);
    }

}
