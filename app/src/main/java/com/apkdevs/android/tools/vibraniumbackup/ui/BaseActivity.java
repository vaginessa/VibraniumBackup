package com.apkdevs.android.tools.vibraniumbackup.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.apkdevs.android.codelib.CAppCompatActivity;
import com.apkdevs.android.codelib.CLog;
import com.apkdevs.android.codelib.Prefs;
import com.apkdevs.android.tools.vibraniumbackup.R;

import java.io.File;
import java.io.IOException;


public class BaseActivity extends CAppCompatActivity {
    public static PackageManager PKGMngr;
    public static Prefs prefs;
    public static SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      // Default
      super.onCreate(savedInstanceState);
      setContentView(R.layout.lay_m);
      //Typeface androidFont = Typeface.createFromAsset(getAssets(), "fonts/Android.ttf");
      // Setup additional items (Actionbar, TabLayout)
      setSAB((Toolbar) findView(R.id.toolbar, "toolbar"));
      ViewPager mViewPager = ((ViewPager) findView(R.id.container, "container"));
      mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));
      getSAB().setTitle(newCFontedText("Vibranium Backup", "Android.ttf"));
      TabLayout tabLayout = ((TabLayout) findView(R.id.tabs, "tabs"));
      tabLayout.setupWithViewPager(mViewPager);
      PKGMngr = getAppContext().getPackageManager();
      settings = getPrefs();
      prefs = new Prefs();
      Window window = BaseActivity.this.getWindow();
      window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
      window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
      window.setStatusBarColor(settings.getInt("sb", Color.rgb(0, 0, 0)));
      // Add commands to data/data
        File existingzip = new File("/system/bin/zip");
        File embeddedzip = new File("file:///android_asset/scripts/zip");
        if (!existingzip.exists()) {
          File midzip = embeddedzip.getAbsoluteFile();
          midzip.renameTo(existingzip);
          try {
            midzip.createNewFile();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        File existingaapt = new File("/system/bin/aapt");
        File embeddedaapt = new File("file:///android_asset/scripts/aapt");
        if (!existingaapt.exists()) {
          File midaapt = embeddedaapt.getAbsoluteFile();
          midaapt.renameTo(existingaapt);
          try {
            midaapt.createNewFile();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        File bkpLoc = new File(settings.getString("bkpsdir", "/sdcard/VB-Bkps"));
        if (!bkpLoc.exists()) {bkpLoc.mkdir();}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {getMenuInflater().inflate(R.menu.menu_m, menu); return true;}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      int id = item.getItemId();
      switch (id) {
        case R.id.action_settings:
          launchActivity(SettingsActivity.class);
          return true;
      }
      return super.onOptionsItemSelected(item);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {super(fm);}

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0: return BackupsFragment.newInstance();
                case 1: return SchedulesFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {return 2;}

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return newCFontedText("BACKUPS", "Android.ttf");
                case 1: return newCFontedText("SCHEDULES", "Android.ttf");
            }
            return null;
        }
    }
    public static PackageManager getACAPkgMngr() {return PKGMngr;}
}
