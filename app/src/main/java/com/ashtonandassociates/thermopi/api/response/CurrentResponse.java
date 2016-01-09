package com.ashtonandassociates.thermopi.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CurrentResponse {

	public List<Current> data;

	public class Current {
		public String value;
		public String datetime;
		@SerializedName("fk_sensor")
		public String fkSensor;
		public String description;
	}
}
