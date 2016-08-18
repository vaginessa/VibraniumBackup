package com.apkdevs.android.tools.vibraniumbackup.ui;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.apkdevs.android.codelib.CAppCompatActivity;
import com.apkdevs.android.codelib.CLog;
import com.apkdevs.android.codelib.CShell;
import com.apkdevs.android.tools.vibraniumbackup.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AppDetails extends CAppCompatActivity {
		static String pkg, name, ver_str;
		static int ver_int;
		static Boolean type;
		static Drawable icon;
		static ArrayList<HashMap<String, Object>> bkps;
		static File bkpsDir;
		// Graphical
			static Button bBkp;
			static LinearLayoutCompat llBkps;
			static TextView tPkg;
			static TextView tVer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Base setup
			super.onCreate(savedInstanceState);
			setContentView(R.layout.lay_appd);
			setSAB((Toolbar) findView(R.id.lappdm_toolbar));
			getSAB().setDisplayHomeAsUpEnabled(false);
		// Setup variables
			List<HashMap<String, Object>> list = BackupsFragment.applist;
			bBkp = (Button) findView(R.id.lappdm_bkp);
			tPkg = (TextView) findView(R.id.lappdm_pkg);
			tVer = (TextView) findView(R.id.lappdm_ver);
			bkpsDir = new File(BaseActivity.settings.getString("bkpsdir", Environment.getExternalStorageDirectory().getPath() + "/VB-Apps"));
			// App package
				if (savedInstanceState == null) {
					Bundle extras = getIntent().getExtras();
					if(extras == null) { CLog.E("No package given in APP_PKG"); finish(); } else { pkg = extras.getString("APP_PKG"); }
				} else { pkg = (String) savedInstanceState.getSerializable("APP_PKG"); }
			// Backups
				bkps = new ArrayList<>();
				for (int i = 0; i < list.size(); i++) {
					HashMap<String, Object> aI = list.get(i);
					if (aI.get("pkg").toString().equals(pkg)) {
						icon = (Drawable) aI.get("icon");
						name = aI.get("name").toString();
						ver_int = (int) aI.get("ver_int");
						ver_str = aI.get("ver_str").toString();
						break;
					}
				}
			getSAB().setTitle(name);
			icon.setBounds(48, 48, 48, 48);
			getSAB().setLogo(icon);
			tPkg.setText(pkg);
			tVer.setText(ver_str + ": " + String.format("%d", ver_int));
		bBkp.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {new Backup(AppDetails.this).execute();}});
	}
	private class Backup extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pd;

		Backup(AppDetails a) {pd = new ProgressDialog(a);}

		@Override
		protected Void doInBackground(Void... params) {
			pd.setMax(2);
			pd.setProgress(0);
			DateFormat df = new SimpleDateFormat("HHmmss-yyMMdd");
			Date date = new Date();
			File prop_f = new File(bkpsDir.getPath() + "/" + pkg + "-" + df.format(date) + ".prop");
			try { prop_f.createNewFile(); } catch(IOException err) { CLog.V("propFile.createNewFile() failed, using SuperUser"); CShell.execute("su -c touch " + prop_f.getPath()); }
			FileWriter prop_w;
			try {
				prop_w = new FileWriter(prop_f);
				prop_w.write("name=" + name);
				prop_w.write("verint=" + ver_int);
				prop_w.write("verstr=" + ver_str);
			} catch(IOException err) {
				CLog.V("FileWriter for prop file failed, using shell commands");
				CShell shell = new CShell("root");
				shell.write("echo \"name=" + name + "\" > " + prop_f.getPath());
				shell.write("echo \"verint=" + ver_int + "\" > " + prop_f.getPath());
				shell.write("echo \"verstr=" + ver_str + "\" > " + prop_f.getPath());
			}
			runOnUiThread(new Runnable() {@Override	public void run() {pd.setMessage("Copying app to backups directory");}});
			CLog.V("Copying");
			String dest = bkpsDir + "/" + pkg + "-" + df.format(date) + ".apk";
			String path = CShell.execute("/system/xbin/su -c pm path " + pkg).get(0).substring(8);
			CLog.V("  Running shell & waiting");
			new CShell("root").write("cp " + path + " " + dest).waitForEnd();
			CLog.V("Done. Zipping");
			runOnUiThread(new Runnable() {@Override	public void run() {pd.setMessage("Compressing app");}});
			switch (BaseActivity.settings.getString("pkgr", "zip")) {
				case "zip": new CShell("root").write(getFilesDir().getPath() + "/zip -" + BaseActivity.settings.getInt("compression", 5) +
					" " + dest.substring(0, dest.length() - 3) + "app " + dest).waitForEnd(); break;
			}
			runOnUiThread(new Runnable() {@Override	public void run() {pd.dismiss();}});
			return null;
		}

		@Override
		protected void onCancelled() { super.onCancelled();	}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd.setTitle("Backing up " + name); pd.setMessage("Initialising");
			pd.setCancelable(false); pd.setCanceledOnTouchOutside(false);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.show();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}
}
