package com.joe.preview.data.remote.model;

import com.google.gson.annotations.SerializedName;
import com.joe.preview.data.local.entity.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieApiResponse {

    private long page;

    @SerializedName("total_pages")
    private long totalPages;

    @SerializedName("total_results")
    private long totalResults;

    private List<Movie> results;

    public MovieApiResponse() {
        results = new ArrayList<>();
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

}
