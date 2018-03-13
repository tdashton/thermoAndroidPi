package com.ashtonandassociates.thermopi.persistence;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.util.Log;

import com.ashtonandassociates.thermopi.api.ApiInterface;
import com.ashtonandassociates.thermopi.api.ApiListenerService;
import com.ashtonandassociates.thermopi.api.response.ControlLogsResponse;
import com.ashtonandassociates.thermopi.persistence.entity.RecentLog;

import retrofit.client.Response;

/**
 * Asynchronous task to insert the control values read from the server into
 * the database for display.
 *
 * Created by tashton on 29.12.17.
 */
public class InsertRecentLogsTask extends AsyncTask<RecentLog, Void, Integer>
{
    static String TAG = InsertRecentLogsTask.class.getSimpleName();

    protected Application application;
    protected ApiInterface apiInterface;


    public InsertRecentLogsTask(Application application, ApiInterface apiInterface) {
        this.application = application;
        this.apiInterface = apiInterface;
    }

    @Override
    protected Integer doInBackground(RecentLog... models) {
        PiDatabase db = Room.databaseBuilder(this.application,
                PiDatabase.class, "pi-database").fallbackToDestructiveMigration().build();

        for (RecentLog model : models) {
            RecentLog dbModel = db.recentLogDao().getLogOfType(model.type, model.param);
            if (dbModel != null) {
                model.hide = dbModel.hide;
                db.recentLogDao().update(model);
            } else {
                db.recentLogDao().insert(model);
            }
        }
        Log.d(TAG, String.format("Count RecentLog %s", db.recentLogDao().getCount()));

        db.close();

        return 1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        ApiListenerService.getInstance().notifyApiListeners(new ControlLogsResponse());
    }
}
