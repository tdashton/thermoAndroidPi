package com.ashtonandassociates.thermopi.util;

import java.text.DecimalFormat;

/**
 * Created by theKernel on 09.01.2016.
 */
public class NumberUtil {

	static public String formatTemperature(Double temperature) {
		DecimalFormat form = new DecimalFormat("0.00");
		return form.format(temperature);
	}

}
