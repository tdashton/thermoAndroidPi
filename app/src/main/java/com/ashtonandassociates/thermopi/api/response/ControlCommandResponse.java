package com.ashtonandassociates.thermopi.api.response;

public class ControlCommandResponse {

	public String result;
	public Error error;

	public class Error {
		public Integer code;
		public String text;
	}
}
