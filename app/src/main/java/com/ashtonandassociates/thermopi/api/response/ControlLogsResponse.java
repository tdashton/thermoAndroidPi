package com.ashtonandassociates.thermopi.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class ControlLogsResponse {

	public List<Result> result;
	public Error error;

	public class Result {
		public String type;

		public String param;

		@SerializedName("countx")
		public Integer count;

		public String toString() {
			return "" + this.type + " " + this.param + "";
		}
	}

	public class Error {
		public Integer code;
		public String text;
	}
}
