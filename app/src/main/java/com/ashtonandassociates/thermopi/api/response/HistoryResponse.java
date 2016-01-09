package com.ashtonandassociates.thermopi.api.response;

import android.graphics.Point;

import java.util.List;

public class HistoryResponse {

	List<DataSet> data;

	public class DataSet {
		public String type;
		public String xValueType;
		public List<Point> dataPoints;
	}
}
