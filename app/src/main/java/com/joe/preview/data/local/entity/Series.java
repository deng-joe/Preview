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
import com.joe.preview.data.local.converters.SeriesConverter;
import com.joe.preview.data.local.converters.StringListConverter;
import com.joe.preview.data.local.converters.VideoConverter;
import com.joe.preview.data.remote.model.Cast;
import com.joe.preview.data.remote.model.Crew;
import com.joe.preview.data.remote.model.Genre;
import com.joe.preview.data.remote.model.Review;
import com.joe.preview.data.remote.model.Video;

import java.util.ArrayList;
import java.util.List;

@Entity(tableName = "tv_shows", primaryKeys = "id")
public class Series implements Parcelable {

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

    @Expose
    @TypeConverters(SeriesConverter.class)
    private List<Series> similarSeries;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("number_of_seasons")
    @Expose
    private Long numberOfSeasons;

    @SerializedName("vote_average")
    @Expose
    private double voteAverage;

    public Series() {
        casts = new ArrayList<>();
        crews = new ArrayList<>();
        genres = new ArrayList<>();
        videos = new ArrayList<>();
        reviews = new ArrayList<>();
        categoryTypes = new ArrayList<>();
        similarSeries = new ArrayList<>();
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

    public List<Series> getSimilarSeries() {
        return similarSeries;
    }

    public void setSimilarSeries(List<Series> similarSeries) {
        this.similarSeries = similarSeries;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(Long numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public boolean isLastPage() {
        return getPage() >= getTotalPages();
    }

    protected Series(Parcel in) {
        id = (Long) in.readValue(Long.class.getClassLoader());
        page = (Long) in.readValue(Long.class.getClassLoader());
        totalPages = (Long) in.readValue(Long.class.getClassLoader());
        header = in.readString();
        posterPath = in.readString();
        description = in.readString();
        genres = in.createTypedArrayList(Genre.CREATOR);
        videos = in.createTypedArrayList(Video.CREATOR);
        crews = in.createTypedArrayList(Crew.CREATOR);
        casts = in.createTypedArrayList(Cast.CREATOR);
        reviews = in.createTypedArrayList(Review.CREATOR);
        categoryTypes = in.createStringArrayList();
        similarSeries = in.createTypedArrayList(Series.CREATOR);
        status = in.readString();
        numberOfSeasons = (Long) in.readValue(Long.class.getClassLoader());
        voteAverage = in.readDouble();
    }

    public static final Creator<Series> CREATOR = new Creator<Series>() {
        @Override
        public Series createFromParcel(Parcel in) {
            return new Series(in);
        }

        @Override
        public Series[] newArray(int size) {
            return new Series[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(id);
        parcel.writeValue(page);
        parcel.writeValue(totalPages);
        parcel.writeString(header);
        parcel.writeString(posterPath);
        parcel.writeString(description);
        parcel.writeTypedList(genres);
        parcel.writeTypedList(videos);
        parcel.writeTypedList(crews);
        parcel.writeTypedList(casts);
        parcel.writeTypedList(reviews);
        parcel.writeStringList(categoryTypes);
        parcel.writeTypedList(similarSeries);
        parcel.writeString(status);
        parcel.writeValue(numberOfSeasons);
        parcel.writeDouble(voteAverage);
    }

}
