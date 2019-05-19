package com.joe.preview.data.remote;

import com.google.gson.annotations.SerializedName;
import com.joe.preview.data.local.entity.Series;

import java.util.ArrayList;
import java.util.List;

public class SeriesApiResponse {

    private long page;

    @SerializedName("total_pages")
    private long totalPages;

    @SerializedName("total_results")
    private long totalResults;

    private List<Series> results;

    public SeriesApiResponse() {
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

    public List<Series> getResults() {
        return results;
    }

    public void setResults(List<Series> results) {
        this.results = results;
    }

}
