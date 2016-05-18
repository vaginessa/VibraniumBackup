package com.apkdevs.android.tools.vibraniumbackup;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.apkdevs.android.tools.vibraniumbackup.ui.BaseActivity;

public class BackupsAdapter extends ArrayAdapter<HashMap<String, Object>> {
    private List<HashMap<String, Object>> appsList = null;
    private Context context;
    private PackageManager packageManager;

    public BackupsAdapter(Context context, int textViewResourceId, List<HashMap<String, Object>> appsList) {
        super(context, textViewResourceId, appsList);
        this.context = context;
        this.appsList = appsList;
        packageManager = context.getPackageManager();
    }

    @Override
    public int getCount() {
        return ((null != appsList) ? appsList.size() : 0);
    }

    @Override
    public HashMap<String, Object> getItem(int position) {
        return ((null != appsList) ? appsList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.bkps_li, null);
        }

        HashMap<String, Object> appInfo = appsList.get(position);
        if (appInfo != null) {
            TextView appName = (TextView) view.findViewById(R.id.bkpsli_app_name);
            TextView packageName = (TextView) view.findViewById(R.id.bkpsli_app_pkg);
            ImageView iconview = (ImageView) view.findViewById(R.id.bkpsli_app_icon);
            RelativeLayout lL = (RelativeLayout) view.findViewById(R.id.bkpsli_root);
            lL.setBackgroundColor(BaseActivity.settings.getInt("bg", Color.rgb(21, 21, 21)));
            appName.setText(appInfo.get("name").toString());
            appName.setTextColor(BaseActivity.settings.getInt("textcolor", Color.rgb(255, 255, 255)));
            packageName.setText(appInfo.get("pkg").toString());
            packageName.setTextColor(BaseActivity.settings.getInt("textcolor", Color.rgb(255, 255, 255)));
            Drawable icon = (Drawable) appInfo.get("icon");
            icon.setBounds(48, 48, 48, 48);
            iconview.setImageDrawable(icon);
        }
        return view;
    }
};