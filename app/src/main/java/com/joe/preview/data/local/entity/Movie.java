package com.joe.preview.data.local.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.joe.preview.constants.PreviewConstants;
import com.joe.preview.data.local.converters.CastConverter;
import com.joe.preview.data.local.converters.CrewConverter;
import com.joe.preview.data.local.converters.GenreConverter;
import com.joe.preview.data.local.converters.ReviewConverter;
import com.joe.preview.data.local.converters.StringListConverter;
import com.joe.preview.data.local.converters.VideoConverter;
import com.joe.preview.data.remote.Cast;
import com.joe.preview.data.remote.Crew;
import com.joe.preview.data.remote.Genre;
import com.joe.preview.data.remote.Review;
import com.joe.preview.data.remote.Video;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "films", primaryKeys = "id")
public class Movie implements Parcelable {

    @SerializedName("id")
    @Expose
    private Long id;

    @Expose
    private Long page;

    @Expose
    private Long totalPages;

    @SerializedName(value = "header", alternate = {"title", "name"})
    @Expose
    private String header;

    @SerializedName("poster_path")
    @Expose
    private String posterPath;

    @SerializedName(value = "description", alternate = {"overview", "synopsis"})
    private String description;

    @SerializedName("release_date")
    @Expose
    private String releaseDate;

    @SerializedName("genres")
    @Expose
    @TypeConverters(GenreConverter.class)
    private List<Genre> genres;

    @SerializedName("videos")
    @Expose
    @TypeConverters(VideoConverter.class)
    private List<Video> videos;

    @Expose
    @TypeConverters(CrewConverter.class)
    private List<Crew> crews;

    @Expose
    @TypeConverters(CastConverter.class)
    private List<Cast> casts;

    @Expose
    @TypeConverters(ReviewConverter.class)
    private List<Review> reviews;

    @Expose
    @TypeConverters(StringListConverter.class)
    private List<String> categoryTypes;

    @SerializedName("runtime")
    @Expose
    private Long runtime;

    @SerializedName("status")
    @Expose
    private String status;

    public Movie() {
        casts = new ArrayList<>();
        crews = new ArrayList<>();
        genres = new ArrayList<>();
        videos = new ArrayList<>();
        reviews = new ArrayList<>();
        categoryTypes = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getPosterPath() {
        if (posterPath != null && !posterPath.startsWith("http"))
            posterPath = String.format(PreviewConstants.IMAGE_URL, posterPath);
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Crew> getCrews() {
        return crews;
    }

    public void setCrews(List<Crew> crews) {
        this.crews = crews;
    }

    public List<Cast> getCasts() {
        return casts;
    }

    public void setCasts(List<Cast> casts) {
        this.casts = casts;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<String> getCategoryTypes() {
        return categoryTypes;
    }

    public void setCategoryTypes(List<String> categoryTypes) {
        this.categoryTypes = categoryTypes;
    }

    public Long getRuntime() {
        return runtime;
    }

    public void setRuntime(Long runtime) {
        this.runtime = runtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    protected Movie(Parcel in) {
        id = (Long) in.readValue(Long.class.getClassLoader());
        page = (Long) in.readValue(Long.class.getClassLoader());
        totalPages = (Long) in.readValue(Long.class.getClassLoader());
        header = in.readString();
        posterPath = in.readString();
        description = in.readString();
        releaseDate = in.readString();
        genres = in.createTypedArrayList(Genre.CREATOR);
        videos = in.createTypedArrayList(Video.CREATOR);
        crews = in.createTypedArrayList(Crew.CREATOR);
        casts = in.createTypedArrayList(Cast.CREATOR);
        reviews = in.createTypedArrayList(Review.CREATOR);
        categoryTypes = in.createStringArrayList();
        runtime = (Long) in.readValue(Long.class.getClassLoader());
        status = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(this.id);
        parcel.writeValue(this.page);
        parcel.writeValue(this.totalPages);
        parcel.writeString(this.header);
        parcel.writeString(this.posterPath);
        parcel.writeString(this.description);
        parcel.writeString(this.releaseDate);
        parcel.writeTypedList(this.genres);
        parcel.writeTypedList(this.videos);
        parcel.writeTypedList(this.crews);
        parcel.writeTypedList(this.casts);
        parcel.writeTypedList(this.reviews);
        parcel.writeStringList(this.categoryTypes);
        parcel.writeValue(this.runtime);
        parcel.writeString(this.status);
    }

}
