package com.ashtonandassociates.thermopi.persistence;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;

import com.ashtonandassociates.thermopi.api.ApiInterface;
import com.ashtonandassociates.thermopi.persistence.entity.RecentLog;
import com.ashtonandassociates.thermopi.ui.list.RecentLogAdapter;

/**
 * Created by theKernel on 24.01.2018.
 */

public class HideRecentLogTask extends AsyncTask<RecentLog, Void, Integer> {

	public static String TAG = HideRecentLogTask.class.getSimpleName();

	protected Application application;

	public HideRecentLogTask(Application application) {
		this.application = application;
	}

	@Override
	protected Integer doInBackground(RecentLog... models) {
		PiDatabase db = Room.databaseBuilder(this.application,
				PiDatabase.class, "pi-database").fallbackToDestructiveMigration().build();

		for (RecentLog log: models) {
			db.recentLogDao().hide(log.type, log.param);
		}
		db.close();

		return 1;
	}

//	@Override
//	protected void onPostExecute(Integer integer) {
//		this.recentLogAdapter.notifyDataSetChanged();
//	}
}
