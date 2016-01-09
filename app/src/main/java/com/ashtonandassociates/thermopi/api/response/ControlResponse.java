package com.ashtonandassociates.thermopi.api.response;

public class ControlResponse {

	public String result;
	public Error error;

	public class Error {
		public Integer code;
		public String text;
	}
}
