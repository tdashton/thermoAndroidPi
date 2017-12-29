package com.ashtonandassociates.thermopi.persistence;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.ashtonandassociates.thermopi.persistence.entity.RecentLog;

/**
 * Created by tashton on 29.12.17.
 */
public class InsertControlLogsTask extends AsyncTask<RecentLog, Integer, Long>
{
    protected Context ctx;

    public InsertControlLogsTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected Long doInBackground(RecentLog... models) {
        PiDatabase db = Room.databaseBuilder(ctx.getApplicationContext(),
                PiDatabase.class, "pi-database").fallbackToDestructiveMigration().build();

        db.recentLogDao().insertAll(models);

        return null;
    }
}
