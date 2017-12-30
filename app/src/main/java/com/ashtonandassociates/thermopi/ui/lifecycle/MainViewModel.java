package com.ashtonandassociates.thermopi.ui.lifecycle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.ashtonandassociates.thermopi.persistence.PiDatabase;
import com.ashtonandassociates.thermopi.persistence.entity.RecentLog;

/**
 *
 */
public class MainViewModel extends AndroidViewModel
{
	protected Map<String, MutableLiveData<List<RecentLog>>> logsCache;

	public MainViewModel(@NonNull Application application) {
		super(application);
	}

	public LiveData<List<RecentLog>> getLogs(String type) {
		return this.getLogs(type, false);
	}

	public LiveData<List<RecentLog>> getLogs(String type, boolean forceRefresh) {
		if (this.logsCache == null) {
			this.logsCache = new HashMap<>();
		}
		if (!this.logsCache.containsKey(type)) {
			this.logsCache.put(type, new MutableLiveData<List<RecentLog>>());
			forceRefresh = true;
		}
		if (forceRefresh == true) {
			loadLogs(type);
		}
		return this.logsCache.get(type);
	}

	private void loadLogs(String type) {
		LoadControlLogsTask task = new LoadControlLogsTask(this.getApplication(), type);
		task.execute(type);
	}

	private class LoadControlLogsTask extends AsyncTask<String, Void, List<RecentLog>> {

		protected Application application;

		protected String type;

		public LoadControlLogsTask(Application application, String type) {
			this.application = application;
			this.type = type;
		}

		@Override
		protected List<RecentLog> doInBackground(String[] types) {
			PiDatabase db = Room.databaseBuilder(this.application.getApplicationContext(),
					PiDatabase.class, "pi-database").build();

			return db.recentLogDao().getAllOfType(types[0]);
		}

		protected void onPostExecute(List<RecentLog> result) {
			MainViewModel.this.logsCache.get(type).postValue(result);
		}
	}
}
