package com.apkdevs.android.tools.vibraniumbackup.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.apkdevs.android.codelib.CAppCompatActivity;
import com.apkdevs.android.tools.vibraniumbackup.R;

public class SettingsActivity extends CAppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.created(savedInstanceState, R.class);
    setContentView(R.layout.lay_s);
    setSAB((Toolbar) findViewById(R.id.toolbar));
    getSAB().setDisplayHomeAsUpEnabled(true);

  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    launchActivity(BaseActivity.class);
  }
}
