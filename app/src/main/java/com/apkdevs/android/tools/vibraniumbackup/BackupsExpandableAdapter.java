package com.apkdevs.android.tools.vibraniumbackup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkdevs.android.tools.vibraniumbackup.ui.BaseActivity;

import java.util.HashMap;
import java.util.List;

public class BackupsExpandableAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<HashMap<String, Object>> grpMap;
    private List<HashMap<String, Object>> childMap;

    public BackupsExpandableAdapter(Context con, List<HashMap<String, Object>> mapGrp, List<HashMap<String, Object>> mapChild) {
        context = con;
        grpMap = mapGrp;
        childMap = mapChild;
    }

    @Override
    public int getGroupCount() {return grpMap.size();}

    @Override
    public int getChildrenCount(int grpPos) {return childMap.get(grpPos).size();}

    @Override
    public Object getGroup(int grpPos) {return grpMap.get(grpPos);}

    @Override
    public Object getChild(int grpPos, int childPos) {return grpMap.get(grpPos).get(childPos);}

    @Override
    public long getGroupId(int grpPos) {return grpPos;}

    @Override
    public long getChildId(int grpPos, int childPos) {return childPos;}

    @Override
    public boolean hasStableIds() {return false;}

    @Override
    public View getGroupView(int grpPos, boolean isExpanded, View convertV, ViewGroup parent) {
        if (convertV == null) {
            LayoutInflater lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertV = lInflater.inflate(R.layout.bkps_li, null);
        }
        TextView appName = (TextView) convertV.findViewById(R.id.bkpsli_app_name);
        TextView appPkg = (TextView) convertV.findViewById(R.id.bkpsli_app_pkg);
        ImageView appIcon = (ImageView) convertV.findViewById(R.id.bkpsli_app_icon);
        HashMap<String, Object> appInfo = (HashMap<String, Object>) getGroup(grpPos);
        appName.setText(appInfo.get("name").toString()); appName.setTextColor(BaseActivity.settings.getInt("color", Color.rgb(255, 255, 255)));
        appPkg.setText(appInfo.get("pkg").toString()); appPkg.setTextColor(BaseActivity.settings.getInt("color", Color.rgb(255, 255, 255)));
        Drawable icon = ((Drawable) appInfo.get("icon")); icon.setBounds(48, 48, 48, 48);
        appIcon.setImageDrawable(icon);
        convertV.setBackgroundColor(BaseActivity.settings.getInt("bg", Color.rgb(15, 15, 15)));
        return convertV;
    }

    @Override
    public View getChildView(int grpPos, int childPos, boolean isLChild, View convertV, ViewGroup parent) {
        if (convertV == null) {
            LayoutInflater lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertV = lInflater.inflate(R.layout.bkpsli_child, null);
        }
        TextView textVItem = (TextView) convertV.findViewById(R.id.bkplichild_more);
        String description = (String) getChild(grpPos, childPos);
        textVItem.setText(description);
        convertV.setBackgroundColor(BaseActivity.settings.getInt("bg", Color.rgb(15, 15, 15)));
        return convertV;
    }

    @Override
    public boolean isChildSelectable(int grpPos, int childPos) {return false;}
}
