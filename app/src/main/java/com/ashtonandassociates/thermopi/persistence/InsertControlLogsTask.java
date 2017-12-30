package com.ashtonandassociates.thermopi.persistence;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.ashtonandassociates.thermopi.api.ApiInterface;
import com.ashtonandassociates.thermopi.api.response.ControlLogsResponse;
import com.ashtonandassociates.thermopi.persistence.entity.RecentLog;

/**
 * Created by tashton on 29.12.17.
 */
public class InsertControlLogsTask extends AsyncTask<RecentLog, Void, Integer>
{
    protected Application application;
    protected ApiInterface apiInterface;


    public InsertControlLogsTask(Application application, ApiInterface apiInterface) {
        this.application = application;
        this.apiInterface = apiInterface;
    }

    @Override
    protected Integer doInBackground(RecentLog... models) {
        PiDatabase db = Room.databaseBuilder(this.application,
                PiDatabase.class, "pi-database").fallbackToDestructiveMigration().build();

        db.recentLogDao().insertAll(models);

        return 1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        this.apiInterface.notifyApiListeners(new ControlLogsResponse());
    }
}