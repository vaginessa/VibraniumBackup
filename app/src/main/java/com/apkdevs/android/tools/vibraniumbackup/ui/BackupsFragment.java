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
import android.widget.ListView;

import com.apkdevs.android.tools.vibraniumbackup.BackupsAdapter;
import com.apkdevs.android.tools.vibraniumbackup.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BackupsFragment extends Fragment implements AdapterView.OnItemClickListener {
    private PackageManager pkgMngr;
    private List<HashMap<String, Object>> applist;
    private BackupsAdapter listAdaptor;
    private View rootView;
    private ListView listView;

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
          applist.add(mappedInfo);
        }
        List<HashMap<String, Object>> bkdpappslist = getBackupUpApps();
        if (bkdpappslist != null) {
            for (int i = 0; i < bkdpappslist.size(); i++) {
                applist.add(bkdpappslist.get(i));
            }
        }
        return applist;
    }

    private List<HashMap<String, Object>> getBackupUpApps() {
        List<HashMap<String, Object>> apps = null;
        File bkpsDir = new File(BaseActivity.settings.getString("bkpsdir", Environment.getExternalStorageDirectory().getPath() + "/Apps"));
        if (bkpsDir.exists()) {
            //Nothing(for now)
        }
        return apps;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
