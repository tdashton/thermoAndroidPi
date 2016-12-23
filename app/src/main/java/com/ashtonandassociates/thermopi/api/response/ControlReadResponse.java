package com.ashtonandassociates.thermopi.api.response;

import java.util.Date;
import java.util.List;

public class ControlReadResponse {

	public List<Result> result;
	public Error error;
	public String source;

	public class Result {
		public Date datetime;
		public String type;
		public String param;
	}

	public class Error {
		public Integer code;
		public String text;
	}
}
