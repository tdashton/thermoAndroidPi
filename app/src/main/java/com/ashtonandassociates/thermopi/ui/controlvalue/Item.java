package com.ashtonandassociates.thermopi.ui.controlvalue;

/**
 * Created by theKernel on 26.12.2017.
 */

public class Item {

	protected String type;

	protected int value;

	public Item(String type, int value) {
		this.type = type;
		this.value = value;
	}

	public String toString() {
		return "" + this.type + this.value + "";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
