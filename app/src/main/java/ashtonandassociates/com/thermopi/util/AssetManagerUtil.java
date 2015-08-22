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

	protected final String PROP_FILE = "config.properties";

//	AssetManager assetManager = resources.getAssets();
//	Resources resources = this.getResources();
	protected Resources resources = null;
	protected AssetManager assetManager = null;

	protected Properties mProperties;

	public AssetManagerUtil(Resources resource) {
	// Read from the /assets directory
		try {
//			InputStream rawResource = resources.openRawResource(R.raw.micrologv2);
			InputStream inputStream = assetManager.open(PROP_FILE);
			mProperties = new Properties();
			mProperties.load(inputStream);
			System.out.println("The properties are now loaded");
			System.out.println("properties: " + mProperties);
		} catch (IOException e) {
			System.err.println("Failed to open microlog property file");
			e.printStackTrace();
		}
	}

	String getProperty(String propertyName) {
		return mProperties.getProperty(propertyName);
	}
}