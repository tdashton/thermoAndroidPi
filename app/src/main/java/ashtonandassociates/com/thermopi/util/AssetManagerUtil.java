package ashtonandassociates.com.thermopi.util;

import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by theKernel on 22.08.2015.
 */
public class AssetManagerUtil {

//	AssetManager assetManager = resources.getAssets();
//	Resources resources = this.getResources();
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
			System.out.println("The properties are now loaded");
			System.out.println("properties: " + mProperties);
		} catch (IOException e) {
			System.err.println("Failed to open microlog property file");
			e.printStackTrace();
		}
	}

	public String getProperty(String propertyName) {
		return mProperties.getProperty(propertyName);
	}
}