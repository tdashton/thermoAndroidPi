package com.ashtonandassociates.thermopi.util;

import com.ashtonandassociates.thermopi.api.shared.ApiTemperature;

import java.text.DecimalFormat;

/**
 * Created by theKernel on 09.01.2016.
 */
public class NumberUtil {
	public static final String CONST_DEGREES_CELSIUS = " \u2103";

	static public String formatTemperature(Integer temperature) {
		DecimalFormat form = new DecimalFormat("0.00");
		return form.format(temperature) + NumberUtil.CONST_DEGREES_CELSIUS;
	}

	static public String formatTemperature(Double temperature) {
		DecimalFormat form = new DecimalFormat("0.00");
		return form.format(temperature) + NumberUtil.CONST_DEGREES_CELSIUS;
	}
}
