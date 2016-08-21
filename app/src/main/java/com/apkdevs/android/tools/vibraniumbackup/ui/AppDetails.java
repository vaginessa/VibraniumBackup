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
import android.widget.ImageView;
import android.widget.TextView;

import com.apkdevs.android.codelib.CAppCompatActivity;
import com.apkdevs.android.codelib.CLog;
import com.apkdevs.android.codelib.CShell;
import com.apkdevs.android.tools.vibraniumbackup.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AppDetails extends CAppCompatActivity {
		static String pkg, name, ver_str;
		static int ver_int;
		static Boolean type;
		static Drawable icon;
		static ArrayList<HashMap<String, Object>> bkps;
		static File bkpsDir;
		// Graphical
			static Button bBkp;
			static Button bDel;
			static LinearLayoutCompat llBkps;
			static TextView tPkg;
			static TextView tVer;
			static ImageView iIcon;

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
			bDel = (Button) findView(R.id.lappdm_del);
			tPkg = (TextView) findView(R.id.lappdm_pkg);
			tVer = (TextView) findView(R.id.lappdm_ver);
			iIcon = (ImageView) findView(R.id.lappdm_icon);
			bkpsDir = new File(getStrSet("bkpsdir", Environment.getExternalStorageDirectory().getPath() + "/VB-Apps"));
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
			iIcon.setImageDrawable(icon);
			tPkg.setText(pkg);
			tVer.setText(String.format("%s: %s", ver_str, String.format(Locale.ENGLISH, "%d", ver_int)));
			bBkp.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {new Backup().execute();}});
			bDel.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {new Uninstall().execute();}});
	}
	private class Backup extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pd;
		@Override	protected Void doInBackground(Void... params) {
			String date = new SimpleDateFormat("HHmmss-yyMMdd", Locale.ENGLISH).format(new Date());
			File prop_f = new File(bkpsDir.getPath() + "/" + pkg + "-" + date + ".prop");
			try { prop_f.createNewFile(); } catch(IOException err) { CLog.V("propFile.createNewFile() failed, using SuperUser"); CShell.execute("su -c touch " + prop_f.getPath()); }
			try {
				FileWriter prop_w = new FileWriter(prop_f);
				prop_w.write("name=" + name + "\nverint=" + ver_int + "\nverstr=" + ver_str);
				prop_w.flush();
			} catch(IOException err) {new CShell("root").write("echo \"name=" + name + "\\nverint=" + ver_int + "\\nverstr=" + ver_str + "\" > " + prop_f.getPath() + "; exit");}
			runOnUiThread(new Runnable() {@Override	public void run() {pd.setMessage("Copying app to backups directory");}});
			CLog.V("Copying");
			String dest = bkpsDir + "/" + pkg + "-" + date + ".apk";
			String path = CShell.execute("/system/xbin/su -c pm path " + pkg).get(0).substring(8);
			CLog.V("  Running shell & waiting");
			new CShell("root").write("cp " + path + " " + dest + "; exit").waitForEnd();
			CLog.V("Done. Zipping");
			runOnUiThread(new Runnable() {@Override	public void run() {pd.setMessage("Compressing app");}});
			switch (getStrSet("pkgr", "zip")) {
				case "zip": new CShell("root").write(getFilesDir() + "/zip -" + getIntSet("compression", 5) +
					" " + (bkpsDir + "/" + pkg + "-" + date + ".app") + " " + dest + "; rm " + dest + "; exit").waitForEnd(); break;
			}
			CShell.execute("su -c rm " + dest);
			runOnUiThread(new Runnable() {@Override	public void run() {pd.dismiss();}});
		return null;}
		@Override	protected void onCancelled() { super.onCancelled();	}
		@Override	protected void onPostExecute(Void result) {	super.onPostExecute(result); }
		@Override protected void onPreExecute() {	super.onPreExecute();
			pd = new ProgressDialog(AppDetails.this);
			pd.setTitle("Backing up " + name); pd.setMessage("Initialising");
			pd.setCancelable(false); pd.setCanceledOnTouchOutside(false);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.show();
		}
		@Override	protected void onProgressUpdate(Void... values) {super.onProgressUpdate(values);}
	}

	private class Uninstall extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pd;
		@Override	protected Void doInBackground(Void... params) {
			CShell.execute("su -c pm uninstall " + pkg); finish();
			runOnUiThread(new Runnable() {@Override	public void run() {pd.dismiss();}});
		return null;}
		@Override	protected void onCancelled() { super.onCancelled();	}
		@Override	protected void onPostExecute(Void result) {	super.onPostExecute(result); }
		@Override protected void onPreExecute() {	super.onPreExecute();
			pd = new ProgressDialog(AppDetails.this);
			pd.setTitle("Uninstalling " + name); pd.setCancelable(false); pd.setProgressStyle(ProgressDialog.STYLE_SPINNER); pd.show();
		}
		@Override	protected void onProgressUpdate(Void... values) {super.onProgressUpdate(values);}
	}
	Integer getIntSet(String name, Integer def) { return BaseActivity.settings.getInt(name, def); }
	String getStrSet(String name, String def) { return BaseActivity.settings.getString(name, def); }
}
