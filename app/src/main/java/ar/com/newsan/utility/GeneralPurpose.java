package ar.com.newsan.utility;

import android.content.Context;
import android.drm.DrmManagerClient;
import android.drm.DrmSupportInfo;
import android.webkit.WebView;

import java.util.ArrayList;

public class GeneralPurpose {

    private static String LOG_TAG = GeneralPurpose.class.getName();	
	
	static public String getDeviceUserAgent() {
		return System.getProperty("http.agent");
	}
	
	static public String getDefaultUserAgent(Context context){
		return new WebView(context).getSettings().getUserAgentString();
	}
	
	static public String getAndroidVersion() {
		return android.os.Build.VERSION.RELEASE;
	}
	
	static public String getDeviceModelName() {
		return android.os.Build.MODEL;
	}
	static public String getDeviceManufacturer() {
		return android.os.Build.MANUFACTURER;
	}
	
	static public String getDeviceInformation(Context context) {
		String text_splitter = context.getResources().getString(R.string.text_splitter);
		String information_android_version = context.getResources().getString(R.string.information_android_version);
		String information_model_name = context.getResources().getString(R.string.information_model_name);
		String information_manufacturer = context.getResources().getString(R.string.information_manufacturer);
		
		return information_android_version+": "+getAndroidVersion()+text_splitter+
			   information_model_name+": "+getDeviceModelName()+text_splitter+
			   information_manufacturer+": "+getDeviceManufacturer();
	}

    static public String[] getDrmList(Context context) {
        return new DrmManagerClient(context).getAvailableDrmEngines();
    }

    static public boolean isDeviceRooted() {
        return new Root().isDeviceRooted();
    }
}
