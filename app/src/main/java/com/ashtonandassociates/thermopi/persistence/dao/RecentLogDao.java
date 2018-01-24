package com.ashtonandassociates.thermopi.persistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ashtonandassociates.thermopi.persistence.entity.RecentLog;

import java.util.List;

@Dao
public interface RecentLogDao
{
	@Query("SELECT * FROM recent_log WHERE type = :type AND hide = 0 ORDER BY count DESC")
	List<RecentLog> getAllOfType(String type);

	@Query("SELECT * FROM recent_log")
	List<RecentLog> getAll();

	@Query("SELECT count(*) FROM recent_log")
	Double getCount();

	@Query("DELETE FROM recent_log")
	void removeAll();

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertAll(RecentLog... recentLogs);

	@Update(onConflict = OnConflictStrategy.REPLACE)
	void update(RecentLog recentLog);

	@Query("UPDATE recent_log set hide = 1 WHERE type = :type AND param = :param")
	void hide(String type, String param);
}
