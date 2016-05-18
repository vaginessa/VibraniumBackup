package com.apkdevs.android.tools.vibraniumbackup.ui;

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
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.apkdevs.android.tools.vibraniumbackup.BackupsExpandableAdapter;
import com.apkdevs.android.tools.vibraniumbackup.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BackupsFragment extends Fragment implements ExpandableListView.OnChildClickListener {
    private PackageManager pkgMngr;
    private List<HashMap<String, Object>> applist;
    private List<HashMap<String, Object>> aboutlist;
    private BackupsExpandableAdapter listAdaptor;
    private View rootView;
    private ExpandableListView listView;

    public BackupsFragment() {}

    public static BackupsFragment newInstance() {
        return new BackupsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.frag_bkps, container, false);
        pkgMngr = BaseActivity.getACAPkgMngr();
        new LoadApplications().execute();
        listView = ((ExpandableListView) (rootView.findViewById(R.id.bkps_list)));
        listView.setDividerHeight(0);
        listView.setOnChildClickListener(this);
        return rootView;
    }

    private List<HashMap<String, Object>> convert(List<ApplicationInfo> list, boolean what) {
        if (what) {
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
        } else {
            ArrayList<HashMap<String, Object>> ablist = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                ApplicationInfo appInfo = list.get(i);
                HashMap<String, Object> mappedInfo = new HashMap<>();
                mappedInfo.put("more", appInfo.loadDescription(pkgMngr));
                ablist.add(mappedInfo);
            }
            return ablist;
        }
    }

    private List<HashMap<String, Object>> getBackupUpApps() {
        List<HashMap<String, Object>> apps = null;
        File bkpsDir = new File(BaseActivity.settings.getString("bkpsdir", Environment.getExternalStorageDirectory().getPath() + "/Apps"));
        if (bkpsDir.exists()) {
            //Nothing(for now)
        }
        return apps;
    }

    public boolean onChildClick(ExpandableListView parent, View v, int grpPos, int childPos, long id) {
        Toast.makeText(getContext(),
                "id: " + id + ". Child pos: " + Integer.toString(childPos) + ", Group pos: " + Integer.toString(grpPos) + ".",
                Toast.LENGTH_LONG).show();
        return true;
    }



    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            applist = convert(pkgMngr.getInstalledApplications(PackageManager.GET_META_DATA), true);
            aboutlist = convert(pkgMngr.getInstalledApplications(PackageManager.GET_META_DATA), false);
            listAdaptor = new BackupsExpandableAdapter(getContext(), applist, aboutlist);
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
