package csilva2810.udacity.com.popularmovies.utils;

/**
 * Created by carlinhos on 1/12/17.
 */

public interface AsyncTaskDelegate {
    void onProcessPrepare();
    void onProcessFinish(Object output, String taskType);
}
