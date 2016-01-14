package com.ashtonandassociates.thermopi.api.shared;

/**
 * Created by theKernel on 09.01.2016.
 */
public class ApiTemperature {

	public static final int CONST_API_SCALE = 0; // mal 10000
	public static final int CONST_DEFAULT_SCALE = 1; // normal

	private Integer temperature;
	private int scale = CONST_DEFAULT_SCALE;

	public ApiTemperature(Integer temperature, int scale) {
		this.temperature = temperature;
		this.scale = scale;
	}

	/**
	 *
	 * @param param
	 * @param scale
	 */
	public void setTemperature(Integer param, int scale) {
		this.scale = scale;
		this.temperature = param;
	}

	public Integer getTemperature(int scale) {
		Integer retVal = null;
		if(this.scale == scale) {
			retVal = this.temperature;
		} else if(this.scale == CONST_DEFAULT_SCALE && scale == CONST_API_SCALE) {
			retVal = this.temperature.intValue() * 1000;
		} else if(this.scale == CONST_API_SCALE && scale == CONST_DEFAULT_SCALE) {
			retVal = this.temperature.intValue() / 1000;
		}
		return retVal.intValue();
	}

}
