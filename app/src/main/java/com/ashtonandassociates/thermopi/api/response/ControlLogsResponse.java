package com.ashtonandassociates.thermopi.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class ControlLogsResponse {

	public List<Result> result;
	public Error error;
	public String source;

	public class Result {
		public Result setType(String type) {
			this.type = type;
			return this;
		}

		public Result setParam(Integer param) {
			this.param = param;
			return this;
		}

		public String type;
		public Integer param;
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
