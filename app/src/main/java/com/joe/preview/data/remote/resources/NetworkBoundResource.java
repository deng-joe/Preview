package com.joe.preview.data.remote.resources;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public abstract class NetworkBoundResource<ResultType, RequestType> {
    /**
     * ResultType: Type for the Resource data
     * RequestType: Type for the API resource
     */

    private Observable<Resource<ResultType>> results;

    @MainThread
    protected NetworkBoundResource() {
        Observable<Resource<ResultType>> source;
        if (shouldFetch())
            source = createCall()
                    .subscribeOn(Schedulers.io())
                    .doOnNext(apiResponse -> saveCallResult(processResponse(apiResponse)))
                    .flatMap(apiResponse -> loadFromDb().toObservable().map(Resource::success))
                    .doOnError(t -> onFetchFailed())
                    .onErrorResumeNext(t -> {
                        return loadFromDb().toObservable().map(data -> Resource.error(t.getMessage(), data));
                    })
                    .observeOn(AndroidSchedulers.mainThread());
        else
            source = loadFromDb().toObservable().map(Resource::success);

        results = Observable.concat(loadFromDb().toObservable().map(Resource::loading).take(1), source);
    }

    public Observable<Resource<ResultType>> getAsObservable() {
        return results;
    }

    private void onFetchFailed() {

    }

    @WorkerThread
    private RequestType processResponse(Resource<RequestType> response) {
        return response.data;
    }

    /**
     * Updates/inserts the result of the API into the local database.
     * The method is called when the data from the remote server is successfully fetched.
     */
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    // Returns true if it is needed to fetch the data from a remote server, otherwise it returns false
    @MainThread
    protected abstract boolean shouldFetch();

    // Returns the data from the local database
    @NonNull
    @MainThread
    protected abstract Flowable<ResultType> loadFromDb();

    // Responsible for creating a remote server call which is responsible for fetching the data from the server
    @NonNull
    @MainThread
    protected abstract Observable<Resource<RequestType>> createCall();

}
