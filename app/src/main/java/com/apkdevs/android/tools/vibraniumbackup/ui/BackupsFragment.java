package com.apkdevs.android.tools.vibraniumbackup.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
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
    private List<HashMap<String, Object>> applist;
    private BackupsAdapter listAdaptor;
    private View rootView;
    private ListView listView;
    private int listposition;

    public BackupsFragment() {}

    public static BackupsFragment newInstance() {
        return new BackupsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_bkps, container, false);
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
            HashMap<String, Object> mappedInfo = new HashMap<>();
            mappedInfo.put("name", appInfo.loadLabel(pkgMngr));
            mappedInfo.put("pkg", appInfo.packageName);
            mappedInfo.put("icon", appInfo.loadIcon(pkgMngr));
            mappedInfo.put("type", "app");
            applist.add(mappedInfo);
        }
        List<HashMap<String, Object>> bkdpappslist = getBackedupApps();
        if (bkdpappslist != null) {
            for (int i = 0; i < bkdpappslist.size(); i++) {
                applist.add(bkdpappslist.get(i));
            }
        }
        return applist;
    }

    private List<HashMap<String, Object>> getBackedupApps() {
        List<HashMap<String, Object>> apps = new ArrayList<>();
        File bkpsDir = new File(BaseActivity.settings.getString("bkpsdir", Environment.getExternalStorageDirectory().getPath() + "/VB-Apps"));
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
        /*for (String bkp : bkps) {

        }*/
        return apps;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listposition = position;
        LayoutInflater inflater = this.getLayoutInflater(null);
        AlertDialog.Builder bdialog = new AlertDialog.Builder(getContext()).setTitle(applist.get(position).get("name").toString());
        View v = inflater.inflate(R.layout.bkps_dialog, null);
        Button bkp = (Button) v.findViewById(R.id.bkpsd_app_bkp);
        bkp.setOnClickListener(this);
        bdialog.setView(v);
        AlertDialog dialog = bdialog.create();
        dialog.show();
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bkpsd_app_bkp:    // Backup button pressed
                Backup(listposition);
        }
    }

    private void Backup(int pos) {
        HashMap<String, Object> appInfo = applist.get(pos);
        CShell shell = new CShell("root");
        String path = "Unknown";
        if (appInfo.get("type").toString().equals("app")) {
            shell.write("pm path " + appInfo.get("pkg").toString());
            path = shell.getOutput();
            path = path.substring(path.indexOf(":") + 1);
            shell.clearOutput("root");
        } else if (appInfo.get("type").toString().equals("bkps")) {
            
        }
        
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            applist = convert(pkgMngr.getInstalledApplications(PackageManager.GET_META_DATA));
            listAdaptor = new BackupsAdapter(getContext(), R.layout.bkps_li, applist);
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

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
