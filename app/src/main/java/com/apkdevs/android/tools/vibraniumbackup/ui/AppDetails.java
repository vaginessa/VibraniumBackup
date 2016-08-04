package com.apkdevs.android.tools.vibraniumbackup.ui;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.apkdevs.android.codelib.CAppCompatActivity;
import com.apkdevs.android.codelib.CLog;
import com.apkdevs.android.codelib.CShell;
import com.apkdevs.android.tools.vibraniumbackup.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppDetails extends CAppCompatActivity {
		static String pkg;
		static CShell shell;
		static ArrayList<HashMap<String, Object>> bkps;
		static PackageManager pm;
		// Graphical
			static Button bBkp;
			static LinearLayoutCompat llBkps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Base setup
			super.onCreate(savedInstanceState);
			setContentView(R.layout.lay_appd);
			setSAB((Toolbar) findView(R.id.lappdm_toolbar));
			getSAB().setDisplayHomeAsUpEnabled(true);
		// Setup variables
			shell = new CShell("root");
			pm = BaseActivity.getACAPkgMngr();
			List<ApplicationInfo> list = pm.getInstalledApplications(PackageManager.GET_META_DATA);
			// App package
				if (savedInstanceState == null) {
					Bundle extras = getIntent().getExtras();
					if(extras == null) { CLog.E("No package given in APP_PKG"); finish(); } else { pkg = extras.getString("APP_PKG"); }
				} else { pkg = (String) savedInstanceState.getSerializable("APP_PKG"); }
			// Backups
				bkps = new ArrayList<>();
				for (int i = 0; i < list.size(); i++) {
					ApplicationInfo appInfo = list.get(i);
					HashMap<String, Object> mappedInfo = new HashMap<>();
					mappedInfo.put("name", appInfo.loadLabel(pm));
					mappedInfo.put("pkg", appInfo.packageName);
					mappedInfo.put("icon", appInfo.loadIcon(pm));
					mappedInfo.put("type", "app");
					bkps.add(mappedInfo);
				}
				//List<HashMap<String, Object>> bkdpappslist = getBackedupApps();
				//if (bkdpappslist != null) { for (int i = 0; i < bkdpappslist.size(); i++) {	bkps.add(bkdpappslist.get(i));	} }

	}
	
}
