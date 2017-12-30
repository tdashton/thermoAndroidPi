package com.ashtonandassociates.thermopi.util;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by theKernel on 30.12.2017.
 */

public class HashUtil {

	private static final String TAG = HashUtil.class.getSimpleName();

	protected static HashUtil instance;

	public static HashUtil getInstance() {
		if (HashUtil.instance == null) {
			HashUtil.instance  = new HashUtil();
		}
		return HashUtil.instance;
	}

	protected HashUtil() {}

	public String getMessageDigestHash(String input) {
		String retVal = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			//Log.v(TAG, "hashme:" + hashMe);

			byte[] bytes = md.digest(input.getBytes());
			StringBuilder sb = new StringBuilder(2 * bytes.length);
			for (byte b : bytes) {
				sb.append("0123456789abcdef".charAt((b & 0xF0) >> 4));
				sb.append("0123456789abcdef".charAt((b & 0x0F)));
			}

			retVal = sb.toString();
		} catch(NoSuchAlgorithmException nsae) {
			Log.e(TAG, nsae.toString());
		}

		return retVal;
	}
}
