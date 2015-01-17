package ar.com.newsan.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static String LOG_TAG = MainActivity.class.getName();
    private ProgressDialog progressDialog;
    private ArrayList<String> packages;
    private TextView information;
    private TextView userAgent;
    private ListView listDrm;
    private ListView listApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Objects
        information = (TextView) findViewById(R.id.textView_info);
        userAgent = (TextView) findViewById(R.id.textView_ua);
        listDrm = (ListView) findViewById(R.id.listView1);
        listApps = (ListView) findViewById(R.id.listView2);

        // Values
        information.setText(GeneralPurpose.getDeviceInformation(MainActivity.this));
        information.setTextIsSelectable(true);
        userAgent.setText(GeneralPurpose.getDefaultUserAgent(MainActivity.this));
        userAgent.setTextIsSelectable(true);
        listDrm.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, GeneralPurpose.getDrmList(MainActivity.this)));

        // Long task (load and populate data on separate thread)
        new GetInstalledApps().execute();

        // Log
        Log.d(LOG_TAG, "Device isRooted: " + GeneralPurpose.isDeviceRooted());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
     * Background task which loads data in a background thread.
     */
    private class GetInstalledApps extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // show the dialog to indicate loading
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (packages==null || packages.size() == 0) {
                packages = new ArrayList<String>();
                for (PackageInfoStruct packageInfoStruct : getInstalledPackages())
                    packages.add(packageInfoStruct.packageName);
            }

            // Sort objects by appName
            Collections.sort(packages, new Comparator<String>() {
                @Override
                public int compare(String object1, String object2) {
                    return object1.compareToIgnoreCase(object2);
                }
            });
            return true;
        }

        private ArrayList<PackageInfoStruct> getInstalledPackages() {
            final ArrayList<PackageInfoStruct> packages = new ArrayList<PackageInfoStruct>();

            List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
            progressDialog.setMax(packs.size());
            for (int i=0;i < packs.size();i++) {
                PackageInfo p = packs.get(i);

                if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                    //This is System application
                    if (true) {
                        PackageInfoStruct newInfo = new PackageInfoStruct();
                        newInfo.appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                        newInfo.packageName = p.packageName;
                        newInfo.versionName = p.versionName;
                        newInfo.versionCode = p.versionCode;
                        newInfo.firstInstallTime = p.firstInstallTime;
                        newInfo.lastUpdateTime = p.lastUpdateTime;
                        newInfo.sourceDir = p.applicationInfo.publicSourceDir;
                        newInfo.systemApp = true;
                        newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());

                        packages.add(newInfo);
                    }
                }
                else {
                    //This app is installed by user
                    if (true) {
                        PackageInfoStruct newInfo = new PackageInfoStruct();
                        newInfo.appName = p.applicationInfo.loadLabel(getPackageManager()).toString();
                        newInfo.packageName = p.packageName;
                        newInfo.versionName = p.versionName;
                        newInfo.versionCode = p.versionCode;
                        newInfo.firstInstallTime = p.firstInstallTime;
                        newInfo.lastUpdateTime = p.lastUpdateTime;
                        newInfo.sourceDir = p.applicationInfo.publicSourceDir;
                        newInfo.systemApp = false;
                        newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());

                        packages.add(newInfo);
                    }
                }
                progressDialog.setProgress(i);
            }

            return packages;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            listApps.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                    android.R.layout.simple_list_item_1, packages));

            // Event onItemClick
            listApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                    Toast.makeText(MainActivity.this, "Position: " + position, Toast.LENGTH_SHORT).show();
                }
            });

            // ContextMenu
            registerForContextMenu(listApps);

            // Remove dialog
            progressDialog.dismiss();
        }
    }

}
