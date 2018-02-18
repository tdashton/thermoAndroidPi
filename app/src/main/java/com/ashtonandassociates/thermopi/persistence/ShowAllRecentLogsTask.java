package com.ashtonandassociates.thermopi.persistence;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by theKernel on 03.02.2018.
 */

public class ShowAllRecentLogsTask extends AsyncTask<Void, Void, Integer> {

	public static String TAG = ShowAllRecentLogsTask.class.getSimpleName();

	protected Application application;

	public ShowAllRecentLogsTask(Application application) {
		this.application = application;
	}

	@Override
	protected Integer doInBackground(Void... voids) {
		Log.d(ShowAllRecentLogsTask.TAG, "unhiding all RecentLogs");
		PiDatabase db = Room.databaseBuilder(this.application,
				PiDatabase.class, "pi-database").fallbackToDestructiveMigration().build();

		db.recentLogDao().showAll();
		db.close();

		return 1;
	}
}
