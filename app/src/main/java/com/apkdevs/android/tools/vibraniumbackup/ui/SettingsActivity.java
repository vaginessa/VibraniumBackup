package com.apkdevs.android.tools.vibraniumbackup.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.apkdevs.android.codelib.CAppCompatActivity;
import com.apkdevs.android.tools.vibraniumbackup.R;

public class SettingsActivity extends CAppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.lay_s);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSAB(toolbar);
    getSAB().setDisplayHomeAsUpEnabled(true);

  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    launchActivity(BaseActivity.class);
  }

}
