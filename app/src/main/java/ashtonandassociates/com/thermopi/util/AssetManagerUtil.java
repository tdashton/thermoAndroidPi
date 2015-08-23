package ashtonandassociates.com.thermopi.util;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by theKernel on 22.08.2015.
 */
public class AssetManagerUtil {

	protected Resources resources = null;
	protected AssetManager assetManager = null;

	protected Properties mProperties;

	public AssetManagerUtil(Resources resource, int rawConfigResource) {
		this.resources = resource;
		this.assetManager = resource.getAssets();
		try {
			InputStream inputStream = resources.openRawResource(rawConfigResource);
			mProperties = new Properties();
			mProperties.load(inputStream);
			Log.d(this.getClass().getSimpleName(), "The properties are now loaded");
			Log.d(this.getClass().getSimpleName(), "properties: " + mProperties);
		} catch (IOException e) {
			Log.e(this.getClass().getSimpleName(), "Failed to open microlog property file");
			e.printStackTrace();
		}
	}

	public String getProperty(String propertyName) {
		return mProperties.getProperty(propertyName);
	}
}