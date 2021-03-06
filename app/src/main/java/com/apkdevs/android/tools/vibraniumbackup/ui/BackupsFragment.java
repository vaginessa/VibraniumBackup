package com.apkdevs.android.tools.vibraniumbackup.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.apkdevs.android.codelib.CLog;
import com.apkdevs.android.codelib.CShell;
import com.apkdevs.android.tools.vibraniumbackup.BackupsAdapter;
import com.apkdevs.android.tools.vibraniumbackup.R;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BackupsFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
	private PackageManager pkgMngr;
	public static List<HashMap<String, Object>> applist;
	private BackupsAdapter listAdaptor;
	private ListView listView;
	private int listposition;
	public File bkpsDir;
	public AlertDialog adialog;

	public BackupsFragment() {}

	public static BackupsFragment newInstance() {return new BackupsFragment();}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_bkps, container, false);
		pkgMngr = BaseActivity.getACAPkgMngr();
		new LoadApplications().execute();
		listView = ((ListView) (rootView.findViewById(R.id.bkps_list)));
		listView.setDividerHeight(0);
		listView.setOnItemClickListener(this);
		return rootView;
	}

	private List<HashMap<String, Object>> convert(List<ApplicationInfo> list) {
		ArrayList<HashMap<String, Object>> applist = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			ApplicationInfo appInfo = list.get(i);
			PackageInfo pI = null;
			try { pI = pkgMngr.getPackageInfo(appInfo.packageName, 0); } catch(PackageManager.NameNotFoundException err) { err.printStackTrace(); }
			HashMap<String, Object> mappedInfo = new HashMap<>();
			mappedInfo.put("name", appInfo.loadLabel(pkgMngr));
			mappedInfo.put("pkg", appInfo.packageName);
			mappedInfo.put("icon", appInfo.loadIcon(pkgMngr));
			mappedInfo.put("type", "app");
			if (pI != null) {
				mappedInfo.put("ver_int", pI.versionCode);
				mappedInfo.put("ver_str", pI.versionName);
			} else {
				CLog.V("PackageInfo for" + appInfo.packageName + " not found.");
			}
			applist.add(mappedInfo);
		}
		List<HashMap<String, Object>> bkdpappslist = getBackedupApps();
		if (bkdpappslist != null) { for (int i = 0; i < bkdpappslist.size(); i++) {	applist.add(bkdpappslist.get(i));	} }
		return applist;
	}

	private List<HashMap<String, Object>> getBackedupApps() {
		List<HashMap<String, Object>> apps = new ArrayList<>();
		bkpsDir = new File(BaseActivity.settings.getString("bkpsdir", Environment.getExternalStorageDirectory().getPath() + "/VB-Apps"));
		if (!bkpsDir.exists()) bkpsDir.mkdir();
		String[] bkps = bkpsDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
			if (filename.substring(filename.lastIndexOf(".")).equals(".prop")) {
				return true;
			}
			CLog.V(filename.substring(filename.lastIndexOf(".")));
			return false;
			}
		});
		if (bkps == null) return apps;
		for (String bkp : bkps) CLog.V(bkp);
		//noinspection StatementWithEmptyBody
		for (String bkp : bkps) {
			//TODO: Create 'get backed up apps' code
			//TODO: Make sure that 'pkg' item is added for Backup() function
		}
		return apps;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		listposition = position;
		Intent i = new Intent(getContext(), AppDetails.class);
		i.putExtra("APP_PKG", applist.get(position).get("pkg").toString());
		startActivity(i);
	}

	public void onClick(View v) {

	}

		/*ProgressDialog pd = new ProgressDialog(getContext());
		pd.show();
		pd.setTitle("Backing up" + appInfo.get("name").toString());
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setMessage("Initializing stuff...");
		pd.setMax(2);
		pd.setProgress(0);
		// Location of commands
		String commandpath = getContext().getFilesDir().getPath();
		// Shell
		CShell shell = new CShell("root");
		pd.setProgress(1);
		// Get path of apk file
			String path;
			Boolean isApp;
			if (appInfo.get("type").toString().equals("app")) {
				path = CShell.execute("pm path " + appInfo.get("pkg").toString()).get(0);
				// Currently: path = "package:$path"
				path = path.substring(path.indexOf(":") + 1);
				// Now: path = $path
				isApp = true;
			} else {
				path = appInfo.get("path").toString();
				isApp = false;
			}
		pd.setProgressStyle(2);
		pd.setMessage("Copying apk file...");
		pd.setMax(1);
		pd.setProgress(0);
		// IF APP: Copy apk file to backups location (with name as "package.apk")
		if (isApp) {
			shell.write("cp " + path + " " +
				bkpsDir + "/" + appInfo.get("pkg").toString() + ".apk");
			Object[] output = shell.getOutput().toArray();
			for (int i = 0; i < output.length; i++) {
				CLog.V(Integer.toString(i) + output[i]);
			}
			shell.reset("root");
		pd.setProgress(1);
			// Example: cp /data/app/com.aide.ui-1/base.apk /sdcard/VB-Bkps/com.aide.ui.apk


			switch (BaseActivity.settings.getString("command", "zip")) {
				case "zip":
					// App backup
					shell.write(commandpath + "/exec_zip -" + BaseActivity.settings.getInt("compression", 9) + " "
						+ bkpsDir + "/" + appInfo.get("pkg").toString() + ".apk");
					output = shell.getOutput().toArray();
					for (int i = 0; i < output.length; i++) {
						CLog.V(Integer.toString(i) + output[i]);
					}
					// Example: /data/data/com.apkdevs.android.tools.vibraniumbackup/files/exec_bin/zip -9 /sdcard/VB-Bkps/com.aide.ui.apk
				case "gzip":
					shell.write(commandpath + "/exec_gzip -" + BaseActivity.settings.getInt("compression", 9) + " "
						+ gContext().getFilesDir().getPath() + "/" + appInfo.get("pkg"));
			}
		}*/

	private class LoadApplications extends AsyncTask<Void, Void, Void> {
			private ProgressDialog progress = null;

			@Override
			protected Void doInBackground(Void... params) {
					applist = convert(pkgMngr.getInstalledApplications(PackageManager.GET_META_DATA));
					listAdaptor = new BackupsAdapter(getContext(), R.layout.bkps_li, applist);
					return null;
			}

			@Override
			protected void onCancelled() { super.onCancelled();	}

			@Override
			protected void onPostExecute(Void result) {
					listView.setAdapter(listAdaptor);
					progress.dismiss();
					super.onPostExecute(result);
			}

			@Override
			protected void onPreExecute() {
					progress = ProgressDialog.show(getContext(), null, "Loading app info...");
					super.onPreExecute();
			}

			@Override
			protected void onProgressUpdate(Void... values) {
					super.onProgressUpdate(values);
			}
	}
}
