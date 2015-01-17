package ar.com.newsan.utility;

import android.graphics.drawable.Drawable;

/**
 * Created by ernesto.barracchia on 06/01/2015.
 * Class to manage Package information
 */
public class PackageInfoStruct {
    String appName = "";
    String packageName = "";
    String versionName = "";
    int versionCode = 0;
    long firstInstallTime = -1;
    long lastUpdateTime = -1;
    String sourceDir;
    boolean systemApp;
    Drawable icon;
}
